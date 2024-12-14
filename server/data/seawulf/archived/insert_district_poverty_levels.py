import pandas as pd
import argparse
import json
import numpy as np
from seawulf_constants import *

def postprocessing_process(ensemble_id, state_abbr, plan_id):
    state_name = None
    if state_abbr == "MD":
        state_name = "maryland"
    elif state_abbr == "SC":
        state_name = "south_carolina"

    precincts_economics_filepath = f"{state_name}_precincts_household_income.json"
    with open(precincts_economics_filepath, 'r') as precincts_income_file:
        precincts_income_json = json.load(precincts_income_file)

    district_precincts_filepath = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_precincts_results.json"
    with open(district_precincts_filepath, 'r') as district_precincts_file:
        district_precincts_json = json.load(district_precincts_file)

    districts_poverty_households_sums = {
        str(i).zfill(2): 0 
        for i in range(1, len(district_precincts_json) + 1)
    }
    districts_total_households_sums = {
        str(i).zfill(2): 0 
        for i in range(1, len(district_precincts_json) + 1)
    }
    for precinct in precincts_income_json:
        precinct_id = precinct["UNIQUE_ID"]
        for district_id in district_precincts_json:
            # Find the district's precinct and get the household data
            if precinct_id in district_precincts_json[district_id]:
                districts_poverty_households_sums[district_id] += precinct["0_35K"]
                districts_total_households_sums[district_id] += precinct["TOT_HOUS22"]
    
    print(districts_poverty_households_sums)
    print(districts_total_households_sums)

    districts_filepath_r = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_economic_results.json"
    with open(districts_filepath_r, 'r') as districts_file_r:
        output_file_json = json.load(districts_file_r)

    for district_id in district_precincts_json:
        percentage_in_poverty = 0
        if districts_poverty_households_sums[district_id] != 0:
            percentage_in_poverty = int((districts_poverty_households_sums[district_id] / districts_total_households_sums[district_id]) * 100)
        output_file_json[district_id]["POVERTY_PERCENT"] = percentage_in_poverty

    finalized_json_str = json.dumps(output_file_json, indent=4)
    districts_filepath_w = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_economic_results.json"
    with open(districts_filepath_w, 'w') as districts_file_w:
        districts_file_w.write(finalized_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--plan_id', type=int, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr, args.plan_id)