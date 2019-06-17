CREATE TABLE MELDING (
    id  SERIAL PRIMARY KEY,
    aktør_id VARCHAR (50)  NOT NULL,
    melding VARCHAR (2000)  NOT NULL,
    dato DATE NOT NULL DEFAULT CURRENT_DATE,
    gyldig_til DATE,
    saksnr VARCHAR(50) NOT NULL,
    kanal VARCHAR(50) NOT NULL,
    aktiv boolean);