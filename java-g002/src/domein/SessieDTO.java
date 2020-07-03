package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SessieDTO {

    private String titel;
    private String gastspreker;
    private String campus;
    private String gebouw;
    private String lokaalNaam;
    private Gebruiker verantwoordelijke;
    private boolean stuurHerinnering;
    private int dagenOpVoorhand;
    private String mededeling;
    private SessieType status;
    private LocalDateTime start;
    private LocalDateTime einde;
    private int aantalPlaatsen;

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setGastspreker(String gastspreker)  {
        this.gastspreker = gastspreker;
    }

    public String getGastspreker() {
        return gastspreker;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getGebouw() {
        return gebouw;
    }

    public void setGebouw(String gebouw) {
        this.gebouw = gebouw;
    }

    public String getLokaalNaam() {
        return lokaalNaam;
    }

    public void setLokaalNaam(String lokaalNaam) {
        this.lokaalNaam = lokaalNaam;
    }

    public boolean getStuurHerinnering() {
        return this.stuurHerinnering;
    }

    public void setHerinnering(boolean stuurHerinnering) {
        this.stuurHerinnering = stuurHerinnering;
    }

    public SessieType getStatus() {
        return this.status;
    }

    public void setStatus(SessieType status) {
        this.status = status;
    }

    public void setEindDatum(LocalDate eindDatum, Integer eindUur, Integer eindMin) {
        LocalTime eindtijd = LocalTime.of(eindUur, eindMin);
        einde = LocalDateTime.of(eindDatum, eindtijd);
    }

    public void setStartDatum(LocalDate startDatum, Integer startUur, Integer startMin) {
        LocalTime starttijd = LocalTime.of(startUur, startMin);
        start = LocalDateTime.of(startDatum, starttijd);
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEinde() {
        return this.einde;
    }

    public void setDagenOpVoorhand(Integer dagen) {
        this.dagenOpVoorhand = dagen;
    }

    public int getDagenOpVoorhand() {
        return this.dagenOpVoorhand;
    }

    public void setMededeling(String text) {
        this.mededeling = text;
    }

    public String getMededeling() {
        return this.mededeling;
    }

    public int getAantalPlaatsen() {
        return aantalPlaatsen;
    }

    public void setAantalPlaatsen(int aantalPlaatsen) {
        this.aantalPlaatsen = aantalPlaatsen;
    }

    public Gebruiker getVerantwoordelijke() {
        return verantwoordelijke;
    }

    public void setVerantwoordelijke(Gebruiker verantwoordelijke) {
        this.verantwoordelijke = verantwoordelijke;
    }
}
