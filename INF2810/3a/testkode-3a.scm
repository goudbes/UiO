(load "code.scm") ;; Skriv filnavn for løsningen deres her!
;; (Hvis ikke prekoden lastes i deres løsning, last inn "prekode3a.scm" også.)

(define corpus-brown (read-corpus "brown.txt"))

;; b)
(display "\nb) === lm-train! ===\n\n")

(define lm-brown (make-lm))



(lm-train! lm-brown corpus-brown)
(car lm-brown)

(newline)
;; Det kan være lurt å implementere en prosedyre lm-lookup-unigram for å slå
;; opp hvor mange ganger et ord forekommer som første ord i et ordpar
;; (som er det samme som hvor mange ganger ordet forekommer totalt).

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

;; c)
(display "\nc) === lm-estimate! ===\n")

;; Forutsier at lm-estimate! destruktiv endrer modellen
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

;; e)
(display "\ne) === Training with wsj-corpus ===\n")

;; Litt oppsett for å svare på spørsmålet i oppgave e).

(define corpus-wsj (read-corpus "wsj.txt"))

(define lm-wsj (make-lm))
(lm-train! lm-wsj corpus-wsj)   ; Dette vil nok ta veldig lang tid om man ikke
;; implementerer tabellene i modellen med binære søketrær for raskere oppslag.
;; (Det er en del av denne oppgaven å gjøre det.)
(lm-estimate! lm-wsj)

;; Her kan dere sjekke hvordan test-setningene skårer i brown-modellen og
;; wsj-modellen hver for seg.

(define corpus-combined (append corpus-brown corpus-wsj))

(define lm-combined (make-lm))
(lm-train! lm-combined corpus-combined)
(lm-estimate! lm-combined)

;; Her kan dere sjekke hvordan test-setningene skårer i den kombinerte modellen.
