CREATE TABLE HISTORIKK (
    id  SERIAL PRIMARY KEY,
    aktør_id VARCHAR (50)  NOT NULL,
    journalpost_id VARCHAR(50),
    saksnr VARCHAR(50),
    tekst VARCHAR (2000)  NOT NULL,
    dato_mottatt timestamp NOT NULL DEFAULT now());