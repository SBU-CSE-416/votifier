#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_sc_box_whisker_economic_merge
#SBATCH --output=postlogs/postprocess_sc_box_whisker_economic_merge.log

start_time=$(date +%s)
python postprocess_seawulf_economic_whisker_merge.py --ensemble_id 1 --state_abbr "SC"
python postprocess_seawulf_economic_whisker_merge.py --ensemble_id 2 --state_abbr "SC"

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"