module Trees where

-- A funny tree.
data Tree a = Leaf a | Node (a, Tree a, Tree a)

-- countNodes t
-- return the number of nodes in tree t
-- countNodes:: Tree -> Int
-- countNodes Empty = 0
countNodes(Leaf a)  = 1
countNodes(Node (a, l, r)) = 1 + countNodes l + countNodes r

-- forallNodes p t
-- return whether p is true of every node in tree t
forallNodes p (Leaf a) = p a
forallNodes p (Node(a, l, r)) = p a && forallNodes p l && forallNodes p r

-- existsNode p t
-- return whether p is true of some node in tree t
existsNode p (Leaf a) = p a
existsNode p (Node (a, l, r)) = p a || existsNode p l || existsNode p r

-- inorder t
-- return a list of nodes in t in inorder traversal order
inorder (Leaf a) = [a]
inorder (Node(a, l, r)) = inorder l ++ [a] ++ inorder r

tfold f g (Leaf x) = f x
tfold f g (Node (x, left, right)) = 
    g x (tfold f g left) (tfold f g right)

tfold:: (a -> b) -> (a -> b -> b -> b) -> Tree a -> b

countNodes' = tfold (const 1) (\x y z -> 1 + y + z)

forallNodes' p = tfold p (\x y z -> p x && y && z)

existsNode' p = tfold p (\x y z -> p x || y || z)

inorder' = tfold (: []) (\x y z -> y ++ [x] ++ z)
