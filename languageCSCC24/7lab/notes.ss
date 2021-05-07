(define (len-cps xs k)
    (if (empty? xs)
        (k o)
        (len-cps (rest xs) (lambda (x) (k (+ 1 x))))))

(define (fact n k)
    (if (= n 0)
        (k 1)
        (fact (- n 1) (lambda(x) (k (*n x))))))