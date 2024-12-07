#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=02:30:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=40
#SBATCH --job-name=generate_sc_ensemble_plan_1(250)
#SBATCH --output=generate_sc_ensemble_plan_1.log

module load gnu-parallel/6.0

start_time=$(date +%s)
parallel --jobs 40 python gerrychain_mggg.py --state_graph_file precinct_dual_graph_SC_output_for_seawulf.json --ensemble_id 1 --plan_id {} --iterations 10000 ::: {1..250}
wait

end_time=$(date +%s)
runtime=$((end_time - start_time))
echo "Execution took $runtime seconds"