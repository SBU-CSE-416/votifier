import numpy as np
import argparse

def square_me(num):
    result = int(np.square(int(num)))
    return result

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--plan_id', type=str, required=True)
    args = arguments_parser.parse_args()
    print(f'The square of {args.plan_id} is {square_me(args.plan_id)}')