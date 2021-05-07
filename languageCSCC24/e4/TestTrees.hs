module TestTrees where

import Trees
import Test.HUnit

{- Starter -- Test E4 -}

three = Node (1, Leaf 2, Leaf 3)

testCountNodes = TestList [testCountNodes1]
testCountNodes1 = TestCase $ assertEqual
                 ("countNodes three is ") 3
                 (countNodes three)

testForallNodes = TestList [testForallNodes1]
testForallNodes1 = TestCase $ assertEqual
                 ("forallNodes >2 is ") False
                 (forallNodes (>2) three)

testExistsNode = TestList [testExistsNode1]
testExistsNode1 = TestCase $ assertEqual
                 ("existsNode >2 is ") True
                 (existsNode (>2) three)

testInorder = TestList [testInorder1]
testInorder1 = TestCase $ assertEqual
               ("inroder three is ") [2,1,3]
               (inorder three)

testCountNodes' = TestList [testCountNodes1']
testCountNodes1' = TestCase $ assertEqual
                   ("countNodes' three is ") 3
                   (countNodes' three)

testForallNodes' = TestList [testForallNodes1']
testForallNodes1' = TestCase $ assertEqual
                    ("forallNodes' >2 is ") False
                    (forallNodes' (>2) three)

testExistsNode' = TestList [testExistsNode1']
testExistsNode1' = TestCase $ assertEqual
                   ("existsNode' >2 ") True
                   (existsNode' (>2) three)

testInorder' = TestList [testInorder1']
testInorder1' = TestCase $ assertEqual
                 ("inorder' three is ") [2,1,3]
                 (inorder' three)

tests = TestList [testCountNodes,testCountNodes',
                  testForallNodes,testForallNodes',
                  testExistsNode,testExistsNode',
                  testInorder,testInorder'
                 ]

main = runTestTT tests
