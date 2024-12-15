#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_sc_box_whisker_economic
#SBATCH --output=postlogs/postprocess_sc_box_whisker_economic.log

start_time=$(date +%s)
python postprocess_seawulf_economic_whisker.py --ensemble_id 1 --state_abbr "SC" --eco_group "0_35K" --plan_count 250
python postprocess_seawulf_economic_whisker.py --ensemble_id 1 --state_abbr "SC" --eco_group "35K_60K" --plan_count 250
python postprocess_seawulf_economic_whisker.py --ensemble_id 1 --state_abbr "SC" --eco_group "60K_100K" --plan_count 250
python postprocess_seawulf_economic_whisker.py --ensemble_id 1 --state_abbr "SC" --eco_group "100K_125K" --plan_count 250
python postprocess_seawulf_economic_whisker.py --ensemble_id 1 --state_abbr "SC" --eco_group "125K_150K" --plan_count 250
python postprocess_seawulf_economic_whisker.py --ensemble_id 1 --state_abbr "SC" --eco_group "150K_MORE" --plan_count 250

python postprocess_seawulf_economic_whisker.py --ensemble_id 2 --state_abbr "SC" --eco_group "0_35K" --plan_count 5000
python postprocess_seawulf_economic_whisker.py --ensemble_id 2 --state_abbr "SC" --eco_group "35K_60K" --plan_count 5000
python postprocess_seawulf_economic_whisker.py --ensemble_id 2 --state_abbr "SC" --eco_group "60K_100K" --plan_count 5000
python postprocess_seawulf_economic_whisker.py --ensemble_id 2 --state_abbr "SC" --eco_group "100K_125K" --plan_count 5000
python postprocess_seawulf_economic_whisker.py --ensemble_id 2 --state_abbr "SC" --eco_group "125K_150K" --plan_count 5000
python postprocess_seawulf_economic_whisker.py --ensemble_id 2 --state_abbr "SC" --eco_group "150K_MORE" --plan_count 5000

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"