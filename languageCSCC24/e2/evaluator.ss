(module evaluator scheme
  ; some helper functions that should make your code much easier to read
  (define unary-op first)
  (define operand second)
  (define binary-op second)
  (define left first)
  (define right third)
  (define (atomic? v) (not (list? v)))
  (define (unary? expr) (= 2 (length expr)))
  (define (binary? expr) (= 3 (length expr)))
  (define (listsym? expr) (or (list? expr) (symbol? expr)))
  (define fpair car)
  (define spair cdr)

  
  ; It is a good idea to define more such helpers here...
  
  ; (evaluate expr context) -> boolean
  ; expr: a valid representation of an expression
  ; context: list of pairs: symbol to #t/#f
  ; Return the value of expr with values of all variables
  ; that occur in expr specified in context.
  (define evaluate
    (lambda (expr context)
      (evalExpr expr context)))

  ; parsing for a boolean expression
  (define evalExpr
    (lambda (expr context)
      (evalDis expr context)))

  ; parsing for a disjunction
  (define evalDis
    (lambda (expr context)
      (cond ; three statements cuz 3 nested bools will look really uggly
        [(not (list? expr)) (evalCon expr context)]
        [(atomic? expr) (evalCon expr context)]
        [(eq? (binary-op expr) 'or)
          (define fromleft (evalDis (left expr) context))
          (define fromright (evalCon (right expr) context))
          (cond
            [(and (listsym? fromleft) (listsym? fromright))
              (list fromleft 'or fromright)] ; both are symbols
            [(listsym? fromright) fromright]
            [(listsym? fromleft) fromleft]
            [else (or fromleft fromright)])] ; disjunction
        [else (evalCon expr context)]))) ; conjunction

  ; parsing for a conjunction
  (define evalCon
    (lambda(expr context)
      (cond ; same reason for cond as evaldis
        [(not (list? expr)) (evalNeg expr context)]
        [(atomic? expr) (evalNeg expr context)]
        [(eq? (binary-op expr) 'and)
          (define fromleft (evalCon (left expr) context))
          (define fromright (evalNeg (right expr) context))
          (cond
            [(and (listsym? fromleft) (listsym? fromright)) (list fromleft 'and fromright)]
            [(listsym? fromleft) (if fromright fromleft #f)]
            [(listsym? fromright)  (if fromleft fromright #f)]
            [else (and fromleft fromright)])]
        [else (evalNeg expr context)])))

  ; parsing for a negation
  (define evalNeg
    (lambda(expr context)
      (cond
        [(not (list? expr))
          (evalBrac expr context)]
        [(atomic? expr) (evalBrac expr context)]
        [(eq? (left expr) 'not) ; unwrapping a not statement
        (define neg (evalNeg (second expr) context))
        (if (listsym? neg)
          (list 'not neg)
          (not neg))]
        [else (evalBrac expr context)])))

  ; parsing for a bracket
  (define evalBrac
    (lambda(expr context)
      (cond
        [(not (list? expr)) (evalPrim expr context)]
        [(atomic? expr) (evalPrim expr context)]
        [else (evalExpr expr context)]))); ot a primiative -> a bool expr

  ; parsing for a primitive
  (define evalPrim
    (lambda(expr context)
      (cond
        [(not (list? expr)) (evalSym expr context)]
        [(boolean? (left expr)) (left expr)]
        [else (evalSym expr context)]))); get what it is if it's a symbol

  ; parsing for a symbol
  (define evalSym
    (lambda(expr context)
      (if (list? expr)
        (getele (left expr) context)
        (getele expr context))))

  ; for finding the element
  (define (getele ele context)
    (if (empty? context)
      ele
      (if (eq? (fpair (first context)) ele) ; check first pair first element
          (spair (first context))
          (getele ele (rest context)))))

  ; (simplify expr context) -> valid expression
  ; expr: a valid representation of an expression
  ; context: list of pairs: symbol to #t/#f
  ; Return an expression that is equivalent to expr,
  ; but is simplified as much as possible, according to
  ; the given rules.
  (define simplify
    (lambda (expr context)
      (evaluate expr context)))
  
  (provide evaluate simplify) 
)
