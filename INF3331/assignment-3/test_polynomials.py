#!/usr/bin/env python
from polynomials import Polynomial
import unittest


class TestPolynomials(unittest.TestCase):
    def test_degree(self):
        self.assertEqual(Polynomial([]).degree(), -1)
        self.assertEqual(Polynomial([0]).degree(), -1)
        self.assertEqual(Polynomial([0, 0, 0, 0]).degree(), -1)
        self.assertEqual(Polynomial([1]).degree(), 0)
        self.assertEqual(Polynomial([4, 1]).degree(), 1)
        self.assertEqual(Polynomial([4, 1, 0]).degree(), 1)
        self.assertEqual(Polynomial([4, 1, 0, 1]).degree(), 3)
        self.assertEqual(Polynomial([1, 0, 1, 0]).degree(), 2)

    def test_addition(self):
        self.assertEqual(Polynomial([1, 2, 3]) + Polynomial([2, 6]), Polynomial([3, 8, 3]))
        self.assertEqual(Polynomial([2, 4]) + Polynomial([1, 1]), Polynomial([3, 5]))
        self.assertEqual(Polynomial([0, 0, 0]) + Polynomial([0]), Polynomial([0, 0, 0]))
        self.assertEqual(Polynomial([4, 8, 5]) + Polynomial([1, 8]), Polynomial([5, 16, 5]))
        self.assertEqual(Polynomial([3, 4]) + Polynomial([2]), Polynomial([5, 4]))
        self.assertEqual(Polynomial([3, 4]) + 5, Polynomial([8, 4]))

    def test_evaluation(self):
        self.assertEqual(Polynomial([4, -4, 5])(3), 37)
        self.assertEqual(Polynomial([4, -4, 5])(-4), 100)
        self.assertEqual(Polynomial([4, -4, 5])(0), 4)
        self.assertEqual(Polynomial([5, 1, 9])(2), 43)
        self.assertEqual(Polynomial([5, -1, 9])(-1), 15)
        self.assertEqual(Polynomial([1, -4, -9])(2), -43)
        self.assertEqual(Polynomial([1])(2), 1)
        self.assertEqual(Polynomial([1, 0])(2), 1)
        self.assertEqual(Polynomial([0, 1])(2), 2)

    def test_substraction(self):
        self.assertEqual(Polynomial([1, 0, 1]) - Polynomial([0, 2, 0]), Polynomial([1, -2, 1]))
        self.assertEqual(Polynomial([1, 0, 1]) - Polynomial([5, 2]), Polynomial([-4, -2, 1]))
        self.assertEqual(Polynomial([-4, 5, 3, 1]) - Polynomial([6, -5, -8, 3]), Polynomial([-10, 10, 11, -2]))
        self.assertEqual(Polynomial([1, 3, -1]) - Polynomial([3, 0, 1]), Polynomial([-2, 3, -2]))
        self.assertEqual(Polynomial([1, 3, -1]) - Polynomial([3, 1]), Polynomial([-2, 2, -1]))
        self.assertEqual(Polynomial([-1]) - Polynomial([3, 1]), Polynomial([-4, -1]))
        self.assertEqual(Polynomial([-10, 3, 1, 3]) - Polynomial([1, -6, 2, 3, 4]), Polynomial([-11, 9, -1, 0, -4]))
        self.assertEqual(Polynomial([-10, 3]) - Polynomial([1, -6, 2, 3, 4]), Polynomial([-11, 9, -2, -3, -4]))
        self.assertEqual(Polynomial([1, 0, 0, 0]) - Polynomial([0, 0, 1]), Polynomial([1, 0, -1, 0]))
        self.assertEqual(Polynomial([1, 0, 0, 0]) - Polynomial([1, 0, 0, 0]), Polynomial([0, 0, 0, 0]))
        self.assertEqual(Polynomial([0, 8, -2, 6]) - Polynomial([10, -11, 0, 4]), Polynomial([-10, 19, -2, 2]))

    def test_repr(self):
        self.assertEqual(Polynomial([1, 2, 1]).__repr__(),"x^2 + 2*x + 1")
        self.assertEqual(Polynomial([-1, 1, 2, -5, 0, 0, 1]).__repr__(),"x^6 - 5*x^3 + 2*x^2 + x - 1")
        self.assertEqual(Polynomial([2, 0, 3]).__repr__(), "3*x^2 + 2")

    def test_multiplication(self):
        self.assertEqual(Polynomial([1, 0, 1]) * 4, Polynomial([4, 0, 4]))
        self.assertEqual(Polynomial([1, 0, 1]) * 2, Polynomial([2, 0, 2]))
        self.assertEqual(Polynomial([-4, 5, 3, 1]) * 1, Polynomial([-4, 5, 3, 1]))
        self.assertEqual(Polynomial([1, 3, -1]) * 0 , Polynomial([0, 0, 0]))
        self.assertEqual(Polynomial([1, 3, -1]) * -1, Polynomial([-1, -3, 1]))
        self.assertEqual(Polynomial([-1]) * 22, Polynomial([-22]))
