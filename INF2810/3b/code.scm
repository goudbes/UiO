;; Innlevering 3b i inf2810, vår 2017
(load "evaluator.scm")

;; 1a

(set! the-global-environment (setup-environment))
(mc-eval '(+ 1 2) the-global-environment)
(read-eval-print-loop)
;;(mc-eval '(define (foo cond else)
          ;; (cond ((= cond 2) 0)
           ;;      (else (else cond)))) the-global-environment)
;;(mc-eval '(define cond 3) the-global-environment)
;;(mc-eval '(define (else x) (/ x 2)) the-global-environment)
;;(mc-eval '(define (square x) (* x x)) the-global-environment)
;;(mc-eval '(foo 2 square) the-global-environment)
;;(mc-eval '(foo 4 square) the-global-environment)
;;(mc-eval '(cond ((= cond 2) 0)
                ;;(else (else 4))) the-global-environment)

;(foo 2 square) returnerer 0, fordi det er det foo skal gjøre når cond er 2
;(foo 4 square) returnerer 16, fordi square sier at x skal ganges med x
;(cond ((= cond 2) 0) (else (else 4))) returnerer 2 fordi cond er eksternt
;definert til å være 3, så else blir evaluert, og else er eksternt definert
;til å dele x, dvs. 4, med 2.

;; 2b
;; Vi fikk ikke det til å virke når det blir kjørt fra meta-repl, men
;; funksjonen funker som du kan se under

(define (install-primitive! proc args)
  (let ((prim-object (list 'primitive args)))
    (set! primitive-procedures
          (append primitive-procedures (list (list proc args))))
    (define-variable! proc prim-object the-global-environment)))

(install-primitive! 'square (lambda (x) (* x x)))
(mc-eval '(square 3) the-global-environment)
(mc-eval '(square 4) the-global-environment)
(mc-eval '(square 5) the-global-environment)

;;3a og 3b se evaluator.scm


;;Oppgave 3c før vi gjorde 3d

;;(define (let->lambda exp env)
;;  (define lm '())
;;  (define var-list (list (caaar exp)))
;;  (define exp-list (list (cadaar exp)))
;;  (define (make-let exp body var-list exp-list)
;;    (if (not (null? exp))
;;        (make-let (cdr exp) body
;;                  (append var-list (cons (caar exp) '()))
;;                  (append exp-list (cons (cadar exp) '())))
;;        (begin
;;          (set! lm (list (make-lambda var-list body)))
;;          (set-cdr! lm exp-list)
;;          (mc-eval lm env))))
;;  (make-let (cdar exp) (cdr exp) var-list exp-list))

;; 3d se evaluator.scm

;; 3e se evaluator.scm, men vi får samme problem som i 2b (unbound variable)


