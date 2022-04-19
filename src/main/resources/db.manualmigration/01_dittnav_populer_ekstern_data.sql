-- Forventer ca 5500 rader
update dittnavoppgaver o
set EKSTERN_REFERANSE_ID = o.REFERANSE_ID,
    INTERN_REFERANSE_ID  = i.REFERANSE_ID,
    GRUPPERINGS_ID       = i.saksnr,
    TYPE                 = 'OPPGAVE'
from innsending i
where concat('O', i.referanse_id) = o.referanse_id;

-- Forventer ca 500 rader
update dittnavoppgaver
set EKSTERN_REFERANSE_ID = i.REFERANSE_ID, -- C-prefixede oppg er sendt med ren uuid
    INTERN_REFERANSE_ID  = i.REFERANSE_ID,
    GRUPPERINGS_ID       = i.saksnr,
    TYPE                 = 'OPPGAVE'
from innsending i
where concat('C', i.referanse_id) = o.referanse_id;

-- Forventer ca 300 000 rader
update dittnavoppgaver o
set EKSTERN_REFERANSE_ID = o.REFERANSE_ID,
    INTERN_REFERANSE_ID  = i.REFERANSE_ID,
    GRUPPERINGS_ID       = i.saksnr,
    TYPE                 = 'BESKJED'
from innsending i
where concat('B', i.referanse_id) = o.referanse_id;
