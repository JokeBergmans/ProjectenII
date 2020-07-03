package testen;

import domein.SessieKalender;
import exceptions.OngeldigeSessieKalenderGegevensException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.ArrayList;

public class SessieKalenderTest {

    @ParameterizedTest
    @CsvSource({
            "2019-01-01, 2020-01-01",
            "2020-01-01, 2019-01-01",
            "2020-01-01, 2022-01-01"
    })
    public void ongeldigeSessieKalender(@ConvertWith(LocalDateConverter.class) LocalDate start, @ConvertWith(LocalDateConverter.class) LocalDate einde) {
        Assertions.assertThrows(OngeldigeSessieKalenderGegevensException.class, () -> new SessieKalender(start, einde));
    }

    @ParameterizedTest
    @CsvSource({
            "2020-01-01, 2021-02-01",
            "2022-01-01, 2022-02-02"

    })
    public void geldigeSessieKalender(@ConvertWith(LocalDateConverter.class) LocalDate start, @ConvertWith(LocalDateConverter.class) LocalDate einde) throws OngeldigeSessieKalenderGegevensException {
        SessieKalender kalender = new SessieKalender(start, einde);
        String academiejaar = start.getYear() + " - " + einde.getYear();
        Assertions.assertEquals(start, kalender.getStart());
        Assertions.assertEquals(einde, kalender.getEinde());
        Assertions.assertEquals(academiejaar, kalender.geefAcademiejaar());
    }

    static class LocalDateConverter implements ArgumentConverter {

        @Override
        public Object convert(Object source, ParameterContext parameterContext) {
            if (!(source instanceof String) || ((String) source).isEmpty())
                return null;
            try {
                String[] dateParts = ((String) source).split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                throw new IllegalArgumentException("Fout bij omzetting", e);
            }
        }
    }

}