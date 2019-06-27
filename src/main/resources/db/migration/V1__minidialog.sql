CREATE TABLE MINIDIALOG (
    id  SERIAL PRIMARY KEY,
    aktør_id VARCHAR (50),
    fnr VARCHAR (50),
    janei boolean default false,
    vedlegg VARCHAR (2000),
    tekst VARCHAR (2000)  NOT NULL,
    gyldig_til DATE,
    handling VARCHAR (50),
    saksnr VARCHAR(50) NOT NULL,
    opprettet timestamp NOT NULL,
    endret timestamp NOT NULL,
    aktiv boolean);