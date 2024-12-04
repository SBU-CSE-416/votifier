#!/usr/bin/env bash

#SBATCH --nodes=1
#SBATCH --time=05:00
#SBATCH --partition=short-40core
#SBATCH --ntasks-per-node=10
#SBATCH --job-name=parallel_job_test_rst
#SBATCH --output=test_script_output/log.txt

module load anaconda/3
module load gnu-parallel/6.0

parallel --jobs 10 --joblog test_script_output/joblog.txt python number_square.py --plan_id {} '>' test_script_output/squared_number_{}.txt ::: {1..10}