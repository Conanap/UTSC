module Final where
condApply p f [] = []
condApply p f (x:xs)
  | p x = [f x] ++ condApply p f xs
  | otherwise = [x] ++ condApply p f xs

condApply2 p f = foldr (\x y -> if p x then [f x] ++ y else [x] ++ y) []

condAllApply ps f = foldr (\x y -> if all x then [f x] ++ y else [x] ++ y) []
  where all v = foldr (\x y -> x v && y) True ps


data BTree a = Empty | BTree a (BTree a) (BTree a) deriving Show

numNodes Empty = 0
numNodes (BTree a l r) = 1 + numNodes l + numNodes r

inorder :: BTree a -> [a]
inorder Empty = []
inorder (BTree a l r) = inorder l ++ [a] ++ inorder r

sorted [] = True
sorted [x] = True
sorted (x:xs) = x < xs!!0 && sorted xs

isBST Empty = True
isBST (BTree a Empty Empty) = True
isBST (BTree a (BTree b l1 r1) Empty) = a > b && isBST (BTree b l1 r1)
isBST (BTree a Empty (BTree b l1 r1)) = a < b && isBST (BTree b l1 r1)
isBST (BTree a (BTree b l1 r1) (BTree c l2 r2)) = isBST (BTree b l1 r1) && a > b && a < c && isBST (BTree c l2 r2)

check [] [] = True
check [] _ = False
check _ [] = False
check (a:as) (b:bs) = a == b && check as bs

(===) :: Eq a => BTree a -> BTree a -> Bool
a === b = check (inorder a) (inorder b)

