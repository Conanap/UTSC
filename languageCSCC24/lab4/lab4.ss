(module lab4 scheme

;; Learning objective:
;; study Scheme's map, apply, and lambda expressions.

; throughout this module we represent a vector by a list
; and a matrix by a list of lists of numbers
; you may assume all list sizes are such that we have
; well-defined matrices.
; An empty matrix is an empty list.

; (dot-product v1 v2) -> number
; v1, v2: list of number, of the same size
; return the dot product of v1 and v2
; a recursive version
(define (dot-product-rec v1 v2)
    (if (empty? v1)
        0
        (+ (*(first v1) (first v2)) ; add first of each and recur rest
            (dot-product-rec (rest v1) (rest v2)))
    )
)

; no recursion! use map and apply
(define (dot-product v1 v2) (apply + (map * v1 v2)))

; (add v1 v2) -> list of number 
; v1, v2: list of number
; return v1 + v2
; a recursive version 
(define (vector-add-rec v1 v2)
    (if (empty? v1)
        empty
        (cons (+ (first v1) (first v2)) (vector-add-rec (rest v1) (rest v2)))
    )
)

; no recursion: use map
(define (vector-add v1 v2) (map + v1 v2))

; (add m1 m2) -> list of list of number
; m1, m2: list of list of number, of same dimensions
; return m1 + m2 for matrices m1, m2
; no recursion: use map
(define (add m1 m2) (map vector-add m1 m2))

; (scalar-vector-mult k v) -> list of number
; k: number
; v: list of number
; return the scalar multiplication kv
; no recursion: use map (and a lambda expression)
(define (scalar-vector-mult k v)
    (map
    (lambda(x)
        (* x k)) ; multiply each mapped element by k (ie, paseses ele as arg to lambda)
    v
    )
)

; (scalar-mult k m) -> list of list of number
; k: number
; m: list of list of number
; return the scalar multiplication km
; no recursion: use map (and a lambda expression)
(define (scalar-mult k m)
    (map 
        (lambda(x) ; each array in matrix -> do scalar mult
            (scalar-vector-mult k x)
        )
        m
    )
)

; (transpose m) -> list of list of number
; m: list of list of number, non-empty
; return the transpose of matrix m
; a recursive version
(define (transpose-rec m)
    (if (empty? (first m))
        empty
        (cons (map first m)
            (transpose-rec (map rest m)))
    )
)

;think, think, think!
;implement transpose non recursively!
;this one's tricky! 
(define (transpose m)
    (apply
        map list m
    )
)

; (mult m1 m2) -> list of list of number
; m1, m2: list of list of number
; return the matrix multiplication m1xm2
; no recursion: use maps, lambda expressions and the above
; transpose functions
; transpose second matrix
; move down first matrix to go down vertically (how i want to build list)
(define (mult m1 m2)
    (map
        (lambda(x)
            (map
                (lambda(y)
                    (dot-product y x)
                )
                (transpose m2)
            )
        )
        m1
    )
)
(provide dot-product-rec dot-product vector-add-rec vector-add add scalar-vector-mult scalar-mult transpose-rec transpose mult)
)
