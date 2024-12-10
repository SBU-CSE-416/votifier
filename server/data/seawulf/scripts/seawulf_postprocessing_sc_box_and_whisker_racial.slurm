#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_sc_box_whisker_racial
#SBATCH --output=postlogs/postprocess_sc_box_whisker_racial.log

start_time=$(date +%s)
python postprocess_seawulf_racial_whisker.py --ensemble_id 1 --state_abbr "SC" --racial_group "WHITE" --plan_count 250
python postprocess_seawulf_racial_whisker.py --ensemble_id 1 --state_abbr "SC" --racial_group "BLACK" --plan_count 250
python postprocess_seawulf_racial_whisker.py --ensemble_id 1 --state_abbr "SC" --racial_group "ASIAN" --plan_count 250
python postprocess_seawulf_racial_whisker.py --ensemble_id 1 --state_abbr "SC" --racial_group "HISPANIC" --plan_count 250

python postprocess_seawulf_racial_whisker.py --ensemble_id 2 --state_abbr "SC" --racial_group "WHITE" --plan_count 5000
python postprocess_seawulf_racial_whisker.py --ensemble_id 2 --state_abbr "SC" --racial_group "BLACK" --plan_count 5000
python postprocess_seawulf_racial_whisker.py --ensemble_id 2 --state_abbr "SC" --racial_group "ASIAN" --plan_count 5000
python postprocess_seawulf_racial_whisker.py --ensemble_id 2 --state_abbr "SC" --racial_group "HISPANIC" --plan_count 5000

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"