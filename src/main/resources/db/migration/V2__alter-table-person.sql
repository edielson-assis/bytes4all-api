ALTER TABLE person DROP COLUMN street;
ALTER TABLE person DROP COLUMN neighborhood;
ALTER TABLE person MODIFY state CHAR(2) NOT NULL;