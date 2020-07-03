package domein;

import exceptions.OngeldigeSessieGegevensException;
import exceptions.SessieVolzetException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import persistentie.LocalDateTimeConverter;

import javax.persistence.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Access(value = AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "SessieITLab.findById", query = "SELECT s FROM SessieITLab s where s.id = :sessieId"),
        @NamedQuery(name = "SessieITLab.geefAlle", query = "SELECT s FROM SessieITLab s"),
        @NamedQuery(name = "SessieITLab.getStatusSessies", query = "SELECT s FROM SessieITLab s WHERE s.status = :status"),
        @NamedQuery(name = "SessieITLab.zoekSessieOpGegevens", query = "SELECT s FROM SessieITLab s WHERE s.titel = :titel AND s.gastSpreker = :gastSpreker AND s.start = :start"),
})
public class SessieITLab implements Sessie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Gebruiker verantwoordelijke;

    private String titel;
    private String gastSpreker;

    @OneToOne
    private Lokaal lokaal;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime start;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime einde;

    private int maxPlaatsen;

    @ElementCollection
    private List<String> media = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SessieType status;

    private boolean stuurHerinnering = false;
    private int dagenOpVoorhand;
    private String mededeling;
    private String beschrijving;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Inschrijving> inschrijvingen = new ArrayList<>();

    private ObservableList<Aankondiging> aankondigingen = FXCollections.observableArrayList();

    public SessieITLab(Gebruiker verantwoordelijke, String titel, String gastSpreker, Lokaal lokaal, LocalDateTime start, LocalDateTime einde, int maxPlaatsen) throws OngeldigeSessieGegevensException {
        setVerantwoordelijke(verantwoordelijke);
        setTitel(titel);
        setGastSpreker(gastSpreker);
        setLokaal(lokaal);
        valideerMoment(start, einde);
        setMaxPlaatsen(maxPlaatsen);
        this.status = SessieType.AANGEMAAKT;
    }

    public SessieITLab() {

    }

    public Gebruiker getVerantwoordelijke() {
        return verantwoordelijke;
    }

    public void setVerantwoordelijke(Gebruiker verantwoordelijke) {
        this.verantwoordelijke = verantwoordelijke;
    }

    public String getTitel() {
        return titel;
    }

    @Override
    public String getNaamVerantwoordelijke() {
        return verantwoordelijke.getNaam();
    }

    @Override
    public String getLokaalNaam() {
        return this.lokaal.getNaam();
    }

    public void setTitel(String titel) throws OngeldigeSessieGegevensException {
        if (titel == null || titel.isEmpty())
            throw new OngeldigeSessieGegevensException("Titel is vereist");
        this.titel = titel;
    }

    @Override
    public String getGastSpreker() {
        return gastSpreker;
    }

    public void setGastSpreker(String gastSpreker) throws OngeldigeSessieGegevensException {
        if (gastSpreker == null || gastSpreker.isEmpty()) {
            throw new OngeldigeSessieGegevensException("Gastspreker is vereist");
        }
        this.gastSpreker = gastSpreker;
    }

    public Lokaal getLokaal() {
        return lokaal;
    }

    public void setLokaal(Lokaal lokaal) {
        this.lokaal = lokaal;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEinde() {
        return einde;
    }

    public void setEinde(LocalDateTime einde) {
        this.einde = einde;
    }

    public int getMaxPlaatsen() {
        return maxPlaatsen;
    }

    public void setMaxPlaatsen(int maxPlaatsen) throws OngeldigeSessieGegevensException {
        if (maxPlaatsen <= 0)
            throw new OngeldigeSessieGegevensException("Maximum aantal plaatsen moet positief zijn");
        this.maxPlaatsen = maxPlaatsen;
    }

    private void valideerMoment(LocalDateTime start, LocalDateTime einde) throws OngeldigeSessieGegevensException {
        if (start == null)
            throw new OngeldigeSessieGegevensException("Startdatum en -uur zijn vereist");
        if (einde == null)
            throw new OngeldigeSessieGegevensException("Einddatum en -uur zijn vereist");
        if (start.isAfter(einde))
            throw new OngeldigeSessieGegevensException("Start mag niet na einde plaatsvinden");
        if (start.plusMinutes(30).isAfter(einde))
            throw new OngeldigeSessieGegevensException("Sessie moet minstens 30 minuten duren");
        if (LocalDateTime.now().plusDays(1).isAfter(start))
            throw new OngeldigeSessieGegevensException("Startdatum moet minstens 1 dag in de toekomst liggen");
        setStart(start);
        setEinde(einde);
    }

    public SessieType getStatus() {
        return status;
    }

    public void setStatus(SessieType status) {
        this.status = status;
    }


    @Override
    public List<Inschrijving> getInschrijvingen() {
        return inschrijvingen;
    }

    @Override
    public String getCampus() {
        return this.lokaal.getCampus();
    }

    @Override
    public String getGebouw() {
        return this.lokaal.getGebouw();
    }

    @Override
    public LocalDate getStartDatum() {
        return start.toLocalDate();
    }

    @Override
    public Integer getStartUur() {
        return start.toLocalTime().getHour();
    }

    @Override
    public Integer getStartMin() {
        return start.toLocalTime().getMinute();
    }

    @Override
    public LocalDate getEindDatum() {
        return einde.toLocalDate();
    }

    @Override
    public Integer getEindUur() {
        return einde.toLocalTime().getHour();
    }

    @Override
    public Integer getEindMin() {
        return einde.toLocalTime().getMinute();
    }


    public List<String> getMedia() {
        return media;
    }

    @Override
    public int getAantalIngeschreven() {
        return this.inschrijvingen.size();
    }

    @Override
    public int getAantalAfwezigen() {
        return (int) this.inschrijvingen.stream().filter(i -> i.getStatus() == GebruikerAanwezigheidStatus.AFWEZIG).count();
    }

    @Access(value = AccessType.PROPERTY)
    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Aankondiging> getAankondigingen() {
        return aankondigingen;
    }

    public void setAankondigingen(List<Aankondiging> aankondigingen) {
        if (aankondigingen != null) {
            this.aankondigingen = FXCollections.observableArrayList(aankondigingen);
        }
    }

    public void voegInschrijvingToe(Gebruiker gebruiker) throws SessieVolzetException {
        if (getOpenPlaatsen() > 0)
            inschrijvingen.add(new Inschrijving(gebruiker, LocalDateTime.now(), this));
        else
            throw new SessieVolzetException("Deze sessie is volzet");
    }

    public void voegAankondigingToe(Aankondiging aankondiging) {
        aankondigingen.add(aankondiging);
    }

    @Override
    public boolean getStuurHerrinering() {
        return stuurHerinnering;
    }

    public void setStuurHerinnering(boolean stuurHerinnering) throws OngeldigeSessieGegevensException {
        this.stuurHerinnering = stuurHerinnering;
        //standaard 1 dag op voorhand sturen
        if (stuurHerinnering) setDagenOpVoorhand(1);
        else setDagenOpVoorhand(0);
    }


    @Override
    public int getOpenPlaatsen() {
        return maxPlaatsen - inschrijvingen.size();
    }

    @Override
    public ObservableList<Aankondiging> getGeplaatsteAankondigingen() {
        return this.aankondigingen;
    }

    public void setGegevens(SessieDTO sessieDTO, Lokaal lokaal) throws OngeldigeSessieGegevensException {
        setTitel(sessieDTO.getTitel());
        setGastSpreker(sessieDTO.getGastspreker());
        setLokaal(lokaal);
        setStatus(sessieDTO.getStatus());
        setVerantwoordelijke(sessieDTO.getVerantwoordelijke());
        setStuurHerinnering(sessieDTO.getStuurHerinnering());
        if (sessieDTO.getAantalPlaatsen() > 0)
            setMaxPlaatsen(sessieDTO.getAantalPlaatsen());
        if(!(this.getStart() == null && this.getEinde() == null)) {
            if (!sessieDTO.getStart().isEqual(getStart()) || !sessieDTO.getEinde().isEqual(getEinde()))
                valideerMoment(sessieDTO.getStart(), sessieDTO.getEinde());
        } else {
            setStart(sessieDTO.getStart());
            setEinde(sessieDTO.getEinde());
        }
        setStuurHerinnering(sessieDTO.getStuurHerinnering());
        setDagenOpVoorhand(sessieDTO.getDagenOpVoorhand());
        setMededeling(sessieDTO.getMededeling());
    }

    @Override
    public int getAantalAanwezigen() {
        return (int) inschrijvingen.stream().filter(i -> i.getStatus() == GebruikerAanwezigheidStatus.AANWEZIG).count();
    }

    @Override
    public String toString() {
        return titel + " - " + start.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")) + " om " + start.toLocalTime();
    }

    @Override
    public int getDagenOpVoorhand() {
        return dagenOpVoorhand;
    }

    public void setDagenOpVoorhand(int dagenOpVoorhand) throws OngeldigeSessieGegevensException {
        if (dagenOpVoorhand < 0) {
            throw new OngeldigeSessieGegevensException("Het aantal dagen voor de herinnering moet positief zijn");
        }
        this.dagenOpVoorhand = dagenOpVoorhand;
    }

    @Override
    public String getMededeling() {
        return mededeling;
    }

    public void setMededeling(String mededeling) {
        this.mededeling = mededeling;
    }

    public ObservableList<Inschrijving> geefInschrijvingenOpNaam(String naam) {
        return new FilteredList<>(FXCollections.observableList(this.inschrijvingen), i -> i.getGebruiker().getNaam().contains(naam));
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }
}

