(load "huffman.scm")

;;Innlevering 2a i inf2810
;;1 Diverse


;;a
'(Oppgave 1a)
(define (p-cons x y)
  (lambda (proc) (proc x y)))
    
(define (p-car proc)  (proc (lambda (p q) p)))
(define (p-cdr proc) (proc (lambda (p q) q)))

;;Test

(p-cons "foo" "bar")
(p-car (p-cons "foo" "bar"))
(p-cdr (p-cons "foo" "bar"))
(p-car (p-cdr (p-cons "zoo" (p-cons "foo" "bar"))))

;;b
'(Oppgave 1b)

(define foo 42)


((lambda(x y)
  (if (= x y)
      'same
      'different))4 5)


((lambda (x y)
 (list y (list x y))) 42 'towel)

;;c
'(Oppgave 1c)

(define (infix-eval items)
  ((car (cdr items)) (car items) (car(cdr(cdr items)))))

(define foo (list 21 + 21))
(define baz (list 21 list 21))
(define bar (list 84 / 2))



(infix-eval foo)
(infix-eval baz)
(infix-eval bar)

;Oppgave 1d)

;(define bah '(84 / 2))
;(infix-eval bah)
;evalueres som en spes. form, ikke som en liste

;; 2 Huffman-koding

;;a
'(Oppgave 2a)

(define (member? proc likhets-pred items)
  (if (null? items)
      #f
      (if (proc likhets-pred (car items))
          #t
          (member? proc likhets-pred (cdr items)))))

(member? eq? 'zoo '(bar foo zap))
(member? eq? 'foo '(bar foo zap))
;;(member? = 'foo '(bar foo zap))
(member? = 1 '(3 2 1 0))
(member? eq? '(1 bar)
         '(( 0 foo) (1 bar) (2 baz)))
(member? equal? '(1 bar)
         '((0 foo) (1 bar) (2 baz)))

;; 2 b
; Decode 1 trengs fordi vi må kunne gå tilbake til roten av treet, ikke bare ut til løvnodene

;; 2 c
;'(Oppgave 2c)


;;;
;;; Dekoding:
;;;
;;; Bygger opp svaret og reverse det på slutten

(define (decode-3 bits tree)
  (define (decode-1 bits current-branch answer)
    (if (null? bits)
        (reverse answer)
        (let ((next-branch
               (choose-branch (car bits) current-branch)))
          (if (leaf? next-branch)
              (decode-1 (cdr bits) tree (cons (symbol-leaf next-branch) answer))
              (decode-1 (cdr bits) next-branch answer)))))
  (decode-1 bits tree '()))


;; 2 d
'(Oppgave 2d)
(decode-3 sample-code sample-tree)

;;(ninjas fight ninjas by night)

;; 2 e
'(Oppgave 2e)

(define (encode message codebook)
  (define (encode-1 message tree)
    (if (null? message)
        '()
        (if (equal?  (car message) (symbol-leaf (left-branch tree)))
            (cons 0 (encode-1 (cdr message) codebook))
            (if (equal? (car message) (symbol-leaf (right-branch tree)))
                (cons 1 (encode-1 (cdr message) codebook))
                (cons 1 (encode-1 message (right-branch tree)))))))
  (encode-1 message codebook))

;(encode '(ninjas fight ninjas by night) sample-tree)
(decode (encode '(ninjas fight ninjas) sample-tree) sample-tree)


;; 2 f
;;Sjekker om listen er tom, hvis det finnes bare en pair, returnerer den.
;;Ellers lager et tre av to første pairs og sorterer den inn til resten av listen.

'(Oppgave 2f)

 (define (grow-huffman-tree list)
   (define (grow-huffman-tree-1 list)
     (if (null? list)
         '()
         (if (null? (cdr list))
             (car list)
             (grow-huffman-tree-1 (cons
                                  (make-code-tree (cadr list)(car list))
                                  (cdr(cdr list)))))))
   (grow-huffman-tree-1 (make-leaf-set list)))

 (define freqs '((a 2) (b 5) (c 1) (d 3) (e 1) (f 3)))
 (define codebook (grow-huffman-tree freqs))
 (decode (encode '(a b c) codebook) codebook)

;Oppgave 2g
'(Oppgave 2g)

(define opg2g '((samurais 57) (ninjas 20) (fight 45) (night 12)
                              (hide 3) (in 2) (ambush 2) (defeat 1) (the 5)
                              (sword 4) (by 12) (assassin 1) (river 2)
                              (forest 1) (wait 1) (poison 1)))
(define oppgaveg (grow-huffman-tree opg2g))
(encode '(ninjas fight ninjas fight ninjas
                 ninjas fight samurais samurais fight
                 samurais fight ninjas ninjas fight by night) oppgaveg)
;(decode '(1 1 0 1 0 1 1 0 1 0 1 1 0 1 1 0 1 0 0 0 1 0 0 1 0 1 1 0 1 1 0 1 0 1 1 1 1 0 1 1 1 0) oppgaveg)
;42 bits brukes på å kode meldingen
;gjennomsnittslengde på de ordene i meldingen: 2,47
;hvis alle ordene i alfabetet skal ha samme lengde, trengs det 255 bits, da det lengste ordet trenger 15 bits

;; 2 h
'(Oppgave 2h)

(define (merge list-1 list-2)
  (if (null? list-1)
         list-2
         (if(null? list-2)
            list-1
            (cons (car list-1)
               (merge (cdr list-1) list-2)))))

       
(define (huffman-leaves tree)
  (define (huffman-leaves-1 branch)
    (if (null? branch)
        '()
        (if (leaf? branch)
            (list (cons (symbol-leaf branch) (cons (weight-leaf branch) '())))
            (merge (huffman-leaves-1 (left-branch branch))
                   (huffman-leaves-1 (right-branch branch))))))          
  (huffman-leaves-1 tree))

(huffman-leaves sample-tree)

;;2i
'(Oppgave 2i)

;;Hjelpemetode

(define (sum-weight list)
  (define (sum-weight-1 list-1 answer)
    (if (null? list-1)
        answer
        (sum-weight-1 (cdr list-1) (+ answer (car (cdr (car list-1)))))))
  (sum-weight-1 list 0))

(define (elem-length elem tree)
  (define (elem-length-1 elem answer)
    (if (null? tree)
        0
        (length (encode elem tree))))
  (elem-length-1 elem 0))

(define (expected-code-length tree)
  (define (expected-code-length-1 branch total-weight)
  (if (null? tree)
      answer
      (if (leaf? branch)
          (* (/ (weight-leaf branch) total-weight)
             (elem-length (cons (symbol-leaf branch) '()) tree))
          (+ (expected-code-length-1 (left-branch branch) total-weight)
             (expected-code-length-1 (right-branch branch) total-weight)))))
  (expected-code-length-1 tree (sum-weight (huffman-leaves tree))))

(expected-code-length sample-tree)
  











