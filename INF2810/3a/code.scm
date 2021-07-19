;; Innlevering 3b i inf2810, vår 2017

(load "prekode3a.scm")


;; 1 STRØMMER

;; 1(a)

(define (list-to-stream list)
  (if (null? list)
      '()
      (cons-stream (car list)(list-to-stream (cdr list)))))

(define (stream-to-list stream . args)
  (define (stream-to-list-2 stream arg)
    (if (null? arg)
        (begin
          (if (stream-null? stream)
              '()
              (cons (stream-car stream) (stream-to-list (stream-cdr stream)))))
        (stream-to-list-3 stream arg)))
  (define (stream-to-list-3 stream n)
    (if (= n 0)
        '()
        (cons (stream-car stream) (stream-to-list-3 (stream-cdr stream) (- n 1)))))
  (if (pair? args)
      (stream-to-list-2 stream (car args))
      (stream-to-list-2 stream '())))

;; 1(a) Test
(display "\n 1(a) \n")

(list-to-stream '(1 2 3 4 5))
(stream-to-list (stream-interval 10 20))
(show-stream nats 15)
(stream-to-list nats 10) 


;; 1(b)

(define (one-is-empty stream)
  (if (not(null? stream))
      (begin
        (if (stream-null? (car stream))
            0
            (one-is-empty (cdr stream))))
      '()))

(define (stream-map proc . argstreams)
  (if (equal? 0 (one-is-empty argstreams))
      the-empty-stream
      (cons
       (apply proc (map stream-car argstreams))
       (apply stream-map
              (cons proc (map stream-cdr argstreams))))))

;; 1(b) Test
(display "\n 1(b) \n")
(stream-map + (list-to-stream '(1 2 3 4 5)) (list-to-stream '(1 2 3 4 5)))
(stream-map + (list-to-stream '(1 2 3 4 5)) (list-to-stream '(1 2 3 4)))
(stream-map + (list-to-stream '(1 2 3 4 5)) (list-to-stream '()))

;; 1(c)
;;Stream-cons evaluerer ikke cdr-verdien automatisk, så hvis vi bytter cons med stream-cons uten andre endringer
;;blir ikke den rekursive delen evaluert, og funksjonen stopper

(define (remove-duplicates lst)
  (cond ((null? lst) '())
        ((not (memq (car lst) (cdr lst)))
         (cons (car lst) (remove-duplicates (cdr lst))))
        (else (remove-duplicates (cdr lst)))))

;(define (memq item x)
;  (cond ((null? x) #f)
;        ((eq? item (car x)) x)
;        (else (memq item (cdr x)))))

(display "\n 1(c) \n")
(remove-duplicates '(1 2 3 3 4 1 5 6 3))


;; 1(d)
;; Define x (stream-map show ... skriver ut tallene 0 til 10 med linjebrudd mellom.
; (stream-ref x 5) returnerer en feilmelding om at den forventet et argument av typen promise fordi stream-ref's første argument
; skal være en stream, men x er definert som en funksjon som skriver ut en stream.

(display "\n 1(d) \n")

(define (show x)
  (display x)
  (newline)
  x)

;;(define x
;;  (stream-map show
;;             (stream-interval 0 10)))
;;(stream-ref x 5)
;;(stream-ref x 7)

;; 2 SPRÅKMODELLER (BASAL 'MASKINLÆRING')

;; 2(a)

(define (make-lm)
  (list '0))

(define (lm-lookup-unigram lm string)
  (define (lm-lookup-unigram-1 lm-1 string-1 answer else-case)
    (if (null? lm-1)
        (cond ((= answer 0)(/ 1 (car lm)))
              ((= answer 1) else-case)
              (else answer))
        (if (string=? string-1 (caaar lm-1))
            (lm-lookup-unigram-1 (cdr lm-1) string-1 (+ (cdar lm-1) answer) (+ 1 else-case))
            (lm-lookup-unigram-1 (cdr lm-1) string-1 answer else-case))))
  (lm-lookup-unigram-1 (cdr lm) string 0 0))

(define (lm-lookup-bigram lm string1 string2)
  (define (lm-lookup-bigram-1 lm-1 pair)
    (if (null? lm-1)
        (/ 1 (car lm))
        (if (equal? pair (caar lm-1))
            (cdar lm-1)
            (lm-lookup-bigram-1 (cdr lm-1) pair))))
  (lm-lookup-bigram-1 (cdr lm) (cons string1 string2)))

(define (lm-record-bigram! lm string1 string2)
  (define (lm-record-bigram-1 list1)
    (let ((record (assoc list1 (cdr lm))))
      (if record
          (set-cdr! record (+ (cdr record) 1))
          (set-cdr! lm 
                    (cons (cons list1 1) (cdr lm))))))
     (set-car! lm (+ (car lm) 1))
     (lm-record-bigram-1 (cons string1 string2)))
  
(define lm (make-lm))

;; 2(b)
(define (lm-train! lm corpus)
  (define (check-sentence sentence)
    (if (null? sentence)
        '()
        (if (> (length sentence) 1)
            (begin
              (lm-record-bigram! lm (car sentence) (cadr sentence))
              (check-sentence (cdr sentence))))))
  (define (train lm-1 corpus)
    (if (null? corpus)
        '()
        (begin
          (check-sentence (car corpus))
          (train lm-1 (cdr corpus)))))
  (train lm corpus))

;; 2(c)

(define (lm-estimate lm)
  (define elm (list (car lm)))
  (define (lm-record-new-bigram lm list1 freq)
    (let ((record (assoc list1 (cdr lm))))
     (if record
          '()
          (begin
            (set-cdr! lm 
                      (cons (cons list1 1) (cdr lm)))
            (let ((record (assoc list1 (cdr lm))))
              (if record
                  (set-cdr! record freq)))))))
  (define (estimate olm)
    (if (null? olm)
        elm
        (begin
          (lm-record-new-bigram elm (caar olm)
                                (/ (cdar olm)
                                   (lm-lookup-unigram lm (caaar olm))))
              (estimate (cdr olm)))))
  (estimate (cdr lm)))



;; 2(d)


(define (lm-score lm sentence)
  (define (check-sentence sentence prod)
    (if (null? sentence)
        prod
        (if (>  (length sentence) 1)
              (check-sentence (cdr sentence) (* prod (lm-lookup-bigram lm (car sentence) (cadr sentence))))
              prod)))
  (check-sentence sentence 1))

(define (check-all-score lm sentence-list)
  (define (check-all-score-1 lm-1 sentence-list-1)
    (if (not (null? sentence-list-1))
        (begin (display "\nScore: ") (display (exact->inexact (lm-score lm (car sentence-list-1))))
               (newline)
               (list-display (car sentence-list-1))
               (newline)
               (check-all-score-1 lm-1 (cdr sentence-list-1)))))
    (check-all-score-1 lm sentence-list))

(define (list-display lis)
          (if (null? lis)
              #f
              (begin (display #\space)(display (car lis))
                     (list-display (cdr lis)))))

;; Minst sannsynlig variant er: Score: 1.0342505814376122e-056
;;  <s> As the case was approached by the court, unfair, fundamentally illegal evidence was dismissed by it. </s>

;; Mest sannsynlig variant er: Score: 9.522954572388729e-058
;;  <s> Fundamentally illegal, unfair evidence was dismissed by it, as the case was approached by the court. </s>


;; MASKINLÆRING TEST

(define corpus-brown (read-corpus "brown.txt"))

(display "\nb) === lm-train! ===\n\n")

(define lm-brown (make-lm))

(lm-train! lm-brown corpus-brown)


(display "lookup unigram jury ")
(lm-lookup-unigram lm-brown "jury") 
;; -> 21

(display "\nlookup bigram jury said: ")
(lm-lookup-bigram lm-brown "jury" "said")
;; -> 3

(display "\nlookup unigram the: ")
(lm-lookup-unigram lm-brown "the") 
;; -> 1596

(display "\nlookup bigram the jury: ")
(lm-lookup-bigram lm-brown "the" "jury")
;; -> 9

;; Også "pseudoordet" <s> må bli talt opp.
(display "\nlookup unigram <s>: ")
(lm-lookup-unigram lm-brown "<s>")
;; -> 935 (fordi det er 935 setninger)


(display "\nc) === lm-estimate! ===\n")


(define new-model (lm-estimate lm-brown))

(display "\nlookup bigram jury said: ")
(lm-lookup-bigram new-model "jury" "said")
;; -> 1/7 (sanns. for at "said" kommer etter "jury")

(display "\nlookup bigram the jury: ")
(lm-lookup-bigram new-model "the" "jury")
;; -> 3/532 (sanns. for at "jury" kommer etter "the")

(display "\nlookup bigram <s> The: ")
(lm-lookup-bigram new-model "<s>" "The")
;; -> 35/187 (sanns. for at "The" er første ord i en setning)

(display "\nlookup bigram <s> A: ")
(lm-lookup-bigram new-model "<s>" "A")
;; -> 27/935 (sannsynligheten for at "A" er første ord i en setning)

(display "\nlookup unigram Moritz,: ")
(lm-lookup-unigram new-model "Moritz,")
;; -> 3

(display "\nlookup bigram <s> Moritz,: ")
(lm-lookup-bigram new-model "<s>" "Moritz,")
;; -> 1/935 (sannsynligheten for at "Moritz," er første ord i en setning)

;; d)
(display "\nd) === lm-score ===\n")

(define test-snts (read-corpus "test.txt"))

;; Score-verdiene her er basert på at ordpar som ikke forekommer i korpuset gis
;; en sannsynlighet på 1/N der N er antall ord totalt i modellen, som diskutert
;; i oblig-teksten. (Denne verdien er her 1/24067). Hvis man ikke gjør dette vil
;; alle disse score-verdiene bli 0.

(display "\nScore of 1. test sentence: ")
(define s (lm-score new-model (car test-snts)))
s (display "= ") (exact->inexact s)
;; -> 1.236880904485992e-046

(display "\nScore of 2. test sentence: ")
(set! s (lm-score new-model (cadr test-snts)))
s (display "= ") (exact->inexact s)
;; -> 5.139323158208302e-051

(display "\nScore of sentence \"The jury is out.\": ")
(set! s (lm-score new-model '("<s>" "The" "jury" "is" "out." "</s>")))
s (display "= ") (exact->inexact s)
;; -> 1.1423125094390425e-09

(check-all-score new-model test-snts)

(display "\nScore of sentence \"That's impossible.\": ")
(set! s (lm-score new-model '("<s>" "That's" "impossible." "</s>")))
s (display "= ") (exact->inexact s)
;; -> 1.8464794269001422e-012

(display "\nScore of sentence \"It was a beautiful day.\": ")
(set! s (lm-score new-model '("<s>" "It" "was" "a" "beautiful" "day." "</s>")))
s (display "= ") (exact->inexact s)
;; -> 1.213753975077391e-012


;; 2(e)
  (define (leaf-name node) (car node))
  (define (frequency node) (cadr node))
  (define (sub-tree node) (caddr node))
  (define (left-br node) (cadddr node))
  (define (right-br node) (cddddr node))
  
(define (lm-lookup-unigram-tree lm string)
  (define (find-node node name)
    (cond ((null? node) (/ 1 (car lm)))
          ((eq? name (leaf-name node)) node)
          ((string<=? name (leaf-name node)) (find-node (cdr (left-br node)) name))
          ((string>=? name (leaf-name node)) (find-node (cdr (right-br node)) name))))
  (define (lm-lookup-unigram-1 lm-1 string1)
    (if (null? lm-1)
        (/ 1 (car lm))
        (let ((record (find-node lm-1 string1))) ;sjekk for string1 
          (frequency record))))
  (lm-lookup-unigram-1 (cdr lm) string))

(define (lm-lookup-bigram-tree lm string1 string2)
  (define (find-node node name)
    (cond ((null? node) (/ 1 (car lm)))
          ((eq? name (leaf-name node)) node)
          ((string<=? name (leaf-name node)) (find-node (cdr (left-br node)) name))
          ((string>=? name (leaf-name node)) (find-node (cdr (right-br node)) name))))

  (define (lm-lookup-bigram-1 lm-1 string1 string2)
    (if (null? lm-1)
        (/ 1 (car lm))
    (let ((record (find-node lm-1 string1))) ;sjekk for string1 
      (begin
        (let ((record2 (find-node (car (sub-tree record)) string2)))
          (cadr record2))))))
  (lm-lookup-bigram-1 (cdr lm) string1 string2))

(define (lm-record-bigram-tree! lm string1 string2)
  (define (update-word-count) (set-car! lm (+ (car lm) 1)))
  (define (add-leaf node name) (set-cdr! node (cons node (list name 0 '(*sub-tree*) (list '*left*) (list '*right*)))))
  (define (update-freq node) (set-car! (cdr node) (+ (cadr node) 1)))

  (define (find-node node name)
    (display name)
    (cond ((eq? '() node) (begin (add-leaf node name) (find-node node name)))
          ((eq? name (leaf-name node)) node)
          ((string<=? name (leaf-name node))
           (if (eq? (car (left-br node)) '*left*)
               (begin 
                 (add-leaf node name)
                 (find-node ((left-br node)) name))
               (find-node ((left-br node)) name))
          ((string>=? name (leaf-name node))
           (if (eq? (car (right-br node)) '*right*)
               (begin 
                 (add-leaf node name)
                 (find-node (cdr (right-br node)) name))
               (find-node (cdr (right-br node)) name))))))

  (define (lm-record-bigram-1 tree string1 string2)
    (display tree)
    (let ((record (find-node tree string1))) ;sjekk for string1 
      (begin
        (display record)
        (update-freq record) ;oppdaterer frekvensen til string1
        (if (eq? (car (sub-tree record)) '*sub-tree*)
            (begin
              (set-car! (sub-tree record) (list string2 0 '(*sub-tree*) (list '*left*) (list '*right*)))
              (let ((record2 (find-node (car (sub-tree record)) string2)))
                (update-freq record2)))
            (let ((record2 (find-node (sub-tree record) string2))) ;sjekk for string2 i subtre til string1
              (update-freq record2)))))) ;oppdaterer frekvensen til string2 i subtre til string1
  (update-word-count)
  (if (eq? '() (cdr lm))
      (set-cdr! lm (list string1 0 (list '*sub-tree*) (list '*left*) (list '*right*))))
  (lm-record-bigram-1 (cdr lm) string1 string2))
