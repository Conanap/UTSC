(module lab6 scheme
  
;;; (my-reverse xs) -> list
;;; xs: list
;;; Return the reverse of xs. (Not tail-recursive.)
(define my-reverse-1
  (λ (xs)
    (if (empty? xs)
      '()
      (append (my-reverse-1 (rest xs)) (list (first xs))))))

;;; tail-recursive version
(define my-reverse-2
  (λ (xs)
    (tail-reverse xs '())))

(define tail-reverse
  (lambda (xs ret)
    (if (empty? xs)
      ret
      (tail-reverse (rest xs) (append (list (first xs)) ret)))))

;;; (num-elts xs) -> int
;;; xs: list
;;; Return the number of elements in xs, including
;;; any sublists, on any nesting level.
;;; using recursion only, no tail recursion
(define num-els-1
  (λ (xs)
    (if (empty? xs)
      0
      (if (list? (first xs))
        (+ (num-els-1 (first xs)) (num-els-1 (rest xs)))
        (+ 1 (num-els-1 (rest xs)))))))

;;; using HOPs (and recursion)
;;; this solution should be much shorter!
(define num-els-2
  (λ (xs)
    (foldr (lambda (ele base) (if (list? ele)
      (+ (num-els-2 ele) base)
      (+ 1 base))) 0 xs)))

;;; tail-recursive version
(define num-els-3
  (λ (xs)
    (revesernumel xs 0)))

(define revesernumel
  (lambda (xs num)
    (if (empty? xs)
      num
      (if (list? (first xs))
        (revesernumel (append (first xs) (rest xs)) num)
        (revesernumel (rest xs) (+ 1 num))))))


;;; (flatten xs) -> list
;;; xs: list
;;; Return the flattened version of xs. 
;;; using recursion only, no tail recursion
(define flatten-1
  (λ (xs)
    (if (empty? xs)
      empty
      (if (list? (first xs))
        (append (flatten-1 (first xs)) (flatten-1 (rest xs)))
        (append (list (first xs)) (flatten-1 (rest xs)))))))

;;; using HOPs (and recursion)
(define flatten-2
  (λ (xs)
    (foldr (lambda (next base)
      (if (list? next)
        (append (flatten-2 next) base)
        (append (list next) base))) empty xs)))

;;; tail-recursive version
(define flatten-3
  (λ (xs)
    (reverseflatten xs '())))

(define reverseflatten
  (lambda (xs new)
    (if (empty? xs)
      new
      (if (list? (first xs))
        (reverseflatten (append (first xs) (rest xs)) new)
        (reverseflatten (rest xs) (append new (list (first xs))))))))

(provide my-reverse-1 my-reverse-2 num-els-1 num-els-2 num-els-3 flatten-1 flatten-2 flatten-3)
)
