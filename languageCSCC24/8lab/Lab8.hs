module Lab8 where

-- Translate the following Scheme expressions into Haskell.

-- (define life 42)
life = 42

-- (define life' (+ 42 24))
life' = 42 + 24
life  :: Integer
life' :: Integer
-- assert the types of life and life' 
-- :type life
-- s:type life'

-- list: [, , ,]
-- tuple: (, )
--load module: :l modulename

-- (define (implies a b) (if a b True))
-- assert the type of implies
implies (a, b) = if a
                then b
                else True
implies :: (Bool, Bool) -> Bool

-- (define (greet x) (string-append "hello, " x))
-- assert the type of greet
greet x = "hello, " ++ x
greet :: [Char] -> [Char]

-- define a function stutter that repeats each element in the given list twice
-- assert the type of stutter
stutter lst = if null lst then [] else head lst : head lst : stutter (tail lst)
-- type stutter
-- for stutter only take string:
-- stutter :: [char] -> [char]

-- lambda function:
-- (\ x -> x + 1) 5
-- 6
-- myfunc = (\ (x,y) -> x+ y)
-- myfunc ( 5, 6)
-- 11
-- let test (x) = x + 1
-- test(5)
-- 6

-- : means cons, [] = empty lst
-- can apply tail and rest to str. tail means rest

-- now let's use pattern matching to produce a much better solution
-- call it stutter'
-- assert the type of stutter'
stutter' [] = []
stutter' (h : t) = h : h : (stutter' t)
stutter' :: [t] -> [t]

-- define len (length of list) using pattern matching
-- assert the type of len
len [] = 0
len(h : t) = 1 + len t

-- define firsts that takes a list of pairs and returns a list of its first elements
-- use pattern mathing
-- assert the type of firsts
firsts [] = []
firsts ((f, s) : t) = f : firsts t
