import numpy as np
import multiprocessing as mp
import os

def square_me(num):
    result = int(np.square(num))
    pid = os.getpid()
    return (result, pid)

if __name__ == "__main__":

    p = mp.Pool(processes=4)

    for num in range(1,11):
        results = p.apply_async(square_me, [num])
        square, pid = results.get()
        print(f'{pid} | The square of {num} is {square}')
    p.close()