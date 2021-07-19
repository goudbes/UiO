#!/usr/bin/env python
import unittest

from assignment4.integrator import integrate, midpoint_integrate
from assignment4.numba_integrator import numba_integrate
from assignment4.numpy_integrator import numpy_integrate


class TestIntegrator(unittest.TestCase):
    def integral_of_constant_function(self, fn):
        self.assertRaises(ValueError, fn, lambda x: 2, 0, 1, 0)
        self.assertAlmostEqual(fn(lambda x: 2, 0, 1, 1), 2)
        self.assertAlmostEqual(fn(lambda x: 2, 0, 1, 14), 2)
        self.assertAlmostEqual(fn(lambda x: 2, 0, 1, 10), 2)
        self.assertAlmostEqual(fn(lambda x: 2, 0, 1, 100), 2)

    def test_numpy_integral_of_constant_function(self):
        self.integral_of_constant_function(numpy_integrate)

    def test_integral_of_constant_function(self):
        self.integral_of_constant_function(integrate)

    def test_midpoint_integrate_of_constant_function(self):
        self.integral_of_constant_function(midpoint_integrate)

    def test_numba_integrate_of_constant_function(self):
        self.integral_of_constant_function(numba_integrate)

    def integrator_of_linear_function(self, fn):
        self.assertAlmostEqual(fn(lambda x: 2*x, 0, 1, 10), 1, places=None, delta=1/10)
        self.assertAlmostEqual(fn(lambda x: 2 * x, 0, 1, 20), 1, places=None, delta=1 / 20)
        self.assertAlmostEqual(fn(lambda x: 2 * x, 0, 1, 100), 1, places=None, delta=1 / 99)
        self.assertAlmostEqual(fn(lambda x: 2 * x, 0, 1, 1), 1, places=None, delta=1 / 1)

    def test_numpy_integrator_of_linear_function(self):
        self.integrator_of_linear_function(numpy_integrate)

    def test_integrator_of_linear_function(self):
        self.integrator_of_linear_function(integrate)

    def test_numba_integrator_of_linear_function(self):
        self.integrator_of_linear_function(numba_integrate)

    def test_midpoint_integrate_of_linear_function(self):
        self.integrator_of_linear_function(midpoint_integrate)

