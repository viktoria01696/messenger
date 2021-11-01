ALTER TABLE student
    ADD COLUMN login    varchar(100) UNIQUE,
    ADD COLUMN password varchar(100);