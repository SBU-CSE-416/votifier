#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_sc_box_whisker_economic_merge_ensembles
#SBATCH --output=postlogs/postprocess_sc_box_whisker_economic_merge_ensembles.log

start_time=$(date +%s)
python postprocess_seawulf_whisker_merge_ensembles.py --ensemble_id1 1 --ensemble_id2 2 --state_abbr "SC" --type "economic"

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"