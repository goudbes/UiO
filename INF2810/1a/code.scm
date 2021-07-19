;; 2. b

(define (sign x)
  (if (= x 0)
      0
      (if (< x 0)
          -1
          1)))

(define (sign_ x)
  (cond (( = x 0) 0)
        (( > x 0) 1)
        (( < x 0) -1)))

;;2. c

(define (signc x)
  (or (and (= x 0) 0) (and (< x 0) -1) (and (> x 0) 1)))

;; 3. a

(define (add1 x)
  (+ x 1))

(define (sub1 x)
  (- x 1))

;; 3b
(define (plus x y)
  (if (zero? y)
      x
      (plus (add1 x) (sub1 y))))

;; 3c
(define (plus-c x y)
  (define (iter sum counter max-count)
    (if (> counter max-count)
      sum
      (iter (add1 sum) (add1 counter) max-count)))
  (iter x 1 y))


;; 3.d
(define (power-close-to b n)
  (define (power-iter e)
    (if (> (expt b e) n)
        e
        (power-iter (+ 1 e))))
    (power-iter 1))


