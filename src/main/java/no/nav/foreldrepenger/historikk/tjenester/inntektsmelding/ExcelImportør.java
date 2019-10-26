package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static java.util.stream.StreamSupport.stream;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Versjon;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Service
@ConditionalOnProperty(name = "import.enabled", havingValue = "true")
public class ExcelImportør {

    private static class UnexpectedCellException extends RuntimeException {

        public UnexpectedCellException(Cell cell, Exception e) {
            super("Feil i celle (" + cell.getRowIndex() + "," + cell.getColumnIndex() + ")", e);
        }

    }

    private static final Versjon VERSJON = Versjon.valueOf("1.0-IMPORT");
    private static final Logger LOG = LoggerFactory.getLogger(ExcelImportør.class);
    private final InntektsmeldingHendelseProdusent produsent;

    public ExcelImportør(InntektsmeldingHendelseProdusent produsent) {
        this.produsent = produsent;
    }

    public void importer(Resource res) throws IOException {
        if (!res.exists()) {
            throw new IllegalStateException("Resource " + res.getDescription() + " finnes ikke");
        }
        try (var wb = workbookFra(res)) {
            stream(((Iterable<Row>) () -> wb.getSheetAt(0).iterator()).spliterator(), true)
                    .skip(1)
                    .map(ExcelImportør::hendelseFra)
                    .filter((Objects::nonNull))
                    .forEach(produsent::send);
        } catch (IOException e) {
            throw new IllegalStateException("Resource " + res.getDescription() + " kan ikke leses", e);
        }
    }

    private XSSFWorkbook workbookFra(Resource res) {
        try {

            return new XSSFWorkbook(res.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static InntektsmeldingHendelse hendelseFra(Row row) {
        try {
            return new InntektsmeldingHendelse(
                    aktørId(row),
                    journalPost(row),
                    saksnr(row),
                    referanseId(row),
                    type(row),
                    innsendingsTidspunkt(row),
                    arbeidsgiver(row),
                    VERSJON,
                    startDato(row));
        } catch (UnexpectedCellException e) {
            LOG.warn("Feil i rad {}, ignorerer ({})", row.getRowNum(), e.getMessage());
            return null;
        }
    }

    private static String referanseId(Row row) {
        return fraTekst(celle(row, 0));
    }

    private static String journalPost(Row row) {
        return fraNumerisk(celle(row, 1));
    }

    private static AktørId aktørId(Row row) {
        return AktørId.valueOf(fraNumerisk(celle(row, 2)));
    }

    private static String saksnr(Row row) {
        return fraNumerisk(celle(row, 3));
    }

    private static LocalDate startDato(Row row) {
        return fraDato(celle(row, 4));
    }

    private static String arbeidsgiver(Row row) {
        return fraNumerisk(celle(row, 5));
    }

    private static HendelseType type(Row row) {
        return fraType(celle(row, 6));
    }

    private static LocalDateTime innsendingsTidspunkt(Row row) {
        return fraTid(celle(row, 7));
    }

    private static Cell celle(Row row, int i) {
        return row.getCell(i, CREATE_NULL_AS_BLANK);
    }

    private static String fraTekst(Cell cell) {
        try {
            return cell.getStringCellValue();
        } catch (Exception e) {
            throw new UnexpectedCellException(cell, e);
        }
    }

    private static String fraNumerisk(Cell cell) {
        try {
            return new BigDecimal(cell.getNumericCellValue()).toPlainString();
        } catch (Exception e) {
            throw new UnexpectedCellException(cell, e);
        }
    }

    private static HendelseType fraType(Cell cell) {
        try {
            return HendelseType.valueOf(cell.getStringCellValue());
        } catch (Exception e) {
            throw new UnexpectedCellException(cell, e);
        }
    }

    public static LocalDateTime fraTid(Cell cell) {
        try {
            return tilZonedTime(cell).map(ZonedDateTime::toLocalDateTime)
                    .orElse(null);
        } catch (Exception e) {
            throw new UnexpectedCellException(cell, e);
        }
    }

    public static LocalDate fraDato(Cell cell) {
        try {
            return tilZonedTime(cell)
                    .map(ZonedDateTime::toLocalDate)
                    .orElse(null);
        } catch (Exception e) {
            throw new UnexpectedCellException(cell, e);
        }
    }

    private static Optional<ZonedDateTime> tilZonedTime(Cell cell) {
        return Optional.ofNullable(cell)
                .filter(c -> c.getCellType() != BLANK)
                .map(Cell::getDateCellValue)
                .filter((Objects::nonNull))
                .map(Date::toInstant)
                .map(i -> i.atZone(ZoneId.systemDefault()));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + "]";
    }
}
