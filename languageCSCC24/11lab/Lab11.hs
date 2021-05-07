module Lab11 where

-- recall our datatype for binary trees
data BTree a = Empty | Node a (BTree a) (BTree a)

-- We want to be able to compare BTrees for equality. We say that two
-- BTrees are equal if they contain the same elements. The shapes of
-- the trees do not determine whether the trees are equal.

-- want t == t' (see TestLab11 for definitions of t and t')

-- we now want to be able to display BTrees as follows (tilt your head to
-- the left!)
{-
    20
  15
10
    7
      6
  5
    2
-}
travel Empty = []
travel (Node a l r) = [a] ++ travel l ++ travel r

testeq [] [] = True
testeq [] _ = False
testeq _ [] = False
testeq a b
    | head a `elem` b = testeq (tail a) [ x | x <- b, x /= head a ]
    | otherwise = False

instance Eq a => Eq (BTree a) where
  Empty == Empty = True
  Empty == _ = False
  _ == Empty = False
  x == y =
    testeq (travel x) (travel y)

instance Show a => Show (BTree a) where
  show Empty = ""
  show (Node a Empty Empty) = show a
  show (Node a Empty r) = "  " ++ show r ++ "\n  " ++ show a
  show (Node a l Empty) = show a ++ "\n  " ++ show l
  show (Node a l r) = "  " ++ show r ++ "\n" ++ show a ++ "\n  " ++ show l

-- Let's create a type class Sized, which prescribes a function 
-- size :: a -> Int, and provides definitions for 
-- empty :: a -> Bool and nonempty :: a -> Bool

class Sized a where
  size :: a -> Int
  empty :: a -> Bool
  empty a = size a == 0

-- now let's state that Bool's are of size 1, by defining a
-- corresponding instance
instance Sized Bool where
  size a = 1

-- now let's make lists Sized, by defining the size of a list to be
-- its length
instance Sized [a] where
  size = length

-- finally, let's make our BTree's Sized, by defining the size of a
-- BTree to be the number of the elements stored in it
instance Sized (BTree a) where
  size a = length (travel a)
