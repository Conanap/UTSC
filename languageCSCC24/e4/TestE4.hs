module TestE4 where

import E4
import Test.HUnit

{- Starter -- Test E4 -}

testInterleave = TestList [testInterleave1]
testInterleave1 = TestCase $ assertEqual
                  "interleave ([42,45,56], [24,98,567]) is " [42,24,45,98,56,567]
                  (interleave ([42,45,56], [24,98,567]))

testToPairs = TestList [testToPairs1]
testToPairs1 = TestCase $ assertEqual
                  "toPairs ([42,45,56], [24,98,567]) is " [(42,24),(45,98),(56,567)]
                  (toPairs ([42,45,56], [24,98,567]))

testRepeat = TestList [testRepeat1]
testRepeat1 = TestCase $ assertEqual
              "repeat' (inc, 42, 3) is " [42,43,44,45]
              (repeat' (\ x -> x + 1, 42, 3))

testNumNeg = TestList [testNumNeg1]
testNumNeg1 = TestCase $ assertEqual
                       "numNeg [1,-2,3,-3.14] is " 2
                       (numNeg [1,-2,3,-3.14])

testGenSquares = TestList [testGenSquares1]
testGenSquares1 = TestCase $ assertEqual
                       "genSquares (1, 11) is " [4,16,36,64,100]
                       (genSquares (1, 11))

testTriples = TestList [testTriples1]
testTriples1 = TestCase $ assertEqual
                       "triples 15 is " [(3,4,5),(5,12,13),(6,8,10),(9,12,15)]
                       (triples 15)

tests = TestList [testInterleave,testToPairs,testRepeat,testNumNeg,testGenSquares,testTriples]

main = runTestTT tests
