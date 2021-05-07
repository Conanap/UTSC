module E4 where

-- |interleave (xs, ys) 
-- return a list of corresponding elements from xs and ys, 
-- interleaved. The remaining elements of the longer list
-- are ignored.
interleave(xs, ys)
    | not (null xs) && not (null ys) = [head xs, head ys] ++ interleave(tail xs, tail ys)
    | otherwise = []

-- |toPairs (xs, ys) 
-- return a list of pairs of corresponding elements from xs and ys, 
-- interleaved. The remaining elements of the longer list
-- are ignored.
toPairs(xs, ys)
    | not (null xs) && not (null ys) = (head xs, head ys) : toPairs(tail xs, tail ys)
    | otherwise = []

-- |repeat' (f, x, n)
-- return a list [x, f(x), f(f(x)), ..., f^n(x)]
-- requires n >= 0
repeat' (f, x, n)
    | n == 0 = [x]
    | otherwise = x : repeat'(f, f x, n - 1)
--repeat' (f, x, n) = [x] ++ repeat'(f, f(x), n - 1)
--repeat' (f, x, n) = if(n == 0) then [x] else [x] ++ repeat'(f, f(x), n - 1)

-- |numNeg xs 
-- return a number of negative elements in xs
--  Use list comprehension!
numNeg xs = length [x | x <- xs, x < 0]

-- |genSquares (low, high)
-- return a list of squares of even integers between low and high, inclusive.
-- Use list comprehension!
genSquares(low, high) = [x * x | x <- [low..high], even x]

-- |triples n 
-- return a list of triples (x,y,z) all less than or equal to n, such
-- that x^2 + y^2 == z^2, with no duplicate triples or premutations of
-- ealier triples.
-- Use list comprehension!
triples n = [(x, y, z) | x <- [1..n], y <- [x..n], z <- [1..n], x * x + y * y == z * z]
