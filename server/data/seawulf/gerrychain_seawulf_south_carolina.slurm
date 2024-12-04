#!/bin/bash

#SBATCH --nodes=1
#SBATCH --time=00:45:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=20
#SBATCH --job-name=generate_sc_ensemble_plans(250)
#SBATCH --output=test_script_output/generate_sc_ensemble_plans.log

module load parallel

# conda create --name test_env python=3.9 gerrychain=0.3.1 -y
# conda activate test_env

parallel --jobs 20 python gerrychain_mggg.py --state_graph_file precinct_dual_graph_SC_output.json --plan_id {} --iterations 10000 ::: {1..250}