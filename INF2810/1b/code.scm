;;Innleveringsoppgave 1b

;;1f

(car (cdr '(0 42 #t bar)))

;;1g

(car(cdr(car '((0 42) (#t bar)))))

;;1h

(car (car (cdr '((0) (42 #t) (bar)))))

;;1i

(cons (cons 0 (cons 42 '())) (cons (cons #t (cons 'bar '())) '()))

(list ( list 0 42)(list #t 'bar))

;;2a

(define (length2 items)
  (define (iter i items)
    (if (null? items)
        i
        (iter (+ 1 i) (cdr items))))
  (iter 0 items))

;;Test (length2 '(1 2 3))

;;2b Tail recursion, since we are using init
;; to keep track of the answer, and when the
;;base case is reached, the accumulated value
;;is returned

(define (fold-left proc init items)
  (if (null? items)
      init
      (fold-left proc (proc (car items) init)
                 (cdr items))))

;;Test 2b
(fold-left cons '() '(1 2 3 4 5))

;;2c

(define (all? proc items)
  (if (null? items)
      #t (if (not (proc (car items)))
             #f
             (all? proc (cdr items)))))

;;Test 2c
(all? odd? '(1 3 5 7 9))
(all? odd? '(1 2 3 4 5))
(all? odd? '())

;;2c with lambda

(all? (lambda (x) (> 10 x)) '(1 2 3 4 5))
(all? (lambda (x) (> 10 x)) '(1 2 3 4 50))

;;2d

(define (nth n items)
  (define (iter i items)
    (if (= n i)
        (car items)
        (iter (+ 1 i) (cdr items))))
  (iter 0 items))

;;Test 2d
(nth 2 '(47 11 12 13))

;;2e

(define (where n items)
  (define (iter i items)
    (if (null? items)
    #f
    (if (= n (car items))
      i
    (iter (+ 1 i) (cdr items)))))
 (iter 0 items))

;;Test 2e

(where 3 '(1 2 3 3 4 5 3))
(where 0 '(1 2 3 3 4 5 3))

;;2f

(define (map2 proc items1 items2)
  (if (null? items1)
      '()
      (if (null? items2)
          '()
          (cons (proc (car items1)(car items2))
                (map2 proc (cdr items1) (cdr items2))))))
;;Test 2f

(map2 + '(1 2 3 4) '(3 4 5))


;;2g

(map2 (lambda (i1 i2) (/ (+ i1 i2) 2)) '(1 2 3 4) '(3 4 5))

;;2h
(define (both? p)
  (lambda (x y)
   (cond ((and (p x) (p y)) #t)
         (else #f))))

;;2h test

(map2 (both? even?) '(1 2 3) '(3 4 5))
((both? even?) 2 4)
((both? even?) 2 5) 


;;2i
          
(define (self proc)
  (lambda (x) (proc x x)))

;;2i test
((self +) 5)
((self *) 3)
(self +)
((self list) "hello")




    
  



