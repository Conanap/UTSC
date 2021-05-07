(define (map1 func li)
    (if (empty? li)
    empty
    (append (list (func (first li))) (map1 func (rest li)))))

(define (map1-tail func li)
    (tailmap func li empty))

(define (tailmap func li new)
    (if (empty? li)
    new
    (tailmap func (rest li) (append new (list (func (first li)))))))

(define mymap (lambda vars (apply map1 (first vars) (rest vars))))

(define realmap
  (lambda (f . lsts)
    (if (empty? (first lsts))
        empty
        (cons (apply f (map1 first lsts)) (apply realmap f (map1 rest lsts))))))

(define (deepmap f xs)
    (if (empty? xs)
        empty
        (if (list? (first xs))
            (cons (deepmap f (first xs)) (deepmap f (rest xs)))
            (cons (f (first xs)) (deepmap f (rest xs))))))

(define ca 
    (lambda (p f xs)
        (if (empty? xs)
            empty
            (if (p (first xs))
                (append (list (f (first xs))) (ca p f (rest xs)))
                (append (list (first xs)) (ca p f (rest xs)))))))

(define ca2
    (lambda (p f xs)
        (map
            (lambda (ele)
                (if (p ele)
                    (f ele)
                    ele)) xs)))

(define ca3
    (lambda (p f xs)
        (define cat (lambda (xs k)
            (if (empty? xs) k
                (if (p (first xs))
                    (cat (rest xs) (append k (list (f (first xs)))))
                    (cat (rest xs) (append k (list (first xs)))))
                )))
        (cat xs empty)))

(define cases (lambda (in . xs) 
    (if (empty? xs)
        'Error
        (if (empty? (first (first xs)))
            (cases in (rest xs))
            (if (or (eq? (first (first (first xs))) in) (eq? 'else (first (first (first xs)))))
                (second (first xs))
                (cases in (cons (cons (rest (first (first xs))) (rest (first xs))) (rest xs))))))))