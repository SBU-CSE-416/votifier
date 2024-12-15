import json
import matplotlib.pyplot as plt
from matplotlib.ticker import (MultipleLocator, AutoMinorLocator)
import argparse
from matplotlib.colors import LinearSegmentedColormap
import numpy as np


def plot_chart(state):
    file_path = f"vote_splits_{state}_2.json"
    with open(file_path, "r") as file:
        json_data = json.load(file)

    # Extracting relevant data
    labels = json_data['labels']
    data = json_data['data']

    # Extract x and y axis data
    x_ticks = labels['axis-x-ticks']
    y_ticks = labels['axis-y-ticks']
    values = [data[tick] for tick in x_ticks]

    # Plotting the bar chart
    plt.figure(figsize=(10, 6))
    str_title = json_data["labels"]["title"]
    str_subtitle = json_data["labels"]["subtitle"]
    plt.title(f"{str_title}\n{str_subtitle}")
    plt.xlabel(labels['axis-x'])
    plt.ylabel(labels['axis-y'])
    plt.xticks(rotation=0)
    plt.minorticks_on()
    plt.gca().yaxis.set_minor_locator(MultipleLocator(8))
    plt.grid(which='major', axis='y', linestyle='-', linewidth=0.3, color='black', zorder=0)
    plt.grid(which='minor', axis='y', linestyle='-', linewidth=0.1, color='black', zorder=0)
    cmap = LinearSegmentedColormap.from_list('custom_red_white_blue', ['tab:red', 'peachpuff', 'tab:blue'])
    plt.bar(x_ticks, values, alpha=1, zorder=2, color=cmap(np.linspace(0, 1, len(values))))
    
    plt.yticks(y_ticks)
    plt.tight_layout()  # Adjusts plot to fit labels better
    plt.show()

if __name__ == "__main__":
    # Argument parsing
    parser = argparse.ArgumentParser()
    parser.add_argument('--state', type=str, required=True)
    args = parser.parse_args()
    plot_chart(args.state)