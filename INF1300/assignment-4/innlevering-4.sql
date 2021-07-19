--1)
SELECT id
COUNT (kode) as p
FROM Prosjektleder
GROUP BY id HAVING p>1;

--2)
SELECT id
FROM Prosjektleder AS p, Deltakelse AS d,
WHERE p.id=d.id AND p.kode!=p.kode;

--3)
SELECT a.id
FROM Timeansatt AS  t
JOIN Ansatt AS a ON (t.id = a.id)
JOIN Lønnstrinn AS l ON (a.nr = l.nr)
WHERE l.nr = (SELECT max(l.nr) FROM Lønnstrinn AS l);

--4.1)
SELECT avg(l.månedslønn)
FROM Lønnstrinn l
JOIN Ansatt AS a ON (l.nr = a.nr)
JOIN Fulltidsansatt AS f ON (a.id = f.id);

--4.2-4.3)
SELECT avg(l.månedslønn)
FROM Lønnstrinn AS l
JOIN Ansatt AS a ON (l.nr = a.nr)
JOIN Deltidsansatt AS d ON (a.id = d.id)
GROUP BY d.timerperuke
ORDER BY d.timerperuke DESC;
