CREATE TABLE INNTEKTSMELDING (
    id  SERIAL PRIMARY KEY,
    aktør_id VARCHAR (50),
    fnr VARCHAR (50),
    journalpost_id VARCHAR(50),
    saksnr VARCHAR(50),
    orgnr varchar(10),
    navn varchar(200),
    opprettet timestamp NOT NULL);