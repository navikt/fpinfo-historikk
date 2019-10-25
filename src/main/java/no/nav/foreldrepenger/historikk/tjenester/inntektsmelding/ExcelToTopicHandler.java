package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Versjon;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public class ExcelToTopicHandler {

    private static final Versjon VERSJON = Versjon.valueOf("1.0");
    private static final Logger LOG = LoggerFactory.getLogger(ExcelToTopicHandler.class);
    private final InntektsmeldingHendelseProdusent produsent;
    private final XSSFSheet sheet;

    public ExcelToTopicHandler(InntektsmeldingHendelseProdusent produsent, Resource excel) {
        this.produsent = produsent;
        this.sheet = sheetFra(excel);
    }

    public void importer() {
        if (sheet != null) {
            Iterator<Row> rows = sheet.iterator();
            rows.next();
            while (rows.hasNext()) {
                InntektsmeldingHendelse hendelse = hendelseFra(rows.next());
                if (hendelse != null) {
                    produsent.send(hendelse);
                }
            }
        }
    }

    private XSSFSheet sheetFra(Resource resource) {
        try {
            return new XSSFWorkbook(resource.getInputStream()).getSheetAt(0);
        } catch (IOException e) {
            LOG.warn("Kunne ikke lese resource", resource);
            return null;
        }
    }

    private static InntektsmeldingHendelse hendelseFra(Row row) {
        try {
            int i = 0;
            String referanseId = nextCell(row, i++).getStringCellValue();
            String journalpostId = stringFrom(nextCell(row, i++));
            AktørId aktørId = AktørId.valueOf(stringFrom(nextCell(row, i++)));
            String saksnr = stringFrom(nextCell(row, i++));
            LocalDate startDato = localdateFrom(nextCell(row, i++));
            String arbeidsgiver = stringFrom(nextCell(row, i++));
            HendelseType type = typeFra(nextCell(row, i++));
            LocalDateTime innsendingsTidspunkt = localDateTimeFrom(nextCell(row, i++));
            return new InntektsmeldingHendelse(aktørId, journalpostId, saksnr, referanseId, type,
                    innsendingsTidspunkt, arbeidsgiver, VERSJON, startDato);
        } catch (Exception e) {
            LOG.warn("feil i rad {}, ignorerer", row.getRowNum());
            return null;
        }
    }

    private static Cell nextCell(Row row, int i) {
        return row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    private static HendelseType typeFra(Cell cell) {
        try {
            return HendelseType.valueOf(cell.getStringCellValue());
        } catch (Exception e) {
            LOG.warn("{} har feil" + "({},{})", cell, cell.getRowIndex(), cell.getColumnIndex());
            return null;
        }
    }

    private static String stringFrom(Cell cell) {
        try {
            return new BigDecimal(cell.getNumericCellValue()).toPlainString();
        } catch (Exception e) {
            LOG.warn("{} har feil" + "({},{})", cell, cell.getRowIndex(), cell.getColumnIndex());
            return null;
        }
    }

    public static LocalDateTime localDateTimeFrom(Cell cell) {
        try {
            if (cell.getCellType().equals(CellType.BLANK)) {
                return null;
            }
            var dateToConvert = cell.getDateCellValue();
            if (dateToConvert == null)
                return null;
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            LOG.warn("{} har feil" + "({},{})", cell, cell.getRowIndex(), cell.getColumnIndex());
            return null;
        }
    }

    public static LocalDate localdateFrom(Cell cell) {
        if (cell.getCellType().equals(CellType.BLANK)) {
            return null;
        }
        try {
            var dateToConvert = cell.getDateCellValue();
            if (dateToConvert == null)
                return null;
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (Exception e) {
            LOG.warn("{} har feil" + "({},{})", cell, cell.getRowIndex(), cell.getColumnIndex());
            return null;
        }
    }
}
