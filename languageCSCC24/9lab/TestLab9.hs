module TestLab9 where

import Lab9
import Test.HUnit

{- Starter -- Test Lab 7 -}

t = Binary(\ (x, y) -> x + y,
           Binary(\ (x, y) -> x + y,
                  Unary(abs,
                        Unary (\ x -> -x,
                               Leaf 3)),
                  Leaf 2),
           Binary(\ (x, y) -> x * y,
                  Binary(\ (x, y) -> x + y,
                         Unary(\ x -> -x,
                               Leaf 1),
                         Leaf 4),
                  Leaf 7))

testApply = TestCase $ assertEqual
            ("eval (apply (x -> 2 * x, t) is ")
            94
            (eval (apply (\ x -> 2 * x, t)))

testSearch = TestCase $ assertEqual
              ("search (2, t) is ") True
              (search (2, t))

testReplace = TestCase $ assertEqual
              ("eval(replace (2, 42, t) is ") 66
              (eval (replace (2, 42, t)))

tests = TestList [TestLabel "apply" testApply,
                  TestLabel "search" testSearch,
                  TestLabel "replace" testReplace
                 ]

main = runTestTT tests
