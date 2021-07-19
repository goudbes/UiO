-- 1

SELECT t.filmcharacter, COUNT(t.partid) AS antall_forekomster
FROM filmcharacter AS t
GROUP BY t.filmcharacter
HAVING COUNT(t.partid) > 800
ORDER BY COUNT(t.partid) DESC;

--2

SELECT p.personid AS id, p.lastname AS ln, f.title AS title,
fc.country as country
FROM Person AS p
JOIN Filmparticipation AS fp ON (p.personid = fp.personid)
JOIN Filmcharacter AS flc ON (fp.partid = flc.partid)
JOIN Film AS f ON (fp.filmid = f.filmid)
JOIN Filmcountry AS fc ON (fp.filmid = fc.filmid)
WHERE p.firstname = 'Ingrid' AND flc.filmcharacter = 'Ingrid';

--3

SELECT p.firstname AS firstname, p.lastname as last_name, fc.filmcharacter as rollenavn
FROM Person AS p
JOIN Filmparticipation AS fp ON (p.personid = fp.personid)
JOIN Filmcharacter AS fc ON (fp.partid = fc.partid)
JOIN Film as f ON (fp.filmid = f.filmid)
WHERE p.personid = 3914169 AND fc.filmcharacter IS NOT NULL;

--4

SELECT p.firstname AS firstname, p.lastname AS last_name, COUNT(flc.filmcharacter)
FROM Person AS p
JOIN Filmparticipation AS fp ON (p.personid = fp.personid)
JOIN Filmcharacter AS flc ON (fp.partid = flc.partid)
WHERE flc.filmcharacter = 'Ingrid'
GROUP BY firstname, last_name
ORDER BY COUNT(flc.filmcharacter) DESC
LIMIT 1;

--5

SELECT fc.filmcharacter AS film_character, COUNT(fc.filmcharacter)
FROM Filmcharacter AS fc
JOIN Filmparticipation AS fp ON (fc.partid = fp.partid)
JOIN Person AS p ON (fp.personid = p.personid)
WHERE fc.filmcharacter = p.firstname AND fp.parttype = 'cast'
GROUP BY fc.filmcharacter
ORDER BY COUNT(fc.filmcharacter) DESC
LIMIT 1;

--6

SELECT f.title AS title, fp.parttype AS parttype, COUNT(fp.parttype) AS number_of_participants
FROM Filmparticipation AS fp
JOIN Film as f ON (fp.filmid = f.filmid)
JOIN Filmitem as fi ON (f.filmid = fi.filmid)
WHERE f.title LIKE '%Lord of the Rings%' AND fi.filmtype = 'C'
GROUP BY f.title, fp.parttype
ORDER BY f.title;

--7

SELECT f.title AS title
FROM Film AS f
JOIN Filmgenre AS g1 ON (f.filmid = g1.filmid AND g1.genre = 'Comedy')
JOIN Filmgenre AS g2 ON (f.filmid = g2.filmid AND g2.genre = 'Film-Noir');

--8 Jeg forstår ikke hvordan dere klarte å få 23 rader i stedet for 13 :) Kan du forklare?

SELECT p.firstname AS first_name, p.lastname AS last_name, COUNT(fp.partid)
FROM Person AS p
JOIN Filmparticipation AS fp ON (p.personid = fp.personid)
JOIN Filmcharacter AS fc ON (fp.partid = fc.partid)
INNER JOIN (SELECT fc.filmcharacter FROM Filmcharacter AS fc GROUP BY fc.filmcharacter HAVING COUNT(fc.filmcharacter) =1) AS fc2
ON fc.filmcharacter = fc2.filmcharacter
GROUP BY p.lastname, p.firstname
HAVING COUNT(fc.filmcharacter) > 199;
