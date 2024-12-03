#!/usr/bin/env bash

#SBATCH --nodes=1
#SBATCH --time=05:00
#SBATCH --partition=short-40core
#SBATCH --job-name=parallel_job_test_rst
#SBATCH --output=squared_numbers.txt

module load anaconda/3
module load gnu-parallel/6.0

parallel --jobs 5 python number_square.py {} ::: {1..50}