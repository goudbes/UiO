create table Plantesort (
  sortid int primary key,
  latinsk_navn varchar(40) not null,
  norsk_navn varchar(40) not null,
  --unique (latinsk_navn, norsk_navn)
);

create table Farge(
  sortid int references Plantesort(sortid),
  farge varchar(10),
  primary key(sortid,farge)
);

create table Lysforhold (
  sortid int references Plantesort(sortid),
  lys varchar(15) not null
      check (lys = 'sol' or lys = 'halvskygge' or lys = 'skygge'),
  primary key (sortid,lys)
);

create table Vare(
  varenr int primary key,
  sortid int references Plantesort(sortid),
  FL varchar(10) not null
        check (FL = 'frø' OR FL = 'løk'),
  mengde int not null,
  pl_fra integer not null
        check (pl_fra between 1 and 12),
  pl_til integer
        check (pl_fra between 1 and 12),
  pl_dybde integer,
  blomstrer integer not null
        check (blomstrer between 1 and 12)
);

create table Pris(
  varenr int not null references Vare(varenr),
  antall int not null,
  enhetspris int not null,
  primary key (varenr,antall)
);

--2a

SELECT v.varenr
FROM   Vare AS v
WHERE v.pl_fra > blomstrer;

--2b

SELECT COUNT(v.varenr)
FROM Vare AS v
WHERE v.FL = 'løk' AND v.pl_dybde is NULL
GROUP BY v.varenr;

--2c

SELECT p.sortid, p.latinsk_navn, p.norsk_navn
FROM Plantesort AS p
JOIN Vare AS v1 ON (p.sortid = v1.sortid AND v1.FL='løk')
JOIN Vare AS v2 ON (p.sortid = v2.sortid AND v2.FL='frø')
GROUP BY p.sortid;

--2d

SELECT p.sortid, p.norsk_navn
FROM Plantesort AS p
JOIN Lysforhold AS l ON (p.sortid = l.sortid AND l.lys='sol')
JOIN Lysforhold AS l1 ON (p.sortid = l1.sortid AND l1.lys = 'skygge')
JOIN Lysforhold AS l2 ON (p.sortid = l2.sortid AND l2.lys!='halvskygge')
GROUP BY p.norsk_navn
ORDER BY p.norsk_navn;

--2e
SELECT p.latinsk_navn, v.varenr
FROM Plantesort AS p,
JOIN Vare AS v ON (p.sortid = v.sortid)
JOIN Pris AS pr ON (v.varenr = pr.varenr)
WHERE pr.enhetspris = (SELECT MAX(pr.enhetspris)
                       FROM Pris AS pr
                       WHERE pr.antall = 1
                       )
GROUP BY p.latinsk_navn,v.varenr,
ORDER BY p.latinsk_navn;

--2f

SELECT p.varenr, COUNT(p.antall)
FROM Pris AS p
GROUP BY p.varenr
ORDER BY COUNT(p.antall) DESC
LIMIT 1;

--2g

SELECT p.enhetspris*220 AS persillefrø_pris_220
FROM Price AS p
WHERE p.varenr = '42'
ORDER BY p.enhetspris
LIMIT 1;

-- Oppgave 3
Vare(varenr,sortid, FL, mengde, pl_fra,[pl_til],[pl_dybde], blomstrer)
Funksjonelle avhengigheter i tabellen  Vare:
varenr -> sortid, FL, mengde, pl_fra,[pl_til],[pl_dybde]
sortid -> blomstrer

Vare fulfills 2NF because all non-key attributes are functionally
dependent on primary key varenr.
It doesnt fullfill 3NF, because non-key attribute blomstrer is functionally
dependent on another non-key attribute sortid.
