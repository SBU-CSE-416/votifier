import json
import matplotlib.pyplot as plt
from matplotlib.ticker import (MultipleLocator, AutoMinorLocator)
import argparse
import numpy as np

def is_sequential(lst):
    # Check if the difference between consecutive elements is always 1
    return all(lst[i] + 1 == lst[i + 1] for i in range(len(lst) - 1))

def plot_chart(race, state):
    # Load the JSON data from a file
  file_path = f"box_and_whisker_results_{state}_racial_group_{race}.json"  # Replace with your JSON file path
  with open(file_path, "r") as file:
      json_data = json.load(file)

  from matplotlib.patches import Rectangle
  from matplotlib.ticker import MultipleLocator

  # Extract and sort data for each district by the 2022_DOT_VALUE
  districts = json_data["data"].keys()
  boxplot_data = []

  for district in districts:
      district_data = json_data["data"][district]
      boxplot_data.append({
          "district": district,
          "min": district_data["MIN"],
          "q1": district_data["LOWER_QUARTILE_Q1"],
          "median": district_data["MEDIAN"],
          "q3": district_data["UPPER_QUARTILE_Q3"],
          "max": district_data["MAX"],
          "dot": district_data["2022_DOT_VALUE"]
      })

  # Sort districts by their dot value
  # sorted_boxplot_data = sorted(boxplot_data, key=lambda x: x["dot"])

  # Set up the plot
  fig, ax = plt.subplots(figsize=(10, 6))

  # Plot each district's box-and-whisker manually in sorted order
  for idx, data in enumerate(boxplot_data):
      x = idx + 1.0
      # Draw whiskers
      ax.plot([x, x], [data["min"], data["q1"]], color="black")  # Lower whisker
      ax.plot([x, x], [data["q3"], data["max"]], color="black")  # Upper whisker

      # Draw whisker ends
      ax.hlines([data["min"], data["max"]], xmin=x - 0.2, xmax=x + 0.2, color="black", linewidth=1)

      # Draw box
      rect = Rectangle((x - 0.2, data["q1"]), 0.4, data["q3"] - data["q1"], edgecolor="black", facecolor=(88/255, 221/255, 211/255, 0.72))
      ax.add_patch(rect)

      # Draw median line
      ax.plot([x - 0.2, x + 0.2], [data["median"], data["median"]], color="green", linewidth=1)

      # Plot red dot
      ax.plot(x, data["dot"], 'ro', label="2022 Enacted Plan" if idx == 0 else "")



  # Add labels and title
  ax.grid(axis="y")
  plt.gca().tick_params(axis='x', which='minor', bottom=False)
  ax.set_axisbelow(True)
  ax.grid(linewidth = 0.4, alpha = 0.75)
  fig.suptitle(json_data["labels"]["title"])
  plt.title(json_data["labels"]["subtitle"])
  ax.set_xlabel(json_data["labels"]["axis-x"])
  ax.set_ylabel(json_data["labels"]["axis-y"])
  ax.set_xticks(range(1, len(boxplot_data) + 1))
  ax.tick_params(axis='y', which='major', length=12)
  ax.tick_params(axis='y', which='minor', length=6)
  ax.set_xticklabels([data["district"].lstrip('0') for data in boxplot_data])

  # Add the axis-y ticks
  yticks = json_data["labels"]["axis-y-ticks"]
  ax.set_yticks(json_data["labels"]["axis-y-ticks"])
  if is_sequential(yticks) is False:
    ax.yaxis.set_minor_locator(MultipleLocator(1))
    ax.tick_params(axis='y', which='minor')
    # plt.minorticks_on()

  # Display the legend and plot
  ax.legend()
  plt.show()
  return


if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--race', type=str, required=True)
    arguments_parser.add_argument('--state', type=str, required=True)
    args = arguments_parser.parse_args()
    plot_chart(args.race, args.state)