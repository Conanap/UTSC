#lang racket

(require rackunit)
(require rackunit/text-ui)
(require "lab7.ss")

(define-test-suite lab7-test-suite
  
  (test-equal? 
   "foldr-tail"
   (foldr-tail - 0 '())
   0)
  
  (test-equal? 
   "foldr-tail"
   (foldr-tail + 0 '(1 2 3))
   6)

  (test-equal? 
   "foldr-cps"
   (foldr-cps - 0 '())
   0)
  
  (test-equal? 
   "foldr-cps"
   (foldr-cps + 0 '(1 2 3))
   6)

  (test-equal?
   "map-cps"
   (map-cps abs '())
   '()
   )
  
  (test-equal?
   "map-cps"
   (map-cps abs '(1 -2 3 -4 -5 -6))
   '(1 2 3 4 5 6)
   )
  )
(display (run-tests lab7-test-suite))
