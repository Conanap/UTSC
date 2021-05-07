import unittest
from recursion import *


def mult2(a):
    return 2 * a


def maybe(a):
    return not isinstance(a, str)


class testrecursion(unittest.TestCase):

    def testlength(self):
        self.assertEquals(length([]), 0)
        self.assertEquals(length([1]), 1)
        self.assertEquals(length([1, 2]), 2)

    def testreverse(self):
        self.assertEquals(reverse([]), [])
        self.assertEquals(reverse([1]), [1])
        self.assertEquals(reverse([1, 2]), [2, 1])

    def testis_pal(self):
        self.assertEquals(is_pal([]), True)
        self.assertEquals(is_pal([1]), True)
        self.assertEquals(is_pal([1, 2]), False)
        self.assertEquals(is_pal([1, 2, 1]), True)

    def testnum_el(self):
        self.assertEquals(num_el([]), 0)
        self.assertEquals(num_el([1]), 1)
        self.assertEquals(num_el([1, 2]), 2)
        self.assertEquals(num_el([1, [2, 3], 4]), 4)

    def teststutter(self):
        self.assertEquals(stutter([]), [])
        self.assertEqual(stutter([1]), [1, 1])
        self.assertEquals(stutter([1, 'a']), [1, 1, 'a', 'a'])

    def testmy_filter(self):
        self.assertEquals(my_filter(maybe, [1, 2, 'a']), [1, 2])

    def testmy_map(self):
        self.assertEquals(my_map(mult2, [1, 'a']), [2, 'aa'])

if __name__ == '__main__':
    unittest.main(exit=False)
