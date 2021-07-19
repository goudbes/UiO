#!/usr/bin/env python
import matplotlib.pyplot as plot
import seaborn
import time

from assignment4.numba_integrator import numba_integrate
from assignment4.numpy_integrator import numpy_integrate


def integrate(fn, a, b, n):
    if n <= 0:
        raise ValueError
    h = float(b - a) / n
    s = 0
    for i in range(n):
        s += fn(a + i * h) * h
    return s


def midpoint_integrate(fn, a, b, n):
    if n <= 0:
        raise ValueError
    h = float(b - a) / n
    s = 0
    for i in range(n):
        s += h*fn(a + h/2 + i*h)
    return s


def f(x):
    return x ** 2


def plottus(x, y):
    plot.yscale("log")
    plot.title("Error as a function of N")
    plot.xlabel("N")
    plot.ylabel("Error")
    plot.plot(x, y, "b-o")
    plot.show()


if __name__ == '__main__':
    pn = []
    pe = []
    for n in [10, 100, 1000, 10000, 100000, 500000, 1000000]:
        pn.append(n)
        k = integrate(f, 0, 1, n)
        pe.append(1/3 - k)
        print("N {} K {} E {}".format(n, k, pe[len(pe) - 1]))

    plottus(pn, pe)

    start = time.time()
    k = integrate(f, 0, 1, 100000000)
    elapsed = (time.time() - start)
    print("Result {} time took {}".format(k, elapsed))
    start = time.time()
    k = numpy_integrate(f, 0, 1, 100000000)
    elapsed = (time.time() - start)
    print("Result {} time took with numpy {}".format(k,  elapsed))
    start = time.time()
    k = numba_integrate(f, 0, 1, 100000000)
    elapsed = (time.time() - start)
    print("Result {} time took with numba {}".format(k, elapsed))
