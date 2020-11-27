# takk til perl guru kjell.arne.rekaa@gmail.com for dette likke hacket 
perl -F, -ane "\$F[0]=~s/^\s+//;print \"insert into dittnavoppgaver(fnr,referanseid) values('\$F[0]','\$F[1]');\n\"" historikk.dat
