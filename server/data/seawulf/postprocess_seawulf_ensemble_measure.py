import pandas as pd
import argparse
import json
import numpy as np
from seawulf_constants import *

def calculate_y_axis_ticks(min_val, max_val, starting_ticks):
    # Adjust min_val and max_val to the nearest even numbers
    if min_val % 2 != 0:
        min_val -= 1  # Round down to the nearest even number
    if max_val % 2 != 0:
        max_val += 1  # Round up to the nearest even number
    tick_range = max_val - min_val
    if tick_range < starting_ticks:
        starting_ticks = starting_ticks / 2
    ticks = None
    if tick_range < starting_ticks * 2:
        tick_interval = 2
        ticks = np.arange(min_val, max_val + tick_interval, tick_interval).tolist()
        alt = 0 
        while len(ticks) < starting_ticks:
            if alt == 0 or alt == 3:
                # Back half
                last_index = len(ticks)-1
                new_tick = ticks[last_index] + tick_interval
                ticks.insert(last_index+1, new_tick)
                if alt != 3:
                    alt = 1
            else:
                # Front half
                if ticks[0] - tick_interval < 0:
                    alt = 3
                else:
                    new_tick = ticks[0] - tick_interval
                    ticks.insert(0, new_tick)
                    alt = 0
    else:
        tick_interval = int((max_val - min_val) / (starting_ticks))
        if tick_interval == 0:
            tick_interval = 1  # Avoid division by zero by defaulting to 1
        ticks = np.arange(min_val, max_val + tick_interval, tick_interval).tolist()
    return ticks

def generate_outcome_buckets(total_districts):
    # Generate all possible outcomes of wins for DEM and REP in a given number of districts
    outcome_dict = {}
    for dem_wins in range(total_districts + 1):
        rep_wins = total_districts - dem_wins
        key = f"{dem_wins}:{rep_wins}"
        outcome_dict[key] = 0 
    return outcome_dict

def postprocessing_process(ensemble_id, state_abbr, plan_count):
    buckets = None
    district_count = 0
    for plan_id in range(1, plan_count+1):
        with open(f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_election_results.json") as plan_election_results_file:
            plan_election_results_json = json.load(plan_election_results_file)
        if buckets is None:
            district_count = len(plan_election_results_json)
            buckets = generate_outcome_buckets(district_count)
        rep_wins = 0
        dem_wins = 0
        for district, election_data in plan_election_results_json.items():
            if election_data["WIN"] == "REP":
                rep_wins += 1
            else:
                dem_wins += 1
        split_str = f"{dem_wins}:{rep_wins}"
        buckets[split_str] += 1
    
    max_val = 0
    for bucket, value in buckets.items():
        if value > max_val:
            max_val = value
    
    chart_ensemble_id = ensemble_id
    if ensemble_id == 3:
        chart_ensemble_id = 1
    elif ensemble_id == 4:
        chart_ensemble_id = 2

    state_name = None

    if state_abbr == "MD":
        state_name = "Maryland"
    elif state_abbr == "SC":
        state_name = "South Carolina"

    yticks = calculate_y_axis_ticks(0, (max_val + 4), 10)
    labels = {
        "title" : f"Vote Splits Across Ensemble",
        "subtitle": f"({state_name} - Ensemble {chart_ensemble_id}, {plan_count} Plans)",
        "axis-x": "Win Ratios (Democratic Party:Republican Party)",
        "axis-y": f"Outcome Frequency",
        "axis-x-ticks" : list(buckets.keys()),
        "axis-y-ticks": yticks,
    }

    finalized_json = {
        "NAME": state_name,
        "ENSEMBLE_ID": ensemble_id,
        "TOT_PLANS": plan_count,
        "labels" : labels,
        "data": buckets
    }
    finalized_json_str = json.dumps(finalized_json, indent=4)
    finalized_path = f"results/ensemble_{ensemble_id}/vote_splits_{state_abbr}.json"
    with open(finalized_path, 'w') as json_file:
        json_file.write(finalized_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--plan_count', type=int, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr, args.plan_count)