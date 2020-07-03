package testen;

import domein.Lokaal;
import domein.SessieITLab;
import exceptions.OngeldigeSessieGegevensException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

public class SessieITLabTest {

    @ParameterizedTest
    @CsvSource({
            "Sessie1, 2020-01-01T00:00:00:000, 2019-01-01T00:00:00:000, 8",
            "Sessie2, 2020-01-01T00:00:00:000, 2020-01-01T00:10:00:000, 20",
            "Sessie3, 2020-01-01T00:00:00:000, 2020-01-01T00:29:59:999, 10",
            " , 2020-01-01T00:00:00:000, 2020-01-01T01:00:00:000, 15",
            "Sessie5, 2020-01-01T00:00:00:000, 2020-01-01T01:00:00:000, 12",
            "Sessie6, 2020-01-01T00:00:00:000, 2020-01-01T01:00:00:000, 0",
            "Sessie7, 2020-01-01T00:00:00:000, 2020-01-01T01:00:00:000, -1",
            "Sessie8, , 2020-01-01T00:00:00:000, 1",
            "Sessie9, 2020-01-01T00:00:00:000, ,2"
            })
    public void ongeldigeSessie(String titel,  @ConvertWith(LocalDateTimeConverter.class) LocalDateTime start, @ConvertWith(LocalDateTimeConverter.class) LocalDateTime einde, int maxPlaatsen) {
        Assertions.assertThrows(OngeldigeSessieGegevensException.class, () -> new SessieITLab(null, titel, null, new Lokaal(), start, einde, maxPlaatsen));
    }

    @Test
    public void ongeldigeSessieStartTeVroeg() {
        Assertions.assertThrows(OngeldigeSessieGegevensException.class, () -> new SessieITLab(null, "Sessie", null, new Lokaal(), LocalDateTime.now().plusHours(23), LocalDateTime.now().plusDays(1), 10));
    }

    @ParameterizedTest
    @CsvSource({
            "Sessie1, 2021-01-01T00:00:00:000, 2021-01-01T01:00:00:000, 10",
            "Sessie2, 2021-01-01T00:00:00:000, 2021-01-01T01:00:00:000, 8",
            "Sessie3, 2021-01-01T00:00:00:000, 2021-01-01T00:30:00:000, 20"
    })
    public void geldigeSessie(String titel, @ConvertWith(LocalDateTimeConverter.class) LocalDateTime start, @ConvertWith(LocalDateTimeConverter.class) LocalDateTime einde, int maxPlaatsen) throws OngeldigeSessieGegevensException {

        SessieITLab sessieITLab = new SessieITLab(null, titel, "gastspreker", new Lokaal(), start, einde, maxPlaatsen);
        Assertions.assertEquals(titel, sessieITLab.getTitel());
        Assertions.assertEquals(start, sessieITLab.getStart());
        Assertions.assertEquals(einde, sessieITLab.getEinde());
        Assertions.assertEquals(maxPlaatsen, sessieITLab.getMaxPlaatsen());
    }


    static class LocalDateTimeConverter implements ArgumentConverter {

        @Override
        public Object convert(Object source, ParameterContext parameterContext) {
            if (!(source instanceof String) || ((String) source).isEmpty())
                return null;
            try {
                String date = ((String) source).split("T")[0];
                String time = ((String) source).split("T")[1];
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                String[] timeParts = time.split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);
                int milliseconds = Integer.parseInt(timeParts[3]);
                return LocalDateTime.of(year, month, day, hours, minutes, seconds, milliseconds);
            } catch (Exception e) {
                throw new IllegalArgumentException("Fout bij omzetting", e);
            }
        }
    }
}