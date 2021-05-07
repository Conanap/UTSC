#lang scheme

(require "lab3.ss")
(require test-engine/scheme-tests)

(check-expect (my-length empty) 0)

(check-expect (my-length '(1 2 3)) 3)

(check-expect (my-reverse '(1)) '(1))

(check-expect (my-reverse '(1 2 3)) '(3 2 1))

(check-expect (is-pal '(1 2 3)) #f)

(check-expect (is-pal '(1 2 1)) #t)

(check-expect (is-pal '(1)) #t)

(check-expect (is-pal '()) #t)

(check-expect (is-pal '(1 2 2 1)) #t)

(check-expect (num-el empty) 0)

(check-expect (num-el '(1)) 1)

(check-expect (num-el '(1 (2 3))) 3)

(check-expect (stutter empty) empty)

(check-expect (stutter '(1)) '(1 1))

(check-expect (stutter '(1 2 2)) '(1 1 2 2 2 2))

(define (gg x) (odd? x))
(check-expect (my-filter gg empty) empty)

(check-expect (my-filter gg '(2)) empty)

(check-expect (my-filter gg '(1 2 3)) '(1 3))

(define (dd x) (* x 2))
(check-expect (my-map dd empty) empty)

(check-expect (my-map dd '(1)) '(2))

(check-expect (my-map dd '(1 2 3)) '(2 4 6))

(test)