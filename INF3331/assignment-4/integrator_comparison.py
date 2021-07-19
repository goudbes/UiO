#!/usr/bin/env python
from math import sin, pi

from assignment4.integrator import integrate, midpoint_integrate
from assignment4.numba_integrator import numba_integrate, midpoint_numba_integrate
from assignment4.numpy_integrator import numpy_integrate, midpoint_numpy_integrate


def comparison(func, nstart=5000000):
    n = nstart
    p = 0
    i = 1
    while True:
        old = n
        x = func(sin, 0, pi, n)
        if abs(2-x) < 1E-10:
            n -= abs(n-p)//2
        else:
            n += abs(n-p)//2
        if n == old:
            print("{} found {} for N {} in {} steps".format(func.__name__, x, n, i))
            break
        if n > nstart:
            print("{} did not find N. Error at N {} is {}".format(func.__name__, old, abs(2-x)))
            break
        p = old
        i += 1


if __name__ == '__main__':
    comparison(integrate)
    comparison(midpoint_integrate)
    comparison(numpy_integrate)
    comparison(midpoint_numpy_integrate)
    comparison(numba_integrate)
    comparison(midpoint_numba_integrate)
