#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=24:00:00
#SBATCH --partition=long-40core
#SBATCH --ntasks-per-node=40
#SBATCH --job-name=generate_md_ensemble_plan_2(5000)
#SBATCH --output=generate_md_ensemble_plan_2.log

module load gnu-parallel/6.0

start_time=$(date +%s)
parallel --jobs 40 python gerrychain_mggg.py --state_graph_file precinct_dual_graph_MD_output_for_seawulf.json --ensemble_id 4 --plan_id {} --iterations 10000 ::: {1..5000}
wait

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"