#lang scheme

; (scale-func old new) -> procedure
; old, new: positive integers
; Return a procedure that takes a value on a [0, old] scale
; and returns a corresponding value of a [0, new] scale.
(define scale-func
  (λ (old new)
    (λ (value)
      (/ (* value new) old))))
