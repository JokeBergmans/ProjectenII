package domein;

import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import persistentie.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Kalender {

    private ObservableList<SessieITLab> sessies;
    private ObservableList<SessieKalender> sessieKalenders;
    private ObservableList<Gebruiker> gebruikers;
    private SessieITLabDao sessieDAO;
    private GebruikerDao gebruikerDAO;
    private KalenderDao kalenderDAO;

    public Kalender() {
        this(new SessieITLabDaoJpa(), new GebruikerDaoJpa(), new KalenderDaoJpa());
    }

    public Kalender(SessieITLabDao sdao, GebruikerDao gdao, KalenderDao kdao) {
        this.sessieDAO = sdao;
        this.gebruikerDAO = gdao;
        this.kalenderDAO = kdao;

        sessies = FXCollections.observableList(new ArrayList<>(sdao.geefAlle()));
        sorteerSessies();
        gebruikers = FXCollections.observableList(new ArrayList<>(gdao.geefAlle()));
        sorteerGebruikers();
        sessieKalenders = FXCollections.observableList(new ArrayList<>(kdao.geefAlle()));
        sorteerKalenders();
    }

    private void sorteerSessies() {
        sessies.sort(Comparator.comparing(SessieITLab::getStart));
    }

    private void sorteerGebruikers() {
        gebruikers.sort((g1, g2) -> g1.getNaam().compareToIgnoreCase(g2.getNaam()));
    }

    private void sorteerKalenders() {
        sessieKalenders.sort(Comparator.comparing(SessieKalender::getStart));
    }

    public void voegSessieToe(SessieITLab sessieITLab) throws OngeldigeSessieGegevensException {
        if (sessies.stream().anyMatch((SessieITLab s) ->
                s.getLokaal().equals(sessieITLab.getLokaal())
                        && (s.getStart().isBefore(sessieITLab.getEinde()) || s.getStart().equals(sessieITLab.getEinde()))
                        && (s.getEinde().isAfter(sessieITLab.getStart()) || s.getEinde().equals(sessieITLab.getStart()))
                        && !sessieITLab.equals(s)
        ))
            throw new OngeldigeSessieGegevensException("Dit lokaal wordt al bezet door een andere sessie op dit moment");
        GenericDaoJpa.startTransactie();
        sessies.add(sessieITLab);
        sessieDAO.voegToe(sessieITLab);
        GenericDaoJpa.commitTransactie();
        sorteerSessies();
    }

    public void voegGebruikerToe(Gebruiker g) {
        GenericDaoJpa.startTransactie();
        gebruikerDAO.voegToe(g);
        gebruikers.add(g);
        GenericDaoJpa.commitTransactie();
    }

    public void pasSessieAan(SessieITLab sessieITLab, SessieDTO sessieDTO, Lokaal lokaal) throws OngeldigeSessieGegevensException {
        if (sessies.stream().anyMatch((SessieITLab s) ->
                s.getLokaal().equals(lokaal)
                        && (s.getStart().isBefore(sessieDTO.getEinde()) || s.getStart().equals(sessieDTO.getEinde()))
                        && (s.getEinde().isAfter(sessieDTO.getStart()) || s.getEinde().equals(sessieDTO.getStart()))
                        && !sessieITLab.equals(s)
        )) {
            throw new OngeldigeSessieGegevensException("Dit lokaal wordt al bezet door een andere sessie op dit moment");
        }
        GenericDaoJpa.startTransactie();
        try {
            sessieITLab.setGegevens(sessieDTO, lokaal);
            GenericDaoJpa.commitTransactie();
            sorteerSessies();
        } catch (OngeldigeSessieGegevensException e) {
            GenericDaoJpa.rollbackTransactie();
            throw e;
        }

    }

    public void voegAankondigingToe(SessieITLab sessieITLab, Aankondiging aankondiging) {
        GenericDaoJpa.startTransactie();
        sessieITLab.voegAankondigingToe(aankondiging);
        GenericDaoJpa.commitTransactie();
    }

    public ObservableList<SessieITLab> geefSessies(Gebruiker gebruiker, SessieType sessieType) {
        if (sessieType == null)
            return geefLegeLijst();
        if (gebruiker.getType().equals(GebruikerType.HOOFDVERANTWOORDELIJKE)) {
            if (sessieType.equals(SessieType.AANGEMAAKT))
                return geefAangemaakteSessies();
            else if (sessieType.equals(SessieType.OPEN))
                return geefOpenSessies();
            else if (sessieType.equals(SessieType.GESTART))
                return geefGestarteSessies();
            else if (sessieType.equals(SessieType.AFGELOPEN))
                return geefAfgelopenSessies();
            else return geefLegeLijst();
        } else if (gebruiker.getType().equals(GebruikerType.VERANTWOORDELIJKE)) {
            if (sessieType.equals(SessieType.AANGEMAAKT))
                return filterSessiesOpVerantwoordelijke(geefAangemaakteSessies(), gebruiker.getNaam());
            else if (sessieType.equals(SessieType.OPEN))
                return filterSessiesOpVerantwoordelijke(geefOpenSessies(), gebruiker.getNaam());
            else if (sessieType.equals(SessieType.GESTART))
                return filterSessiesOpVerantwoordelijke(geefGestarteSessies(), gebruiker.getNaam());
            else if (sessieType.equals(SessieType.AFGELOPEN))
                return filterSessiesOpVerantwoordelijke(geefAfgelopenSessies(), gebruiker.getNaam());
            else return geefLegeLijst();
        } else return geefLegeLijst();
    }

    private ObservableList<SessieITLab> geefAangemaakteSessies() {
        return new FilteredList<>(sessies, sessieITLab -> sessieITLab.getStatus() == SessieType.AANGEMAAKT);
    }

    private ObservableList<SessieITLab> geefOpenSessies() {
        return new FilteredList<>(sessies, sessieITLab -> sessieITLab.getStatus() == SessieType.OPEN);
    }

    private ObservableList<SessieITLab> geefGestarteSessies() {
        return new FilteredList<>(sessies, sessieITLab -> sessieITLab.getStatus() == SessieType.GESTART);
    }

    private ObservableList<SessieITLab> geefAfgelopenSessies() {
        return new FilteredList<>(sessies, sessieITLab -> sessieITLab.getStatus() == SessieType.AFGELOPEN);
    }

    private ObservableList<SessieITLab> filterSessiesOpVerantwoordelijke(ObservableList<SessieITLab> sessies, String naam) {
        return new FilteredList<>(sessies, s -> s.getNaamVerantwoordelijke().equals(naam));
    }

    private ObservableList<SessieITLab> geefLegeLijst() {
        return new FilteredList<>(sessies, s -> false);
    }

    public ObservableList<SessieITLab> geefSessies(LocalDate start, LocalDate einde) {
        return new FilteredList<>(sessies, s -> s.getStartDatum().isAfter(start) && s.getEindDatum().isBefore(einde));
    }

    public ObservableList<Gebruiker> FilteredGebruikerOpNaam(ObservableList<Gebruiker> gebruikers, String naam) {
        return new FilteredList<>(gebruikers, s -> s.getNaam().contains(naam));
    }

    public void verwijderSessie(SessieITLab s) throws OngeldigeVerwijderingException {
        if (s.getStatus() != SessieType.AANGEMAAKT && s.getStatus() != SessieType.AFGELOPEN) {
            throw new OngeldigeVerwijderingException("Sessie is nog niet afgelopen.");
        }
        if (sessies.contains(s)) {
            GenericDaoJpa.startTransactie();
            sessies.remove(s);
            sessieDAO.verwijder(s);
            GenericDaoJpa.commitTransactie();
        } else
            throw new OngeldigeVerwijderingException("Sessie bestaat niet.");
    }

    public ObservableList<SessieKalender> geefSessieKalenders() {
        return sessieKalenders;
    }

    public void voegSessieKalenderToe(SessieKalender sessieKalender) throws OngeldigeSessieKalenderGegevensException {
        if (sessieKalenders.stream().anyMatch((SessieKalender s) -> s.geefAcademiejaar().equals(sessieKalender.geefAcademiejaar()))) {
            throw new OngeldigeSessieKalenderGegevensException("Er bestaat reeds een sessiekalender voor dit academiejaar");
        } else {
            GenericDaoJpa.startTransactie();
            kalenderDAO.voegToe(sessieKalender);
            sessieKalenders.add(sessieKalender);
            GenericDaoJpa.commitTransactie();
        }
    }

    public void pasSessieKalenderAan(SessieKalender sessieKalender, LocalDate start, LocalDate einde) throws OngeldigeSessieKalenderGegevensException {
        try {
            GenericDaoJpa.startTransactie();
            sessieKalender.pasAan(start, einde);
            GenericDaoJpa.commitTransactie();
        } catch (OngeldigeSessieKalenderGegevensException e) {
            GenericDaoJpa.rollbackTransactie();
            throw e;
        }

    }

    public ObservableList<Gebruiker> geefGebruikers() {
        return gebruikers;
    }


    public void pasGebruikerAan(Gebruiker oud, Gebruiker nieuw) {
        GenericDaoJpa.startTransactie();
        oud.setNaam(nieuw.getNaam());
        oud.setStatus(nieuw.getStatus());
        oud.setType(nieuw.getType());
        oud.setGebruikersnaam(nieuw.getGebruikersnaam());
        oud.setProfielFoto(nieuw.getProfielFoto());
        GenericDaoJpa.commitTransactie();
        sorteerGebruikers();
    }

    public void verwijderGebruiker(Gebruiker gebruiker) throws OngeldigeVerwijderingException {
        if (!gebruikers.contains(gebruiker))
            throw new OngeldigeVerwijderingException("Gebruiker bestaat niet");
        GenericDaoJpa.startTransactie();
        gebruikers.remove(gebruiker);
        gebruikerDAO.verwijder(gebruiker);
        GenericDaoJpa.commitTransactie();
    }
}
