--Oppgave 1
psql -h dbpg-ifi-kurs -U username
psql (8.4.20, server 9.4.8)
WARNING: psql version 8.4, server version 9.4.
Some psql features might not work.
SSL connection (cipher: ECDHE-RSA-AES256-GCM-SHA384, bits: 256)


\i timelistedb.sql
psql:timelistedb.sql:1: NOTICE:  view "timeantall" does not exist, skipping
DROP VIEW
psql:timelistedb.sql:2: NOTICE:  view "varighet" does not exist, skipping
DROP VIEW
psql:timelistedb.sql:3: NOTICE:  table "timelistelinje" does not exist, skipping
DROP TABLE
psql:timelistedb.sql:4: NOTICE:  table "timeliste" does not exist, skipping
DROP TABLE
CREATE TABLE
CREATE TABLE
CREATE VIEW
CREATE VIEW


--1.Hvilke timelistelinjer som er lagt inn for timeliste nr 3

SELECT * FROM Timelistelinje WHERE timelistenr = 3;
timelistenr | linjenr | startdato  | starttid | sluttid  | pause | beskrivelse
-------------+---------+------------+----------+----------+-------+-------------
3 |       1 | 2016-07-01 | 15:00:00 | 16:00:00 |       | Test 1
3 |       2 | 2016-07-04 | 13:15:00 | 17:00:00 |    40 | Test 2
3 |       3 | 2016-07-04 | 22:00:00 | 01:00:00 |    30 | Test 3
3 |       4 | 2016-07-05 | 14:00:00 | 18:00:00 |       | Test 4
3 |       5 | 2016-07-06 | 10:00:00 | 16:50:00 |    55 | Test 5
3 |       6 | 2016-07-07 | 10:00:00 | 12:00:00 |       | Test 6
3 |       7 | 2016-07-07 | 15:00:00 | 18:00:00 |    20 | Test 7
3 |       8 | 2016-07-08 | 13:00:00 | 13:50:00 |       | Test 8
3 |       9 | 2016-07-09 | 22:00:00 | 03:00:00 |    25 | Retesting


--2. Hvor mange timelister det er

SELECT distinct timelistenr FROM Timeliste;
timelistenr
-------------
2
5
6
4
1
3
7
(7 rows)

SELECT distinct COUNT(timelistenr) FROM Timeliste;
count
-------
7
(1 row)

--3. Hvor mange timelister som det ikke er utbetalt penger for

SELECT distinct timelistenr FROM Timeliste WHERE status!='utbetalt';
timelistenr
-------------
6
4
(2 rows)


SELECT distinct COUNT(timelistenr) FROM Timeliste WHERE status!='utbetalt';
count
-------
2
(1 row)

--4. ved hvor mange tilfeller klokka passerte midnatt mens vedkommende var på jobb

SELECT COUNT(*)
  FROM Timelistelinje
  JOIN Timeliste ON (Timeliste.timelistenr=Timelistelinje.timelistenr)
  WHERE Timelistelinje.sluttid < Timelistelinje.starttid;
count
-------
4
(1 row)



--5. Antall timer som det ikke er utbetalt penger for (uten pause)


SELECT SUM((time'24:00:00' - Timelistelinje.starttid) +  Timelistelinje.sluttid) - SUM(((Timelistelinje.pause*60)::text)::interval)
  FROM Timelistelinje
  JOIN Timeliste ON (Timeliste.timelistenr=Timelistelinje.timelistenr)
  WHERE status != 'utbetalt';
?column?
----------
11:45:00
(1 row)


--6. Hvor mange timer det totalt ble jobbet i juli

--med pause

SELECT SUM((time'24:00:00' - Timelistelinje.starttid) +  Timelistelinje.sluttid) --- SUM(((Timelistelinje.pause*60)::text)::interval)
  FROM Timelistelinje
  JOIN Timeliste ON (Timeliste.timelistenr=Timelistelinje.timelistenr)
  WHERE EXTRACT(MONTH FROM startdato) = 07;
