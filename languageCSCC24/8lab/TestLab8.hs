module TestLab8 where

import Lab8
import Test.HUnit

testLife = TestCase $ assertEqual
                 ("life is ") 42
                 (life)

testGreet = TestCase $ assertEqual
              ("greet Anya is ") "hello, Anya" (greet "Anya")

testStutter1 = TestCase $ assertEqual
              ("stutter [] is ") ([]::[Int]) (stutter [])

testStutter2 = TestCase $ assertEqual
              ("stutter [1,2,3] is ") [1,1,2,2,3,3] (stutter [1,2,3])

testLen1 = TestCase $ assertEqual
              ("len [] is ") 0 (len ([]::[Int]))

testLen3 = TestCase $ assertEqual
              ("len foobar is ") 6 (len "foobar")

testFirsts1 = TestCase $ assertEqual
              ("firsts [] is ") [] (firsts ([]::[(Int,Int)]))

testFirsts2 = TestCase $ assertEqual
              ("firsts [(1,2)] is ") [1] (firsts [(1,2)])

tests = TestList [TestLabel "life" testLife,
                  TestLabel "stutter 1" testStutter1,
                  TestLabel "stutter 2" testStutter2,
                  TestLabel "len  1" testLen1,
                  TestLabel "len  3" testLen3,
                  TestLabel "firsts  1" testFirsts1,
                  TestLabel "firsts  2" testFirsts2
                 ]

main = runTestTT tests
     
