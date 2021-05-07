(define cond-apply ;; single call to map
  (lambda (p f xs)
    (map (lambda (y) (if (p y) (f y) y)) xs)))

(define cond-apply ;; recursive
  (lambda (p f xs)
  (if (empty? xs) '()
    (cons (if (p (first xs)) (f (first xs)) (first xs)) (cond-apply p f (rest xs))))))

(define cond-apply
  (lambda (p f xs)
    (local [(define cond-apply-tail
      (lambda (xs k)
        (if (empty? xs) k
          (if (p (first xs))
            (cond-apply-tail (rest xs) (append k (list (f (first xs)))))
            (cond-apply-tail (rest xs) (append k (list (first xs))))))))]
    (cond-apply-tail xs empty))))

