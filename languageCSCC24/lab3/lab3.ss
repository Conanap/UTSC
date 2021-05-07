(module lab3 scheme

  ;;; (my-length xs) -> int
  ;;; xs: list
  ;;; Return the length of xs.
  (define (my-length xs)
    (if (empty? xs)
      0
      (+ 1 (my-length (rest xs))
      )
    )
  )

  ;;; (reverse xs) -> list
  ;;; xs: list
  ;;; Return the reverse of xs.
  (define (my-reverse xs)
    (if (empty? xs)
      '()
      (append (my-reverse (rest xs)) (list (car xs))
      )
    )
  )

  ;;; (is-pal xs) -> boolean
  ;;; xs: list
  ;;; Return whether xs is a palindrome.
  (define (is-pal xs)
    (cond
      [(empty? xs) #t]
      [(eq? 1 (length xs)) #t]
      [else
      (if(eq? (first xs) (last xs))
        (and #t
          (is-pal (rest (drop-right xs 1))
          )
        )
        #f
      )]
    )
  )

  ;;; (num-el xs) -> int
  ;;; xs: list
  ;;; Return the number of (non-list) elements of lst, on any nesting level.
  (define (num-el xs)
    (cond
      [(empty? xs) 0]
      [(list? (first xs))
        (+ (num-el (first xs)) (num-el (rest xs))
        )
      ] 
      [else
        (+ 1 (num-el (rest xs))
        )
      ]
    )
  )

  ;;; (stutter xs) -> list
  ;;; xs: list
  ;;; Return a list, which repeats each element of lst.
  (define (stutter xs)
    (if (empty? xs)
      '()
      (append (list(first xs))
        (list(first xs))
        (stutter (rest xs))
      )
    )
  )

  ;;; (my-filter f xs) -> list
  ;;; f: boolean-valued function applicable to every element of xs
  ;;; xs: list
  ;;; Return a list of those elements from xs that pass the function
  ;;;  f (i.e., f(x) is true for element x in xs), in their original order.
  (define (my-filter f xs)
    (cond
      [(empty? xs) '()]
      [(f (first xs))
        (append (list (first xs))
          (my-filter f (rest xs))
        )
      ]
      [else (my-filter f (rest xs))]
    )
  )

  ;;; (my-map f xs) -> list
  ;;; f: function applicable to every element of xs
  ;;; xs: list
  ;;; Return the result of applying f to every element of xs.
  (define (my-map f xs)
    (if (empty? xs)
      '()
      (append
        (list (f (first xs))
        )
        (my-map f (rest xs))
      )
    )
  )

  (provide my-length my-reverse is-pal num-el stutter my-filter my-map)
  )
