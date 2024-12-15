import pandas as pd
import argparse
import json
import numpy as np
from seawulf_constants import *

def postprocessing_process(ensemble_id, state_abbr):
    ensemble_filepath = f"results/ensemble_{ensemble_id}"
    PATH_WHITE = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_racial_group_WHITE.json"
    PATH_BLACK = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_racial_group_BLACK.json"
    PATH_ASIAN = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_racial_group_ASIAN.json"
    PATH_HISPANIC = f"{ensemble_filepath}/box_and_whisker_results_{state_abbr}_racial_group_HISPANIC.json"
    race_json_files = []
    if state_abbr == "SC":
        with open(PATH_WHITE, 'r') as white_file:
            white_json = json.load(white_file)
        with open(PATH_BLACK, 'r') as black_file:
            black_json = json.load(black_file)
        race_json_files.append(white_json)
        race_json_files.append(black_json)
    elif state_abbr == "MD":
        with open(PATH_WHITE, 'r') as white_file:
            white_json = json.load(white_file)
        with open(PATH_BLACK, 'r') as black_file:
            black_json = json.load(black_file)
        with open(PATH_ASIAN, 'r') as asian_file:
            asian_json = json.load(asian_file)
        with open(PATH_HISPANIC, 'r') as hispanic_file:
            hispanic_json = json.load(hispanic_file)
        race_json_files.append(white_json)
        race_json_files.append(black_json)  
        race_json_files.append(asian_json)
        race_json_files.append(hispanic_json) 
    combined_json = {
        "NAME" : race_json_files[0]['NAME'],
        "ENSEMBLE_ID" : ensemble_id,
        "data": {}
    }
    for race_json_file in race_json_files:
        combined_json["data"][race_json_file["RACIAL_GROUP"]] = {
            "labels" : race_json_file["labels"],
            "data" : race_json_file["data"]
        }
    finalized_json_str = json.dumps(combined_json, indent=4)
    output_path = f"{ensemble_filepath}/racial_boxplots_{ensemble_id}.json"
    with open(output_path, 'w') as output_file:
            output_file.write(finalized_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id, args.state_abbr)