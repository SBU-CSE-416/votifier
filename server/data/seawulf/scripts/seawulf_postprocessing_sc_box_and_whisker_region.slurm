#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=1
#SBATCH --job-name=postprocess_sc_box_whisker_region
#SBATCH --output=postlogs/postprocess_sc_box_whisker_region.log

start_time=$(date +%s)
python postprocess_seawulf_region_whisker.py --ensemble_id 1 --state_abbr "SC" --region_type "RURAL" --plan_count 250
python postprocess_seawulf_region_whisker.py --ensemble_id 1 --state_abbr "SC" --region_type "SUBURBAN" --plan_count 250
python postprocess_seawulf_region_whisker.py --ensemble_id 1 --state_abbr "SC" --region_type "URBAN" --plan_count 250

python postprocess_seawulf_region_whisker.py --ensemble_id 2 --state_abbr "SC" --region_type "RURAL" --plan_count 5000
python postprocess_seawulf_region_whisker.py --ensemble_id 2 --state_abbr "SC" --region_type "SUBURBAN" --plan_count 5000
python postprocess_seawulf_region_whisker.py --ensemble_id 2 --state_abbr "SC" --region_type "URBAN" --plan_count 5000

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"