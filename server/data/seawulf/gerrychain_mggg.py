import gerrychain
from gerrychain import (Partition, Graph, MarkovChain, Election, updaters, constraints, accept)
from gerrychain.updaters import Tally, cut_edges
from gerrychain.proposals import recom
from gerrychain.tree import bipartition_tree
from gerrychain.constraints import contiguous
from functools import partial
import argparse
import json
import networkx as nx
from networkx.readwrite import json_graph
import random
import pandas as pd
import matplotlib.pyplot as plt

def calculate_district_election_winner():
   return

def cut_edges_length(partition):
  return len(partition["cut_edges"])

def generate_new_districting_plan(state_graph_json_file, plan_id, num_iterations, pop_constraint):
  random.seed(plan_id)
  with open(state_graph_json_file) as precinct_graph_file:
    precinct_graph_json = json.load(precinct_graph_file)
  precinct_graph = json_graph.node_link_graph(precinct_graph_json, edges="links")

  # Set up the initial partition object
  elections = [
    Election("GOV_22", {"Democratic": "TOT_DEM", "Republican": "TOT_REP"})
  ]

  all_updaters = {
      "population": Tally("TOT_POP22", alias="population"),
      "cut_edges": cut_edges,
  }

  election_updaters = {election.name: election for election in elections}
  all_updaters.update(election_updaters)

  initial_partition = Partition(
    precinct_graph,
    assignment="DIS_22",
    updaters=all_updaters
  )

  ideal_population = sum(initial_partition["population"].values()) / len(initial_partition)
  
  proposal = partial(
      recom,
      pop_col="TOT_POP22",
      pop_target=ideal_population,
      epsilon=pop_constraint,
      node_repeats=4,
      method = partial(
        bipartition_tree,
        max_attempts=100,
      )
  )

  compactness_bound = constraints.UpperBound(
    cut_edges_length,
    2*len(initial_partition["cut_edges"])
  )

  pop_constraint_value = constraints.within_percent_of_ideal_population(initial_partition, pop_constraint)

  recom_chain = MarkovChain(
    proposal=proposal,
    constraints=[pop_constraint_value,
                 compactness_bound],
    accept=accept.always_accept,
    initial_state=initial_partition,
    total_steps=num_iterations
  )

  election_data = pd.DataFrame(
    [sorted(partition["GOV_22"].percents("Democratic"))
    for partition in recom_chain.with_progress_bar()]
  )

  election_data.to_csv("last_partition.csv", index=False)

  fig, ax = plt.subplots(figsize=(8, 6))

  # Draw 50% line
  ax.axhline(0.5, color="#cccccc")

  # Draw boxplot
  election_data.boxplot(ax=ax, positions=range(len(election_data.columns)))
  # Draw initial plan's Democratic vote %s (.iloc[0] gives the first row)
  plt.plot(election_data.iloc[0], "ro")

  # Annotate
  ax.set_title("Comparing the 2022 plan to an ensemble")
  ax.set_ylabel("Democratic vote % (Governor 2022")
  ax.set_xlabel("Sorted Districts")
  ax.set_ylim(0, 1)
  ax.set_yticks([0, 0.25, 0.5, 0.75, 1])

  plt.savefig('plotdemo.png')
  return

if __name__ == "__main__":
    POP_CONSTRAINT = 0.1
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--state_graph_file', type=str, required=True)
    arguments_parser.add_argument('--plan_id', type=str, required=True)
    arguments_parser.add_argument('--iterations', type=int, required=True)
    args = arguments_parser.parse_args()
    generated_plan = generate_new_districting_plan(args.state_graph_file, args.plan_id, args.iterations, POP_CONSTRAINT)
    # calculate_district_election_winner(generated_plan)
    # Save plan to file path?