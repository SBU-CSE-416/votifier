import argparse
import json
import random
import os

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
            percentages[district] = round((race_population / total_population) * 100) if total_population > 0 else 0
        return percentages
    return updater

def election_results_updater(partition):
    results = {}
    for district, nodes in partition.parts.items():
        dem_votes = sum(partition.graph.nodes[node]["TOT_DEM"] for node in nodes)
        rep_votes = sum(partition.graph.nodes[node]["TOT_REP"] for node in nodes)
        winner = "DEM" if dem_votes > rep_votes else "REP"
        results[district] = {
            "TOT_DEM": dem_votes,
            "TOT_REP": rep_votes,
            "WIN": winner
        }
    return results   

def region_results_updater(partition):
    results = {}
    region_counts = {"urban": 0, "rural": 0, "suburban": 0}
    for district, nodes in partition.parts.items():
        region_counts = {"urban": 0, "rural": 0, "suburban": 0}
        region_percentages = {}
        for precinct_node in nodes:
            precinct_region_type = partition.graph.nodes[precinct_node]["region_type"]
            region_counts[precinct_region_type] += 1
        total_precincts = len(nodes)
        if total_precincts > 0:
            urban_percentage = ((region_counts["urban"] / total_precincts) * 100)
            suburban_percentage = ((region_counts["suburban"] / total_precincts) * 100)
            rural_percentage = ((region_counts["rural"] / total_precincts) * 100)
            rounded_urban = round(urban_percentage)
            rounded_suburban = round(suburban_percentage)
            rounded_rural = round(rural_percentage)
            rounded_sum = rounded_urban + rounded_suburban + rounded_rural
            region_percentages = {
                  "URBAN_PERCENTAGE": rounded_urban,
                  "SUBURBAN_PERCENTAGE": rounded_suburban,
                  "RURAL_PERCENTAGE": rounded_rural
            }
            if rounded_sum == 99 or rounded_sum == 101:
                decimal_portions = {}
                for region, percentage in region_percentages.items():
                    # Calculate decimal portion by subtracting the int part from percentage
                    decimal_value = percentage - int(percentage)
                    # Store the decimal portion in dict with corresponding region type
                    decimal_portions[region] = decimal_value
                decimal_region = None
                if rounded_sum == 99: 
                  decimal_region = max(decimal_portions, key=decimal_portions.get)
                  region_percentages[decimal_region] += 1
                if rounded_sum == 101: 
                  decimal_region = min(decimal_portions, key=decimal_portions.get)
                  region_percentages[decimal_region] -= 1
        else:
            # Edge case but should be OK
            region_percentages = {
                "URBAN_PERCENTAGE": 0,
                "SUBURBAN_PERCENTAGE": 0,
                "RURAL_PERCENTAGE": 100
            }
        results[district] = region_percentages
    return results

def economic_results_updater(partition):
    results = {}
    for district, nodes in partition.parts.items():
        precinct_weighted_sums = []
        precinct_households = []
        for precinct_node in nodes:
          precinct_avg_household_income = partition.graph.nodes[precinct_node]["AVG_HOUSEHOLD_INCOME"]
          precinct_tot_households = partition.graph.nodes[precinct_node]["TOT_HOUS22"]
          precinct_households.append(precinct_tot_households)
          precinct_weighted_sums.append(precinct_avg_household_income * precinct_tot_households)
        district_average_income_total = sum(precinct_weighted_sums)
        district_households_count = int(sum(precinct_households))
        district_average_household_income = round(district_average_income_total / district_households_count)
        results[district] = {
          "AVG_HOUSEHOLD_INCOME": district_average_household_income,
          "TOT_HOUSEHOLDS": district_households_count,
        }
    return results


def cut_edges_length(partition):
  return len(partition["cut_edges"])


