package testen;

import domein.Aankondiging;
import domein.Gebruiker;
import exceptions.OngeldigeAankondigingTekstException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class AankondigingTest {

    private Gebruiker gebruiker;

    @BeforeEach
    public void before() {
        gebruiker = new Gebruiker();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "\t", "\n" })
    public void ongeldigeAankondiging(String tekst) {
        Assertions.assertThrows(OngeldigeAankondigingTekstException.class, () -> new Aankondiging(tekst, gebruiker));
    }

    @ParameterizedTest
    @CsvSource({
            "Aankondiging1",
            "Aankondiging2 TEST",
            "Aankondiging3"
    })
    public void geldigeAankondiging(String tekst) throws OngeldigeAankondigingTekstException {
        Aankondiging aankondiging = new Aankondiging(tekst, gebruiker);
        Assertions.assertEquals(tekst, aankondiging.getTekst());
    }
}