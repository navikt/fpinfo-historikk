ALTER TABLE INNSENDING ADD innsendt timestamp;
ALTER TABLE MINIDIALOG ADD innsendt timestamp;
ALTER TABLE INNTEKTSMELDING RENAME  innsendingstidspunkt TO innsendt;