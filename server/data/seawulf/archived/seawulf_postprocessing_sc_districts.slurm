#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=4:00:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=40
#SBATCH --job-name=create_new_districts_sc(250)
#SBATCH --output=postlogs/create_new_districts_sc(250).log

module load gnu-parallel/6.0

start_time=$(date +%s)
parallel --jobs 40 python postprocess_seawulf_district_boundaries.py --state_precincts_geojson_file ../states/south_carolina/geodata/south_carolina_precincts.geojson --ensemble_id 1 --plan_id {} ::: {1..250}
wait

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"