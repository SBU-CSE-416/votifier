import argparse
import json
from seawulf_constants import *

def postprocessing_process(ensemble_id, state_abbr):
    ensemble_filepath = f"results/ensemble_{ensemble_id}"
    
    PATH_0_35K = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_economic_group_0_35K.json"
    PATH_35K_60K = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_economic_group_35K_60K.json"
    PATH_60K_100K = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_economic_group_60K_100K.json"
    PATH_100K_125K = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_economic_group_100K_125K.json"
    PATH_125K_150K = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_economic_group_125K_150K.json"
    PATH_150K_MORE = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_economic_group_150K_MORE.json"

    with open(PATH_0_35K, 'r') as eco1_file:
        json_0_35K = json.load(eco1_file)
    with open(PATH_35K_60K, 'r') as eco2_file:
        json_35K_60K = json.load(eco2_file)
    with open(PATH_60K_100K, 'r') as eco3_file:
        json_60K_100K = json.load(eco3_file)
    with open(PATH_100K_125K, 'r') as eco4_file:
        json_100K_125K = json.load(eco4_file)
    with open(PATH_125K_150K, 'r') as eco5_file:
        json_125K_150K = json.load(eco5_file)
    with open(PATH_150K_MORE, 'r') as eco6_file:
        json_150K_MORE = json.load(eco6_file)

    combined_json = {
        "NAME" : json_0_35K['NAME'],
        "ENSEMBLE_ID" : ensemble_id,
        "data": {}
    }
    economic_json_files = [json_0_35K, json_35K_60K, json_60K_100K, json_100K_125K, json_125K_150K, json_150K_MORE]
    for economic_json_file in economic_json_files:
        combined_json["data"][economic_json_file["ECONOMIC_GROUP"]] = {
            "labels" : economic_json_file["labels"],
            "data" : economic_json_file["data"]
        }
    finalized_json_str = json.dumps(combined_json, indent=4)
    output_path = f"{ensemble_filepath}/economic_boxplots_{ensemble_id}.json"
    with open(output_path, 'w') as output_file:
            output_file.write(finalized_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr)