import pandas as pd
import argparse
import json
import numpy as np
from seawulf_constants import *

def calculate_y_axis_ticks(min_val, max_val):
    starting_ticks = 10

    if min_val > 4:
        min_val -= 4
    max_val += 4

    tick_interval = int((max_val - min_val) / (starting_ticks - 1))
    if tick_interval == 0:
        tick_interval = 1  # Avoid division by zero by defaulting to 1
    
    ticks = np.arange(min_val, max_val + tick_interval, tick_interval).tolist()
    return ticks

def get_district_buckets(ensemble_id, racial_group, plan_count):
    district_buckets = None
    for plan_id in range(1, (plan_count+1)):
        racial_groups_result_path = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_racial_groups_results.json"
        with open(racial_groups_result_path, 'r') as racial_groups_results_file:
            racial_groups_district_percentages_json = json.load(racial_groups_results_file)
        if district_buckets is None:
            num_districts = len(racial_groups_district_percentages_json)
            district_buckets = {
                str(i).zfill(2): [] 
                for i in range(1, num_districts + 1)
            }
        for district_id, racial_data in racial_groups_district_percentages_json.items():
            racial_group_percentage = racial_data[racial_group.upper() + "_PERCENTAGE"]
            # print(f"Ensemble {ensemble_id} | Plan {plan_id} | District {district_id} | Racial Group {racial_group} | Percentage {racial_group_percentage}")
            district_buckets[district_id].append(racial_group_percentage)
    return district_buckets

def get_box_and_whisker_data(group_district_buckets, racial_group, districts_2022_summary_file):
    district_buckets = {}
    for district_id in group_district_buckets:
        district_data = next(
        (d for d in districts_2022_summary_file["data"] if d["CONG_DIST"] == int(district_id)),
        None
        )
        district_2022_actual_percentage = district_data[racial_group.upper() + "_PERCENT"]
        district_min_percentage = int(np.min(group_district_buckets[district_id]))
        district_Q1_percentage = int(np.percentile(group_district_buckets[district_id], 25))
        district_median_percentage = int(np.median(group_district_buckets[district_id]))
        district_Q3_percentage = int(np.percentile(group_district_buckets[district_id], 75))
        district_max_percentage = int(np.max(group_district_buckets[district_id]))
        
        district_buckets[district_id] = {
            "MIN": district_min_percentage,
            "LOWER_QUARTILE_Q1": district_Q1_percentage,
            "MEDIAN": district_median_percentage,
            "UPPER_QUARTILE_Q3": district_Q3_percentage,
            "MAX": district_max_percentage,
            "2022_DOT_VALUE": district_2022_actual_percentage
        }
    return district_buckets

def postprocessing_process(ensemble_id, state_abbr, racial_group, plan_count):
    group_district_buckets = get_district_buckets(ensemble_id, racial_group, plan_count)

    all_values = [value for district in group_district_buckets.values() for value in district]
    global_min = int(np.floor(min(all_values)))
    if global_min < 4:
        global_min = 0
    global_max = int(np.ceil(max(all_values)))

    congressional_districts_2022_filepath = f"{state_abbr.lower()}_congressional_districts_summary_seawulf.json"
    with open(congressional_districts_2022_filepath, 'r') as congressional_districts_2022_file:
        districts_2022_json = json.load(congressional_districts_2022_file)
    group_box_and_whisker_data = get_box_and_whisker_data(group_district_buckets, racial_group, districts_2022_json)

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

    yticks = calculate_y_axis_ticks(global_min, global_max)
    labels = {
        "title" : f"{racial_group.capitalize()} Population Percentage in Ensemble District Plans vs. 2022 Enacted Plan",
        "subtitle": f"({state_name} - Ensemble {chart_ensemble_id}, {plan_count} Plans)",
        "axis-x": f"Sorted Districts",
        "axis-y": f"District {racial_group.capitalize()} Percentage (%)",
        "axis-x-ticks" : [i for i in range(1, len(group_district_buckets.keys())+1)],
        "axis-y-ticks": yticks
    }

    finalized_racial_whisker_json = {
        "NAME": state_name,
        "ENSEMBLE_ID": ensemble_id,
        "TOT_PLANS": plan_count,
        "RACIAL_GROUP": racial_group.upper(),
        "labels" : labels,
        "data": group_box_and_whisker_data
    }

    finalized_racial_whisker_json_str = json.dumps(finalized_racial_whisker_json, indent=4)
    finalized_path = f"results/ensemble_{ensemble_id}/box_and_whisker_results_{state_abbr}_racial_group_{racial_group.upper()}.json"
    with open(finalized_path, 'w') as json_file:
        json_file.write(finalized_racial_whisker_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--racial_group', type=str, required=True)
    arguments_parser.add_argument('--plan_count', type=int, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr, args.racial_group, args.plan_count)