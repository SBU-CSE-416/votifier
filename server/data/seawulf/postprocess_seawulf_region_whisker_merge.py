import argparse
import json
from seawulf_constants import *

def postprocessing_process(ensemble_id, state_abbr):
    ensemble_filepath = f"results/ensemble_{ensemble_id}"
    PATH_RURAL = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_region_type_RURAL.json"
    PATH_SUBURBAN = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_region_type_SUBURBAN.json"
    PATH_URBAN = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_region_type_URBAN.json"

    with open(PATH_RURAL, 'r') as rural_file:
            rural_json = json.load(rural_file)
    with open(PATH_SUBURBAN, 'r') as suburban_file:
            suburban_json = json.load(suburban_file)
    with open(PATH_URBAN, 'r') as urban_file:
            urban_json = json.load(urban_file)
    combined_json = {
        "NAME" : urban_json['NAME'],
        "ENSEMBLE_ID" : ensemble_id,
        "data": {}
    }
    region_json_files = [rural_json, suburban_json, urban_json]
    for region_json_file in region_json_files:
        combined_json["data"][region_json_file["REGION_TYPE"]] = {
            "labels" : region_json_file["labels"],
            "data" : region_json_file["data"]
        }
    finalized_json_str = json.dumps(combined_json, indent=4)
    output_path = f"{ensemble_filepath}/region_boxplots_{ensemble_id}.json"
    with open(output_path, 'w') as output_file:
            output_file.write(finalized_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr)