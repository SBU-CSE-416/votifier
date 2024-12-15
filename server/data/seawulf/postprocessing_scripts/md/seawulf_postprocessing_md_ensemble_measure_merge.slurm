#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_md_ensemble_measure_merge
#SBATCH --output=postlogs/postprocess_md_ensemble_measure_merge.log

start_time=$(date +%s)
python postprocess_seawulf_whisker_merge_ensembles.py --ensemble_id1 3 --ensemble_id2 4 --state_abbr "MD" --type "splits"

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"