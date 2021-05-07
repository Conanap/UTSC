module Lab9 where

-- mathematical expressions
-- leafs are numbers (integers for now)
-- internal nodes are either unary functions and one child
-- or binary functions and two children
data MathTree = Leaf Int
              | Unary (Int -> Int, MathTree)
              | Binary ((Int, Int) -> Int, MathTree, MathTree)

-- This is here to let Haskell know how to display the MathTree to you.
-- We will learn later what exactly this is and how it works.
instance Show MathTree where
    show (Leaf v) = show v
    show (Unary (f,t)) = "unary(" ++ show t ++ ")"
    show (Binary (f,l,r)) = "binary(" ++ show l ++ "," ++ show r ++ ")"

-- evaluate the math tree
eval :: MathTree -> Int
eval (Leaf v) = v
eval (Unary (f, t)) = f (eval t)
eval (Binary (f, l, r)) = f ((eval l), (eval r))

-- apply (op, t)
-- apply op to every leaf in t
apply :: ((Int -> Int), MathTree) -> MathTree
apply (op, (Leaf v)) = Leaf(op v)
apply (op, (Unary (f, t))) = Unary(f, apply(op, t))
apply (op, (Binary (f, l, r))) = Binary (f, apply(op, l), apply(op, r))


-- search (v, t)
-- return whether v is in tree t
search :: (Int, MathTree) -> Bool
search (s, Leaf v) = s == v
search (s, Unary(f, t)) = search(s, t)
search (s, Binary(f, l, r)) = search(s, l) || search(s, r)

-- replace (v, v', t)
-- replace every value v in the leafs with v'
replace :: (Int, Int, MathTree) -> MathTree
replace (o, n, Leaf v) = if (v == o) then Leaf(n) else Leaf(v)
replace (o, n, Unary(f, t)) = Unary(f, replace(n, o, t))
replace (o, n, Binary(f, l, r)) = Binary(f, replace(o, n, l), replace(o, n, r))
