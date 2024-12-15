#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_md_box_whisker_region_merge
#SBATCH --output=postlogs/postprocess_md_box_whisker_region_merge.log

start_time=$(date +%s)
python postprocess_seawulf_region_whisker_merge.py --ensemble_id 3 --state_abbr "MD"
python postprocess_seawulf_region_whisker_merge.py --ensemble_id 4 --state_abbr "MD"

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"