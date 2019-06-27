CREATE TABLE HISTORIKK (
    id  SERIAL PRIMARY KEY,
    aktør_id VARCHAR (50),
    fnr VARCHAR (50),
    journalpost_id VARCHAR(50),
    saksnr VARCHAR(50),
    tekst VARCHAR (2000)  NOT NULL,
    opprettet timestamp NOT NULL);