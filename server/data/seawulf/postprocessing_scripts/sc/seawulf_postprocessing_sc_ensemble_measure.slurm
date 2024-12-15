#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_sc_ensemble_measure
#SBATCH --output=postlogs/postprocess_sc_ensemble_measure.log

start_time=$(date +%s)
python postprocess_seawulf_ensemble_measure.py --ensemble_id 1 --state_abbr "SC" --plan_count 250
python postprocess_seawulf_ensemble_measure.py --ensemble_id 2 --state_abbr "SC" --plan_count 5000

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"