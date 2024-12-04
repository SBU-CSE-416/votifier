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
    Election("GOV_22", {"Democratic": "GOV_22_DEM", "Republican": "GOV_22_REP"})
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
      epsilon=0.01,
      node_repeats=2,
      method = partial(
        bipartition_tree,
        max_attempts=50,
      )
  )

  compactness_bound = constraints.UpperBound(
    cut_edges_length,
    2*len(initial_partition["cut_edges"])
  )

  recom_chain = MarkovChain(
    proposal=proposal,
    constraints=[contiguous,
                 pop_constraint,
                 compactness_bound],
    accept=accept.always_accept,
    initial_state=initial_partition,
    total_steps=num_iterations
  )

  election_data = pd.DataFrame(
    [sorted(partition["GOV_22"].percents("Democratic"))
    for partition in recom_chain]
  )

  return

if __name__ == "__main__":
    POP_CONSTRAINT = 0.02
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--state_graph_file', type=str, required=True)
    arguments_parser.add_argument('--plan_id', type=str, required=True)
    arguments_parser.add_argument('--iterations', type=int, required=True)
    args = arguments_parser.parse_args()
    generated_plan = generate_new_districting_plan(args.state_graph_file, args.plan_id, args.iterations, POP_CONSTRAINT)
    calculate_district_election_winner(generated_plan)
    # Save plan to file path?