def generate_new_districting_plan(state_graph_json_file, ensemble_id, plan_id, num_iterations, pop_constraint):
  random.seed(ensemble_id + plan_id)
  with open(state_graph_json_file) as precinct_graph_file:
    precinct_graph_json = json.load(precinct_graph_file)
  precinct_graph = json_graph.node_link_graph(precinct_graph_json)

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
      "racial_percentages_hispanic": racial_group_updater_hispanic,
      "economic_results": economic_results_updater,
      "region_results": region_results_updater
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
        allow_pair_reselection=True
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
  for partition in recom_chain:
     last_partition = partition

  district_precinct_maps = {}
  for district, nodes in last_partition.parts.items():
      if district not in district_precinct_maps:
        district_precinct_maps[district] = []  # Initialize as an empty list
      for precinct_node in nodes:
         district_precinct_maps[district].append(precinct_node)
  district_precinct_output_path = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_precincts_results.json"
  os.makedirs(os.path.dirname(district_precinct_output_path), exist_ok=True)
  district_precinct_json = json.dumps(district_precinct_maps, indent=4)
  with open(district_precinct_output_path, 'w') as json_file_precincts:
    json_file_precincts.write(district_precinct_json)

  gubernatorial_election_results = last_partition["2022_gubernatorial_election_results"]
  racial_results_white = last_partition["racial_percentages_white"]
  racial_results_black = last_partition["racial_percentages_black"]
  racial_results_asian = last_partition["racial_percentages_asian"]
  racial_results_hispanic = last_partition["racial_percentages_hispanic"]
  economic_results = last_partition["economic_results"]
  region_results = last_partition["region_results"]
  population_results = last_partition["population"]

  racial_groups_df = pd.DataFrame({
    "WHITE_PERCENTAGE": racial_results_white,
    "BLACK_PERCENTAGE": racial_results_black,
    "ASIAN_PERCENTAGE": racial_results_asian,
    "HISPANIC_PERCENTAGE": racial_results_hispanic
  })
  population_results_df = pd.DataFrame.from_dict(population_results, orient="index")
  population_results_dict = {
      district: {"TOT_POP": int(values[0])} 
      for district, values in population_results_df.to_dict(orient="index").items()
  }
  population_results_json = json.dumps(population_results_dict, indent=4)
  population_output_path = (f'results/ensemble_{ensemble_id}/plan_{plan_id}/district_population_results.json')
  os.makedirs(os.path.dirname(population_output_path), exist_ok=True)
  with open(population_output_path, 'w') as json_file:
    json_file.write(population_results_json)

  racial_groups_dict = racial_groups_df.to_dict(orient="index")
  racial_groups_json = json.dumps(racial_groups_dict, indent=4)
  racial_output_path = (f'results/ensemble_{ensemble_id}/plan_{plan_id}/district_racial_groups_results.json')
  os.makedirs(os.path.dirname(racial_output_path), exist_ok=True)
  with open(racial_output_path, 'w') as json_file:
    json_file.write(racial_groups_json)

  economic_results_df = pd.DataFrame.from_dict(economic_results, orient="index")
  economic_results_dict = economic_results_df.to_dict(orient="index")
  economic_results_json = json.dumps(economic_results_dict, indent=4)
  economic_output_path = (f'results/ensemble_{ensemble_id}/plan_{plan_id}/district_economic_results.json')
  os.makedirs(os.path.dirname(economic_output_path), exist_ok=True)
  with open(economic_output_path, 'w') as json_file:
    json_file.write(economic_results_json)
  
  election_results_df = pd.DataFrame.from_dict(gubernatorial_election_results, orient="index")
  election_results_dict = election_results_df.to_dict(orient="index")
  election_results_json = json.dumps(election_results_dict, indent=4)
  election_output_path = (f'results/ensemble_{ensemble_id}/plan_{plan_id}/district_election_results.json')
  os.makedirs(os.path.dirname(election_output_path), exist_ok=True)
  with open(election_output_path, 'w') as json_file:
    json_file.write(election_results_json)

  region_results_df = pd.DataFrame.from_dict(region_results, orient="index")
  region_results_dict = region_results_df.to_dict(orient="index")
  region_results_json = json.dumps(region_results_dict, indent=4)
  region_output_path = (f'results/ensemble_{ensemble_id}/plan_{plan_id}/district_region_results.json')
  os.makedirs(os.path.dirname(region_output_path), exist_ok=True)
  with open(region_output_path, 'w') as json_file:
    json_file.write(region_results_json)
  return

if __name__ == "__main__":
    POP_CONSTRAINT = 0.08
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--state_graph_file', type=str, required=True)
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--plan_id', type=int, required=True)
    arguments_parser.add_argument('--iterations', type=int, required=True)
    args = arguments_parser.parse_args()
    generate_new_districting_plan(args.state_graph_file, args.ensemble_id, args.plan_id, args.iterations, POP_CONSTRAINT)