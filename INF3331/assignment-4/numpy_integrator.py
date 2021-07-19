#!/usr/bin/env python
import numpy as np


def numpy_integrate(fn, a, b, n):
    if n <= 0:
        raise ValueError
    h = float(b - a) / n
    fn = np.vectorize(fn)
    x = np.linspace(0, n, n)
    return np.sum(fn(a + x * h) * h)


def midpoint_numpy_integrate(fn, a, b, n):
    if n <= 0:
        raise ValueError
    h = float(b - a) / n
    fn = np.vectorize(fn)
    x = np.linspace(0, n, n)
    return np.sum(fn(a + h/2 + x*h) * h)


def f(x):
    return x ** 2
