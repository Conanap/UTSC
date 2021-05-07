#lang racket

(require rackunit)
(require rackunit/text-ui)
(require "evaluator.ss")

(define-test-suite evaluator-test-suite
  
  (test-equal? 
   "evaluate"
   (evaluate '(a and (b or c))
             '((a . #t) (b . #f) (c . #t)))
   #t)

  (test-equal? 
   "evaluate"
   (evaluate '((a and (not b)) or ((a and c) or ((not b) and c)))
             '((a . #t) (b . #f) (c . #f) (d . #t)))
   #t)

  (test-equal?
  "evaluate"
  (evaluate 'a
    '((a . #t)))
    #t)

  (test-equal?
  "evaluate"
  (evaluate 'a
    '((a . #f)))
    #f)

  (test-equal?
  "evaluate"
  (evaluate '(not a)
    '((a . #t)))
    #f)

  (test-equal?
  "evaluate"
  (evaluate '(a and b)
    '((a . #t) (b . #f)))
    #f)

  (test-equal?
  "evaluate"
  (evaluate '(a or b)
    '((a . #f) (b . #t)))
    #t)

  (test-equal?
  "evaluate"
  (evaluate '((not a) and b)
    '((a . #t) (b . #t)))
    #f)

  (test-equal?
  "evaluate"
  (evaluate '(not (a and b))
    '((a . #t) (b . #t)))
    #f)
  )

(define-test-suite simplifier-test-suite
  
  (test-equal? 
   "simplify"
   (simplify '(a and (b or c))
             '((a . #t) (b . #f) (c . #t)))
   #t)

  (test-equal? 
   "simplify"
   (simplify '(a and (b or c))
             '((b . #f) (c . #t)))
   'a)

  (test-equal? 
   "simplify"
   (simplify '((a and (not b)) or ((a and c) or ((not b) and c)))
             '((a . #t) (c . #f) (d . #t)))
   '(not b))

  (test-equal? 
   "simplify"
   (simplify 'a
             '((a . #t) (b . #f) (c . #t)))
   #t)

  (test-equal? 
  "simplify"
  (simplify '(a and b)
             '())
   '(a and b))

  (test-equal? 
  "simplify"
  (simplify '((a and b) and c)
             '((c . #t)))
   '(a and b))

  (test-equal? 
  "simplify"
  (simplify '((a and b) and c)
             '((c . #f)))
   #f)

  (test-equal? 
  "simplify"
  (simplify '((a and b) or c)
             '((c . #t)))
   '(a and b))

  (test-equal? 
  "simplify"
  (simplify '((a and b) or c)
             '((c . #f)))
   '(a and b))

  (test-equal? 
  "simplify"
  (simplify '((a and b) and (not c))
             '((a . #t) (b . #t)))
   '(not c))

  (test-equal? 
  "simplify"
  (simplify '((a and b) and c)
             '((a . #t)))
   '(b and c))

  (test-equal? 
  "simplify"
  (simplify '((a and b) and (not c))
             '((a . #t)))
   '(b and (not c)))

  (test-equal? 
   "simplify"
   (simplify 'a
             '())
   'a)
  )

(display (run-tests evaluator-test-suite))
(display (run-tests simplifier-test-suite))