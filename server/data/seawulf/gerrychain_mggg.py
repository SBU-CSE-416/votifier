import argparse
import json
import random

import pandas as pd
import matplotlib.pyplot as plt
import networkx as nx
from networkx.readwrite import json_graph

import gerrychain
from gerrychain import (
   Partition, MarkovChain, Election, constraints, accept
)
from gerrychain.updaters import Tally, cut_edges
from gerrychain.proposals import recom
from gerrychain.tree import bipartition_tree

from functools import partial
from seawulf_constants import *

def race_percentage_updater(selected_racial_group):
    def updater(partition):
        percentages = {}
        for district, nodes in partition.parts.items():
            total_population = sum(partition.graph.nodes[node]["TOT_POP22"] for node in nodes)
            race_population = sum(partition.graph.nodes[node][selected_racial_group] for node in nodes)
            percentages[district] = (race_population / total_population) * 100 if total_population > 0 else 0
        return percentages
    return updater

def election_results_updater(partition):
    results = {}
    for district, nodes in partition.parts.items():
        dem_votes = sum(partition.graph.nodes[node]["TOT_DEM"] for node in nodes)
        rep_votes = sum(partition.graph.nodes[node]["TOT_REP"] for node in nodes)
        winner = "Democratic" if dem_votes > rep_votes else "Republican"
        results[district] = {
            "Democratic": dem_votes,
            "Republican": rep_votes,
            "Winner": winner
        }
    return results

def cut_edges_length(partition):
  return len(partition["cut_edges"])



def generate_new_districting_plan(state_graph_json_file, plan_id, num_iterations, pop_constraint):
  random.seed(plan_id)
  with open(state_graph_json_file) as precinct_graph_file:
    precinct_graph_json = json.load(precinct_graph_file)
  precinct_graph = json_graph.node_link_graph(precinct_graph_json, edges="links")

  elections = [
    Election("GOV_22", {"Democratic": "TOT_DEM", "Republican": "TOT_REP"})
  ]

  racial_group_updater_white = race_percentage_updater(WHITE_POP_LABEL)
  racial_group_updater_black = race_percentage_updater(BLACK_POP_LABEL)
  racial_group_updater_asian = race_percentage_updater(ASIAN_POP_LABEL)
  racial_group_updater_hispanic = race_percentage_updater(HISPANIC_POP_LABEL)

  all_updaters = {
      "population": Tally("TOT_POP22", alias="population"),
      "cut_edges": cut_edges,
      "2022_gubernatorial_election_results": election_results_updater,
      "racial_percentages_white": racial_group_updater_white,
      "racial_percentages_black": racial_group_updater_black,
      "racial_percentages_asian": racial_group_updater_asian,
      "racial_percentages_hispanic": racial_group_updater_hispanic
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

  last_partition = None
  for partition in recom_chain.with_progress_bar():
     last_partition = partition

  gubernatorial_election_results = last_partition["2022_gubernatorial_election_results"]
  racial_results_white = last_partition["racial_percentages_white"]
  racial_results_black = last_partition["racial_percentages_black"]
  racial_results_asian = last_partition["racial_percentages_asian"]
  racial_results_hispanic = last_partition["racial_percentages_hispanic"]

  racial_groups_df = pd.DataFrame({
    "White": racial_results_white,
    "Black": racial_results_black,
    "Asian": racial_results_asian,
    "Hispanic": racial_results_hispanic
  })

  summary_stats = racial_groups_df.describe()
  print(summary_stats)
  
  election_results_df = pd.DataFrame.from_dict(gubernatorial_election_results, orient="index")
  election_results_df.to_csv("results/plan_election_results.csv")
  return

if __name__ == "__main__":
    POP_CONSTRAINT = 0.08
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--state_graph_file', type=str, required=True)
    arguments_parser.add_argument('--plan_id', type=str, required=True)
    arguments_parser.add_argument('--iterations', type=int, required=True)
    args = arguments_parser.parse_args()
    generated_plan = generate_new_districting_plan(args.state_graph_file, args.plan_id, args.iterations, POP_CONSTRAINT)
    # Not yet implemented: Exporting the generated data