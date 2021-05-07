(module lab7 scheme
  
  ;;; define a tail-recursive, linear-time foldr without using
  ;;; continuation passing style
  (define foldr-tail 
    (lambda (func base ls)
      (if (empty? ls)
        base
        (foldr-tail func (func (last ls) base) (drop-right ls 1)))))
  
  ;;; define a tail-recursive, linear-time foldr using CPS
  (define foldr-cps (lambda (func base xs) 
    (define (foldr-cpsin func base xs basefunc)
      (if (empty? xs)
        (basefunc base)
        (foldr-cpsin func base (drop-right xs 1) (lambda(x) (basefunc (func (last xs) x))))))
    (foldr-cpsin func base xs identity)))
  
  ; define a tail-recursive linear-time version of map (a simplified version
  ; that works on one list) using CPS
  (define map-cps (lambda (func xs)
      (define (map-cpsin func xs basefunc)
        (if (empty? xs)
          (basefunc xs)
          (map-cpsin func (rest xs) (lambda(x) (basefunc (cons (func (first xs)) x))))))
      (map-cpsin func xs identity)))
  
  (provide foldr-tail foldr-cps map-cps)
  )
