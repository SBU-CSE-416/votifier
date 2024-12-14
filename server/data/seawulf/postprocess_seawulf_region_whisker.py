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

def get_district_buckets(ensemble_id, region_type, plan_count):
    # Basically, for each random plan, you have like 7-8 districts. What we do is that we sort those disricts in order of increasing racial/economic/region type percentage. 
    # For example, if the districts were 1,2,3 and 1 has 72 and 2 has 65 and 3 has 74, the sort would be 2:65, 1:72, 3:74. Then, place the first of those sorted districts into 
    # bucket 1, the second into bucket 2, the third into bucket 3. Then you take the q1 q3 median etc. from each of those buckets like I did before.

    district_buckets = None
    for plan_id in range(1, (plan_count+1)):
        region_types_result_path = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_region_results.json"
        with open(region_types_result_path, 'r') as region_types_results_file:
            region_type_percentages_json = json.load(region_types_results_file)
        if district_buckets is None:
            num_districts = len(region_type_percentages_json)
            district_buckets = {
                str(i).zfill(2): [] 
                for i in range(1, num_districts + 1)
            }
        current_plan_districts = {}
        for district_id, region_data in region_type_percentages_json.items():
            region_type_percentage = region_data[region_type.upper() + "_PERCENT"]
            current_plan_districts[district_id] = region_type_percentage
        sorted_current_plan_districts = dict(sorted(current_plan_districts.items(), key=lambda x: x[1]))
        sorted_current_plan_districts_list = list(sorted_current_plan_districts.items())
        j = 0
        for bucket in district_buckets:
            district_buckets[bucket].append(sorted_current_plan_districts_list[j][1])
            j += 1
    return district_buckets

def get_box_and_whisker_data(group_district_buckets, districts_2022_group_list):
    data = {}
    i = 0
    for bucket in group_district_buckets:
        district_2022_actual_percentage = districts_2022_group_list[i][1]
        bucket_min_percentage = int(np.min(group_district_buckets[i][1]))
        bucket_Q1_percentage = int(np.percentile(group_district_buckets[i][1], 25))
        bucket_median_percentage = int(np.median(group_district_buckets[i][1]))
        bucket_Q3_percentage = int(np.percentile(group_district_buckets[i][1], 75))
        bucket_max_percentage = int(np.max(group_district_buckets[i][1]))
        data[bucket[0]] = {
            "MIN": bucket_min_percentage,
            "LOWER_QUARTILE_Q1": bucket_Q1_percentage,
            "MEDIAN": bucket_median_percentage,
            "UPPER_QUARTILE_Q3": bucket_Q3_percentage,
            "MAX": bucket_max_percentage,
            "2022_DOT_VALUE": district_2022_actual_percentage
        }
        i += 1
    return data

def postprocessing_process(ensemble_id, state_abbr, region_type, plan_count):
    group_district_buckets = get_district_buckets(ensemble_id, region_type, plan_count)
    group_district_buckets_list = list(group_district_buckets.items())


    congressional_districts_2022_filepath = f"{state_abbr.lower()}_congressional_districts_summary_seawulf.json"
    with open(congressional_districts_2022_filepath, 'r') as congressional_districts_2022_file:
        districts_2022_json = json.load(congressional_districts_2022_file)
        region_type_key = region_type.upper() + "_PERCENT"
        result = {
            str(item['CONG_DIST']).zfill(2): item[region_type_key]
            for item in districts_2022_json['data']
        }
        sorted_districts_2022_json = dict(sorted(result.items(), key=lambda x: x[1]))
        sorted_districts_2022_list = list(sorted_districts_2022_json.items())
    group_box_and_whisker_data = get_box_and_whisker_data(group_district_buckets_list, sorted_districts_2022_list)
    lowest_min = 101
    highest_max = -1
    for district, metrics in group_box_and_whisker_data.items():
        # Update lowest_min if the current MIN is lower
        if metrics['MIN'] < lowest_min:
            lowest_min = metrics['MIN']
        # Update highest_max if the current MAX is higher
        if metrics['MAX'] > highest_max:
            highest_max = metrics['MAX']


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


    legend = {
        "enacted_dots" : "Enacted Plan (2022)"
    }

    yticks = calculate_y_axis_ticks(lowest_min, highest_max, 10)
    labels = {
        "title" : f"{region_type.capitalize()} Population Percent Across Ensemble",
        "subtitle": f"({state_name} - Ensemble {chart_ensemble_id}, {plan_count} Plans)",
        "axis-x": "Bucket Index",
        "axis-y": f"{region_type.capitalize()} Population (%)",
        "axis-x-ticks" : [i for i in range(1, len(group_district_buckets.keys())+1)],
        "axis-y-ticks": yticks,
        "legend": legend
    }

    finalized_racial_whisker_json = {
        "NAME": state_name,
        "ENSEMBLE_ID": ensemble_id,
        "TOT_PLANS": plan_count,
        "REGION_TYPE": region_type.upper(),
        "labels" : labels,
        "data": group_box_and_whisker_data
    }

    finalized_racial_whisker_json_str = json.dumps(finalized_racial_whisker_json, indent=4)
    finalized_path = f"results/ensemble_{ensemble_id}/box_and_whisker_results_{state_abbr}_region_type_{region_type.upper()}.json"
    with open(finalized_path, 'w') as json_file:
        json_file.write(finalized_racial_whisker_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--region_type', type=str, required=True)
    arguments_parser.add_argument('--plan_count', type=int, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr, args.region_type, args.plan_count)