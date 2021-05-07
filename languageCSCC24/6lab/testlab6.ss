#lang racket

(require rackunit)
(require rackunit/text-ui)
(require "lab6.ss")


(define-test-suite lab6-test-suite

  (test-case 
   "my-reverse-1"
   (check-equal? (my-reverse-1 '(1 2 3)) '(3 2 1))
   (check-equal? (my-reverse-1 '(1 (2 4) 3)) '(3 (2 4) 1))
   (check-equal? (my-reverse-1 empty) empty)
   )

  (test-case 
   "my-reverse-2"
   (check-equal? (my-reverse-2 '(1 2 3)) '(3 2 1))
   (check-equal? (my-reverse-2 '(1 (2 4) 3)) '(3 (2 4) 1))
   (check-equal? (my-reverse-2 empty) empty)
   )

  (test-case 
    "num-els-1"
   (check-equal? (num-els-1 '(1 (2 (3) (4 5) (((6))) 7))) 7)
   (check-equal? (num-els-1 empty) 0)
   (check-equal? (num-els-1 '(1 2 3)) 3)
   )

  (test-case 
   "num-els-2"
   (check-equal? (num-els-2 '(1 (2 (3) (4 5) (((6))) 7))) 7)
   (check-equal? (num-els-2 empty) 0)
   (check-equal? (num-els-2 '(1 2 3)) 3)
   )

  (test-case 
   "num-els-3"
   (check-equal? (num-els-3 '(1 (2 (3) (4 5) (((6))) 7))) 7)
   (check-equal? (num-els-3 empty) 0)
   (check-equal? (num-els-3 '(1 2 3)) 3)
   )

  (test-case
   "flatten-1"
   (check-equal? (flatten-1 '(1 (2 (3) (4 5) (((6))) 7))) '(1 2 3 4 5 6 7))
   (check-equal? (flatten-1 empty) empty)
   (check-equal? (flatten-1 '(1 2 3)) '(1 2 3))
   (check-equal? (flatten-1 '((1) 2 (3 4 (5)) 6)) '(1 2 3 4 5 6))
   )

  (test-case
   "flatten-2"
   (check-equal? (flatten-2 '(1 (2 (3) (4 5) (((6))) 7))) '(1 2 3 4 5 6 7))
   (check-equal? (flatten-2 empty) empty)
   (check-equal? (flatten-2 '(1 2 3)) '(1 2 3))
   (check-equal? (flatten-2 '((1) 2 (3 4 (5)) 6)) '(1 2 3 4 5 6))
   )

  (test-case
   "flatten-3"
   (check-equal? (flatten-3 '(1 (2 (3) (4 5) (((6))) 7))) '(1 2 3 4 5 6 7))
   (check-equal? (flatten-3 empty) empty)
   (check-equal? (flatten-3 '(1 2 3)) '(1 2 3))
   (check-equal? (flatten-1 '((1) 2 (3 4 (5)) 6)) '(1 2 3 4 5 6))
   )
 )
(display (run-tests lab6-test-suite))
