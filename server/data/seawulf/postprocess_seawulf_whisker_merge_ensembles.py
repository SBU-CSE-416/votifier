import argparse
import json

def postprocessing_process(ensemble_id1, ensemble_id2, state_abbr, type):
    ensemble1_filepath = None
    ensemble2_filepath = None
    if type == "splits":
        ensemble1_filepath = f"results/ensemble_{ensemble_id1}/vote_splits_{ensemble_id1}.json"
        ensemble2_filepath = f"results/ensemble_{ensemble_id2}/vote_splits_{ensemble_id2}.json"
    else:
        ensemble1_filepath = f"results/ensemble_{ensemble_id1}/{type}_boxplots_{ensemble_id1}.json"
        ensemble2_filepath = f"results/ensemble_{ensemble_id2}/{type}_boxplots_{ensemble_id2}.json"
    with open(ensemble1_filepath, 'r') as ensemble1_file:
        ensemble1_json = json.load(ensemble1_file)
    with open(ensemble2_filepath, 'r') as ensemble2_file:
        ensemble2_json = json.load(ensemble2_file)
    combined_json = None
    if type == "splits":
        combined_json = {
            "NAME" : ensemble1_json['NAME'],
            "data": {
                "ensemble_1" : {},
                "ensemble_2" : {}
            }  
        }
        combined_json["data"]["ensemble_1"]["labels"] = ensemble1_json["labels"]
        combined_json["data"]["ensemble_1"]["data"] = ensemble1_json["data"]
        combined_json["data"]["ensemble_2"]["labels"] = ensemble2_json["labels"]
        combined_json["data"]["ensemble_2"]["data"] = ensemble2_json["data"]
    else:
        combined_json = {
            "NAME" : ensemble1_json['NAME'],
            "data": {}
        }
        combined_json["data"]["ensemble_1"] = ensemble1_json["data"]
        combined_json["data"]["ensemble_2"] = ensemble2_json["data"]
    finalized_json_str = json.dumps(combined_json, indent=4)
    output_path = f"results/{type}_boxplots_{state_abbr}.json"
    with open(output_path, 'w') as output_file:
            output_file.write(finalized_json_str)
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--ensemble_id1', type=int, required=True)
    arguments_parser.add_argument('--ensemble_id2', type=int, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--type', type=str, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.ensemble_id1, args.ensemble_id2, args.state_abbr, args.type)