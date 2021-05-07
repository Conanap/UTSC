module TestLab11 where

import Lab11
import Test.HUnit

{- Starter -- Test Lab 11 -}

t = Node 
    10 
    (Node 5
      (Node 2 Empty Empty)
      (Node 7 
            (Node 6 Empty Empty) 
            Empty))
    (Node 15
          Empty 
          (Node 20 Empty Empty))


t' = Node 
     7 
     (Node 5 
           (Node 2 Empty Empty)
           (Node 6 Empty Empty))
     (Node 15 
           (Node 10 Empty Empty) 
           (Node 20 Empty Empty))


testEq = TestCase $ assertEqual
         "t == t' is "
         True
         (t == t')

testShow = TestCase $ assertEqual
         "show t "
         "    20\n  15\n10\n    7\n      6\n  5\n    2\n"
         (show t)

testSizedBool = TestCase $ assertEqual
                "size True "
                1
                (size True)

testSizedList = TestCase $ assertEqual
                "size [1,2,3] "
                3
                (size [1,2,3])

testSizedBTree = TestCase $ assertEqual
                 "size t "
                 7
                 (size t)

tests = TestList [TestLabel "Eq" testEq
                  ,TestLabel "Show" testShow
                  ,TestLabel "SizedBool" testSizedBool
                  ,TestLabel "SizedList" testSizedList
                  ,TestLabel "SizedBTree" testSizedBTree
                 ]

main = runTestTT tests
