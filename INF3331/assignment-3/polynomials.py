class Polynomial:
    def __init__(self, coefficients):
        """coefficients should be a list of numbers with 
        the i-th element being the coefficient a_i."""
        self.coeff = coefficients

    def degree(self):
        """Return the index of the highest nonzero coefficient.
        If there is no nonzero coefficient, return -1."""
        for i, v in enumerate(reversed(self.coeff)):
            if v != 0:
                return len(self.coeff) - 1 - i
        return -1

    def coefficients(self):
        """Return the list of coefficients.

        The i-th element of the list should be a_i, meaning that the last
        element of the list is the coefficient of the highest degree term."""
        return self.coeff

    def __call__(self, x):
        """Return the value of the polynomial evaluated at the number x"""
        value = 0
        for i in range(len(self.coeff)):
            value += self.coeff[i] * x ** i
        return value

    def __add__(self, p):
        """Return the polynomial which is the sum of p and this polynomial
        Should assume p is Polynomial([p]) if p is int.

        If p is not an int or Polynomial, should raise ArithmeticError."""

        if type(p) == Polynomial:
            if len(self.coeff) > len(p.coeff):
                sum_of_coeff = self.coeff[:]
                for i in range(len(p.coeff)):
                    sum_of_coeff[i] += p.coeff[i]
            else:
                sum_of_coeff = p.coeff[:]
                for i in range(len(self.coeff)):
                    sum_of_coeff[i] += self.coeff[i]

            return Polynomial(sum_of_coeff)
        elif type(p) == int:
            sum_of_coeff = self.coeff[:]
            sum_of_coeff[0]+=p
            return Polynomial(sum_of_coeff)
        else:
            raise ArithmeticError

    def __sub__(self, p):
        """Return the polynomial which is the difference of p and this polynomial
        Should assume p is Polynomial([p]) if p is int.

        If p is not an int or Polynomial, should raise ArithmeticError."""

        if type(p) == Polynomial:
            if len(self.coeff) >= len(p.coeff):
                sub = self.coeff[:]
                sub_second = p.coeff[:]
                for i in range(len(sub_second)):
                    sub_second[i] *= -1
                for i in range(len(sub_second)):
                    sub[i] += sub_second[i]
            else:
                sub_sec = p.coeff[:]
                sub = [0] * len(p.coeff)
                for i in range(len(sub)):
                 if i < len(self.coeff):
                         sub[i] = self.coeff[i]
                 else:
                         sub[i] = 0
                for i in range(len(sub_sec)):
                    sub_sec[i] *= -1
                for i in range(len(sub_sec)):
                    sub[i] += sub_sec[i]
            return Polynomial(sub)
        elif type(p) == int:
            sub = self.coeff[:]
            sub[0] -= p
            return Polynomial(sub)
        else:
            raise ArithmeticError

    def __mul__(self, c):
        """Return the polynomial which is this polynomial multiplied by given integer.
        Should raise ArithmeticError if c is not an int."""

        if type(c) == int:
            mul_of_coeff = self.coeff[:]
            for i in range(len(mul_of_coeff)):
                mul_of_coeff[i] = mul_of_coeff[i] * c
        else:
            raise ArithmeticError
        return Polynomial(mul_of_coeff)

    def __rmul__(self, c):
        """Return the polynomial which is this polynomial multiplied by some integer"""
        return self.__mul__(c)


    def __repr__(self):
        """Return a nice string representation of polynomial.

        E.g.: x^6 - 5x^3 + 2x^2 + x - 1
        """
        output = ''
        for i in reversed(range(0, len(self.coeff))):
            if self.coeff[i] != 0:

                output += ' + %g*x^%d' % (self.coeff[i], i)

        output = output.replace('+ -', '- ')
        output = output.replace('x^0', '1')
        output = output.replace(' 1*', ' ')
        output = output.replace('*1', '')
        output = output.replace('x^1 ', 'x ')
        if output[0:3] == ' + ':
            output = output[3:]
        if output[0:3] == ' - ':
            output = '-' + output[3:]
        return ''.join(output)

    def __eq__(self, p):
        """Check if two polynomials have the same coefficients."""
        return self.coefficients() == p.coefficients()


def sample_usage():
    p = Polynomial([1, 2, 1])  # 1 + 2x + x^2
    q = Polynomial([9, 5, 0, 6])  # 9 + 5x + 6x^3

    print("The value of {} at {} is {}".format(p, 7, p(7)))

    print("The coefficients of {} are {}".format(p, p.coefficients()))

    print("\nAdding {} and {} yields {}".format(p, q, p + q))

    p, q, r = map(Polynomial,
                  [
                      [1, 0, 1], [0, 2, 0], [1, 2, 1]
                  ]
                  )

    print("\nWill adding {} and {} be the same as {}? Answer: {}".format(
        p, q, r, p + q == r
    ))
    print(":" + str(p - q))
    print("\nIs {} - {} the same as {}? Answer: {}".format(
        p, q, r, p - q == r
    ))

    print(p == p)
    print(p * 3)
    print(3 * p)

sample_usage()