sum
----------
78:05:00
(1 row)

--uten pause


SELECT SUM((time'24:00:00' - Timelistelinje.starttid) +  Timelistelinje.sluttid) - SUM(((Timelistelinje.pause*60)::text)::interval)
  FROM Timelistelinje
  JOIN Timeliste ON (Timeliste.timelistenr=Timelistelinje.timelistenr)
  WHERE EXTRACT(MONTH FROM startdato) = 07;
?column?
----------
71:45:00
(1 row)

--Oppgave 3. Legg inn data om følgende ved hjelp av SQL insert-setninger

INSERT INTO Timeliste (timelistenr, status, levert, utbetalt, beskrivelse)
  VALUES (8, 'utbetalt', '2016-07-29', '2016-08-10' ,'Planlegging av neste trinn');
INSERT INTO Timeliste (timelistenr, status, levert, utbetalt, beskrivelse)
  VALUES (9, 'levert', '2016-08-03', NULL ,'Detaljering av neste trinn');
INSERT INTO Timeliste (timelistenr, status, levert, utbetalt, beskrivelse)
  VALUES (10, 'aktiv', NULL, NULL ,'Skriving av rapport');


INSERT INTO Timelistelinje (timelistenr, linjenr, startdato, starttid, sluttid,pause,beskrivelse)
  VALUES (8, 1, '2016-07-25', '10:15' ,'17:30',50,'diskusjoner')
  VALUES (8, 2, '2016-07-27', '12:45' ,'14:00',NULL,'konkretisering')
  VALUES (9, 1, '2016-07-27', '15:15' ,'18:45',70,'del1')
  VALUES (9, 2, '2016-07-28', '10:00' ,'14:00',35,'del2')
  VALUES (9, 3, '2016-07-28', '21:00' ,'04:15',90,'del3')
  VALUES (9, 4, '2016-08-02', '13:00' ,'17:00',NULL,'del4')
  VALUES (10, 1, '2016-08-03', '10:50' ,'16:10',40,'kap1')
  VALUES (10, 2, '2016-08-05', '18:00' ,NULL,NULL,'kap2');

--Oppgave 4. Lag SQL-spørringer som skriver ut de dataene du la inn i oppgave 3

SELECT *
  FROM Timeliste WHERE timelistenr = 8 OR timelistenr = 9 OR timelistenr = 10;

timelistenr |  status  |   levert   |  utbetalt  |        beskrivelse
-------------+----------+------------+------------+----------------------------
          8 | utbetalt | 2016-07-29 | 2016-08-10 | Planlegging av neste trinn
          9 | levert   | 2016-08-03 |            | Detaljering av neste trinn
         10 | aktiv    |            |            | Skriving av rapport
(3 rows)

SELECT *
  FROM Timelistelinje WHERE timelistenr = 8 OR timelistenr = 9 OR timelistenr = 10;

  timelistenr | linjenr | startdato  | starttid | sluttid  | pause |  beskrivelse
 -------------+---------+------------+----------+----------+-------+----------------
            8 |       1 | 2016-07-25 | 10:15:00 | 17:30:00 |    50 | diskusjoner
            8 |       2 | 2016-07-27 | 12:45:00 | 14:00:00 |       | konkretisering
            9 |       1 | 2016-07-27 | 15:15:00 | 18:45:00 |    70 | del1
            9 |       2 | 2016-07-28 | 10:00:00 | 14:00:00 |    35 | del2
            9 |       3 | 2016-07-28 | 21:00:00 | 04:15:00 |    90 | del3
            9 |       4 | 2016-08-02 | 13:00:00 | 17:00:00 |       | del4
           10 |       1 | 2016-08-03 | 10:50:00 | 16:10:00 |    40 | kap1
           10 |       2 | 2016-08-05 | 18:00:00 |          |       | kap2
 (8 rows)
