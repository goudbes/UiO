;; Innlevering 2b i inf2810, vår 2017

(load "prekode2b.scm")

;; 1 INNKAPSLING, LOKAL TILSTAND OG OMGIVELSESMODELLEN


;; 1 (a)
'(Oppgave 1a)

(define count 42)

(define (make-counter)
  (let ((count 0))
    (lambda ()
      (set! count (+ count 1))
      count)))

;; 1a test
(define c1 (make-counter))
(define c2 (make-counter))
(c1)
(c1)
(c1)
count
(c2)

;; 1(a) Tegning
;; fil 1-tegning.pdf

;; 2 INNKAPSLING, LOKAL TILSTAND OG MESSAGE PASSING

;; 2(a)

'(Oppgave 2a)
(define (make-stack list)
  (let ((stack list))
    (define (pop)
      (if (not(null? stack))
          (set! stack (cdr stack))))
    (define (push list1)
      (if (not (null? list1))
          (begin
            (set! stack (cons (car list1) stack))
            (push (cdr list1)))))
    (define (stack-print) stack)
    (lambda (message . arguments)
      (cond ((eq? message 'pop!)  (pop))
            ((eq? message 'push!) (push arguments))
            ((eq? message 'stack) (stack-print))
            ))))


;; 2(a) test

(define s1 (make-stack (list 'foo 'bar)))
(define s2 (make-stack '()))

(s1 'pop!)
(s1 'stack)
(s2 'pop!)
(s2 'push! 1 2 3 4)
(s2 'stack)
(s1 'push! 'bah)
(s1 'push! 'zap 'zip 'baz)
(s1 'stack)
 

;; 2(b)
'(Oppgave 2b)

(define (pop! stack-num)
  (stack-num 'pop!))

(define (stack stack-num)
  (stack-num 'stack))

(define (push! stack-num . arguments)
  (define (push-1 arguments-1)
    (if (not (null? arguments-1))
        (begin
          (stack-num 'push! (car arguments-1))
          (push-1 (cdr arguments-1)))))
  (push-1 arguments))


;; 2(b) test
(pop! s1)
(stack s1)
(push! s1 'foo 'faa)
(stack s1)


;; 3 STRUKTURDELING OG SIRKULÆRE LISTER
;; TEGNING - 3-tegning.pdf
'(Oppgave 3a)

(define bar (list 'a 'b 'c 'd 'e))
(set-cdr! (cdddr bar) (cdr bar))
(list-ref bar 0)
(list-ref bar 3)
(list-ref bar 4)
(list-ref bar 5)


;; Peker tilbake til listen


;; 3(b)
'(Oppgave 3b)

(define bah (list 'bring 'a 'towel))
(set-car! bah (cdr bah))
bah
(set-car! (car bah) 42)
bah


;; 3(c)
;; bar evalueres ikke til en liste, fordi vi kommer aldri til '().
'(Oppgave 3c)
(list? bar)
(list? bah)

;; 4 DYNAMISK PROGRAMMERING: MEMOISERING

'(Oppgave 4a,b)

(define original-func (make-table))
(define (mem message func)
  (let ((table (make-table))
        (original func))
    (cond ((eq? message 'memoize)
        (let ((new-func
               (lambda args
                 (let ((previously-computed-result (lookup args table)))
                   (or previously-computed-result
                       (let ((result (apply func args)))
                         (insert! args result table)
                         result))))))
          (insert! new-func original original-func)
          new-func))
          ((eq? message 'unmemoize)
           (lookup func original-func)))))

;; Test 4 (a,b)

(set! fib (mem 'memoize fib))
(fib 3)
(fib 3)
(fib 2)
(fib 4)
(set! fib (mem 'unmemoize fib))
(fib 3)

(set! test-proc (mem 'memoize test-proc))
(test-proc)
(test-proc)
(test-proc 40 41 42 43 44)
(test-proc 40 41 42 43 44)
(test-proc 42 43 44)

;; 4(c)
'(Oppgave 4c)
(define mem-fib (mem 'memoize fib))
(mem-fib 3)
(mem-fib 3)
(mem-fib 2)

;;Mem-fib kalkulerer for 3 og lagrer resultatet til senere oppslag, men
;;lagrer ikke verdien av 2 eller 1, så når vi kaller mem-fib 2 så gjør den
;;kalkulasjonene på nytt
