CREATE TABLE DITTNAVOPPGAVER (
    id  SERIAL PRIMARY KEY,
    fnr VARCHAR(11),
    saksnr VARCHAR(20),
    opprettet timestamp NOT NULL,
    referanse_id VARCHAR(50));