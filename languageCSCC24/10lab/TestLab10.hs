module TestLab10 where

import Lab10
import Test.HUnit

{- Starter -- Test Lab 10 -}

t = Node (1,
           Node (2, Empty, Empty),
           Node (3, 
                 Node (4, Empty, Empty),
                 Empty))
tInc = Node (2,
           Node (3, Empty, Empty),
           Node (4, 
                 Node (5, Empty, Empty),
                 Empty))
tFlip = Node (1,
            Node (3, 
                  Empty,
                  Node (4, Empty, Empty)),
            Node (2, Empty, Empty))


testNumNodes = TestCase $ assertEqual
              ("numNodes t is ") 4
              (numNodes t)

testNumLevels = TestCase $ assertEqual
              ("numLevels t is ") 3
              (numLevels t)

testTFlip = TestCase $ assertEqual
              ("tflip t is ") tFlip
              (tflip t)

testTMap = TestCase $ assertEqual
              ("tmap (+1) t is ") tInc
              (tmap (+1) t)

tests = TestList [TestLabel "numNodes" testNumNodes,
                  TestLabel "numLevels" testNumLevels,
                  TestLabel "tflip" testTFlip,
                  TestLabel "tmap" testTMap
                 ]

main = runTestTT tests
