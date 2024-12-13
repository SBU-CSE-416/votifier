import pandas as pd
import argparse
import json
import numpy as np
from seawulf_constants import *

def postprocessing_process(ensemble_id, state_abbr, racial_group, plan_count):
    # group_district_buckets = get_district_buckets(ensemble_id, racial_group, plan_count)
    # group_district_buckets_list = list(group_district_buckets.items())


    # congressional_districts_2022_filepath = f"{state_abbr.lower()}_congressional_districts_summary_seawulf.json"
    # with open(congressional_districts_2022_filepath, 'r') as congressional_districts_2022_file:
    #     districts_2022_json = json.load(congressional_districts_2022_file)
    #     racial_group_key = racial_group.upper() + "_PERCENT"
    #     result = {
    #         str(item['CONG_DIST']).zfill(2): item[racial_group_key]
    #         for item in districts_2022_json['data']
    #     }
    #     sorted_districts_2022_json = dict(sorted(result.items(), key=lambda x: x[1]))
    #     sorted_districts_2022_list = list(sorted_districts_2022_json.items())
    # group_box_and_whisker_data = get_box_and_whisker_data(group_district_buckets_list, racial_group, sorted_districts_2022_list)
    # lowest_min = 101
    # highest_max = -1
    # for district, metrics in group_box_and_whisker_data.items():
    #     # Update lowest_min if the current MIN is lower
    #     if metrics['MIN'] < lowest_min:
    #         lowest_min = metrics['MIN']
    #     # Update highest_max if the current MAX is higher
    #     if metrics['MAX'] > highest_max:
    #         highest_max = metrics['MAX']


    # chart_ensemble_id = ensemble_id
    # if ensemble_id == 3:
    #     chart_ensemble_id = 1
    # elif ensemble_id == 4:
    #     chart_ensemble_id = 2

    # state_name = None

    # if state_abbr == "MD":
    #     state_name = "Maryland"
    # elif state_abbr == "SC":
    #     state_name = "South Carolina"


    # legend = {
    #     "enacted_dots" : "Enacted Plan (2022)"
    # }

    # yticks = calculate_y_axis_ticks(lowest_min, highest_max)
    # labels = {
    #     "title" : f"{racial_group.capitalize()} Population Percent Across Ensemble",
    #     "subtitle": f"({state_name} - Ensemble {chart_ensemble_id}, {plan_count} Plans)",
    #     "axis-x": "Bucket Index",
    #     "axis-y": f"{racial_group.capitalize()} Population Percent",
    #     "axis-x-ticks" : [i for i in range(1, len(group_district_buckets.keys())+1)],
    #     "axis-y-ticks": yticks,
    #     "legend": legend
    # }

    # finalized_racial_whisker_json = {
    #     "NAME": state_name,
    #     "ENSEMBLE_ID": ensemble_id,
    #     "TOT_PLANS": plan_count,
    #     "RACIAL_GROUP": racial_group.upper(),
    #     "labels" : labels,
    #     "data": group_box_and_whisker_data
    # }

    # finalized_racial_whisker_json_str = json.dumps(finalized_racial_whisker_json, indent=4)
    # finalized_path = f"results/ensemble_{ensemble_id}/box_and_whisker_results_{state_abbr}_racial_group_{racial_group.upper()}.json"
    # with open(finalized_path, 'w') as json_file:
    #     json_file.write(finalized_racial_whisker_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--racial_group', type=str, required=True)
    arguments_parser.add_argument('--plan_count', type=int, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr, args.racial_group, args.plan_count)