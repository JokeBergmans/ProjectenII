package testen;

import domein.*;
import exceptions.OngeldigLokaalException;
import exceptions.OngeldigeSessieGegevensException;
import exceptions.OngeldigeSessieKalenderGegevensException;
import exceptions.OngeldigeVerwijderingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import persistentie.GebruikerDao;
import persistentie.KalenderDao;
import persistentie.SessieITLabDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class KalenderTest {

    @Mock
    private SessieITLabDao sessieDaoDummy;
    @Mock
    private KalenderDao kalenderDaoDummy;
    @Mock
    private GebruikerDao gebruikerDaoDummy;

    @InjectMocks
    private Kalender kalender;

    private Gebruiker hoofdverantwoordelijke;
    private Gebruiker verantwoordelijke;
    private Gebruiker gebruiker;

    @BeforeEach
    public void maakLijsten() {
        hoofdverantwoordelijke = new Gebruiker(GebruikerType.HOOFDVERANTWOORDELIJKE, "Hoofdverantwoordelijke", "Hoofdverantwoordelijke");
        verantwoordelijke = new Gebruiker(GebruikerType.VERANTWOORDELIJKE, "Verantwoordelijke", "Verantwoordelijke");
        gebruiker = new Gebruiker(GebruikerType.GEBRUIKER, "Gebruiker", "Gebruiker");

        LocalDateTime start = LocalDateTime.of(2021, 1, 1, 10, 0);
        LocalDateTime einde = start.plusHours(1);
        LokalenRepository lr = new LokalenRepository();
        try {
            Lokaal lokaal = lr.geefLokaalOpGegevens("GENT", "B", "1.012");
            SessieITLab sessieITLab1 = new SessieITLab(hoofdverantwoordelijke, "sessie1", "gastspreken", lokaal, start, einde, 10);
            SessieITLab sessieITLab2 = new SessieITLab(hoofdverantwoordelijke, "sessie2", "gastspreker", lokaal, start, einde, 20);
            SessieITLab sessieITLab3 = new SessieITLab(hoofdverantwoordelijke, "sessie3", "gastspreker", lokaal, start, einde, 30);
            sessieITLab1.setStatus(SessieType.OPEN);
            sessieITLab3.setStatus(SessieType.AFGELOPEN);
            SessieITLab sessieITLab4 = new SessieITLab(verantwoordelijke, "sessie4", "gastspreker", lokaal, start, einde, 10);
            SessieITLab sessieITLab5 = new SessieITLab(verantwoordelijke, "sessie5", "gastspreker", lokaal, start, einde, 20);
            SessieITLab sessieITLab6 = new SessieITLab(verantwoordelijke, "sessie6", "gastspreker", lokaal, start, einde, 30);
            sessieITLab4.setStatus(SessieType.OPEN);
            List<SessieITLab> sessies = Arrays.asList(sessieITLab1, sessieITLab2, sessieITLab3, sessieITLab4, sessieITLab5, sessieITLab6);
            Mockito.when(sessieDaoDummy.geefAlle()).thenReturn(sessies);
            Mockito.when(gebruikerDaoDummy.geefAlle()).thenReturn(Arrays.asList(hoofdverantwoordelijke, verantwoordelijke, gebruiker));
            Mockito.when(kalenderDaoDummy.geefAlle()).thenReturn(Collections.singletonList(new SessieKalender(LocalDate.of(2020, 1, 1), start.toLocalDate())));
            kalender = new Kalender(sessieDaoDummy, gebruikerDaoDummy, kalenderDaoDummy);
        } catch (OngeldigeSessieGegevensException | OngeldigLokaalException | OngeldigeSessieKalenderGegevensException fsge) {
            fsge.printStackTrace();
        }
    }

    @Test
    public void geefAangemaakteSessiesHoofdverantwoordelijke() {
        List<SessieITLab> resultaat = kalender.geefSessies(hoofdverantwoordelijke, SessieType.AANGEMAAKT);
        Assertions.assertEquals(3, resultaat.size());
    }

    @Test
    public void geefOpenSessiesHoofdverantwoordeijke() {
        List<SessieITLab> resultaat = kalender.geefSessies(hoofdverantwoordelijke, SessieType.OPEN);
        Assertions.assertEquals(2, resultaat.size());
    }

    @Test
    public void geefAfgelopenSessiesHoofdverantwoordelijke() {
        List<SessieITLab> resultaat = kalender.geefSessies(hoofdverantwoordelijke, SessieType.AFGELOPEN);
        Assertions.assertEquals(1, resultaat.size());
    }

    @Test
    public void geefAangemaakteSessiesVerantwoordelijke() {
        List<SessieITLab> resultaat = kalender.geefSessies(verantwoordelijke, SessieType.AANGEMAAKT);
        Assertions.assertEquals(2, resultaat.size());
    }

    @Test
    public void geefOpenSessiesVerantwoordelijke() {
        List<SessieITLab> resultaat = kalender.geefSessies(verantwoordelijke, SessieType.OPEN);
        Assertions.assertEquals(1, resultaat.size());
    }

    @Test
    public void geefGeslotenSessiesVerantwoordelijke() {
        List<SessieITLab> resultaat = kalender.geefSessies(verantwoordelijke, SessieType.AFGELOPEN);
        Assertions.assertEquals(0, resultaat.size());
    }

    @Test
    public void geefSessiesGebruiker() {
        List<SessieITLab> resultaat = kalender.geefSessies(gebruiker, SessieType.OPEN);
        Assertions.assertEquals(0, resultaat.size());
    }


    @Test
    public void voegSessieToe() throws OngeldigeSessieGegevensException {
        SessieITLab sessie = new SessieITLab(verantwoordelijke, "titel", "gastspreken", new Lokaal(), LocalDateTime.of(2021, 1, 1, 0, 0, 0), LocalDateTime.of(2021, 1, 1, 1, 0, 0), 10);
        kalender.voegSessieToe(sessie);
        Assertions.assertEquals(4, kalender.geefSessies(hoofdverantwoordelijke, SessieType.AANGEMAAKT).size());
    }

    @Test
    public void pasSessieAan() throws OngeldigeSessieGegevensException {
        String titel = "titel";
        String gastspreker = "gastspreker";
        LocalDateTime start = LocalDateTime.of(2021, 1, 2, 0, 0, 0);
        LocalDateTime einde = LocalDateTime.of(2021, 1, 2, 1, 0, 0);
        SessieType status = SessieType.GESTART;

        SessieITLab s = kalender.geefSessies(verantwoordelijke, SessieType.OPEN).get(0);
        SessieDTO sessieDTO = new SessieDTO();
        sessieDTO.setTitel(titel);
        sessieDTO.setGastspreker(gastspreker);
        sessieDTO.setStartDatum(start.toLocalDate(), start.getHour(), start.getMinute());
        sessieDTO.setEindDatum(einde.toLocalDate(), einde.getHour(), einde.getMinute());
        sessieDTO.setStatus(status);

        kalender.pasSessieAan(s, sessieDTO, new Lokaal());
        Assertions.assertEquals(titel, s.getTitel());
        Assertions.assertEquals(gastspreker, s.getGastSpreker());
        Assertions.assertEquals(start, s.getStart());
        Assertions.assertEquals(einde, s.getEinde());
        Assertions.assertEquals(status, s.getStatus());
    }

    @Test
    public void verwijderGeldigeSessie() throws OngeldigeVerwijderingException {

        List<SessieITLab> sessies = kalender.geefSessies(hoofdverantwoordelijke, SessieType.AFGELOPEN);
        SessieITLab s = sessies.get(0);

        kalender.verwijderSessie(s);
        Assertions.assertEquals(0, sessies.size());
    }

    @Test
    public void verwijderOnbestaandeSessie() throws OngeldigeSessieGegevensException {

        LocalDateTime start = LocalDateTime.of(2021, 1, 1, 10, 0);
        LocalDateTime einde = start.plusHours(1);
        SessieITLab sessieITLab = new SessieITLab(hoofdverantwoordelijke, "sessie7", "gastspreken", new Lokaal(), start, einde, 10);
        Assertions.assertThrows(OngeldigeVerwijderingException.class, () -> kalender.verwijderSessie(sessieITLab));
    }

    @Test
    public void geefGebruikers() {
        List<Gebruiker> resultaat = kalender.geefGebruikers();
        Assertions.assertEquals(3, resultaat.size());
    }

    @Test
    public void verwijderGeldigeGebruiker() throws OngeldigeVerwijderingException {
        List<Gebruiker> gebruikers = kalender.geefGebruikers();
        Gebruiker g = gebruikers.get(2);

        kalender.verwijderGebruiker(g);
        Assertions.assertEquals(2, gebruikers.size());
    }

    @Test
    public void verwijderOnbestaandeGebruiker() {
        Gebruiker g = new Gebruiker(GebruikerType.GEBRUIKER, "Testgebruiker", "Testgebruiker");
        Assertions.assertThrows(OngeldigeVerwijderingException.class, () -> kalender.verwijderGebruiker(g));
    }

    @Test
    public void pasGebruikerAan(){
        GebruikerType type = GebruikerType.GEBRUIKER;
        String naam = "gebruiker";
        String gebruikersnaam = "gebruikersnaam";
        GebruikersStatusType status = GebruikersStatusType.NIET_ACTIEF;
        String password = "password";

        Gebruiker gebruiker = gebruikerDaoDummy.geefAlle().get(0);

        kalender.pasGebruikerAan(gebruiker, new Gebruiker(type, naam, gebruikersnaam, status, password));
        Assertions.assertEquals(type, gebruiker.getType());
        Assertions.assertEquals(naam, gebruiker.getNaam());
        Assertions.assertEquals(gebruikersnaam, gebruiker.getGebruikersnaam());
        Assertions.assertEquals(status, gebruiker.getStatus());
    }

    @Test
    public void voegGebruikerToe(){
        kalender.voegGebruikerToe(new Gebruiker(GebruikerType.GEBRUIKER, "gebruiker", "gebruikersnaam", GebruikersStatusType.ACTIEF, "password"));
        Assertions.assertEquals(4, kalender.geefGebruikers().size());
    }

    @Test
    public void geefSessieKalenders() {
        List<SessieKalender> resultaat = kalender.geefSessieKalenders();
        Assertions.assertEquals(1, resultaat.size());
    }


    @Test
    public void pasSessieKalenderAan() throws OngeldigeSessieKalenderGegevensException {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate einde = LocalDate.of(2022, 1, 1);
        SessieKalender sessieKalender = kalender.geefSessieKalenders().get(0);
        kalender.pasSessieKalenderAan(sessieKalender, start, einde);
        Assertions.assertEquals("2021 - 2022", sessieKalender.geefAcademiejaar());
        Assertions.assertEquals(start, sessieKalender.getStart());
        Assertions.assertEquals(einde, sessieKalender.getEinde());
    }

    @Test
    public void voegSessieKalenderToe() throws OngeldigeSessieKalenderGegevensException {
        SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1));
        kalender.voegSessieKalenderToe(sessieKalender);
        Assertions.assertEquals(2, kalender.geefSessieKalenders().size());
    }


    @Test
    public void voegOngeldigeSessieKalenderToe() throws OngeldigeSessieKalenderGegevensException {
        SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1));
        Assertions.assertThrows(OngeldigeSessieKalenderGegevensException.class, () -> kalender.voegSessieKalenderToe(sessieKalender));
    }
}