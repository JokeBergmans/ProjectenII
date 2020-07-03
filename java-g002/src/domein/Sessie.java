package domein;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public interface Sessie {

    String getTitel();
    String getNaamVerantwoordelijke();
    String getGastSpreker();
    String getLokaalNaam();
    String getCampus();
    String getGebouw();
    LocalDate getStartDatum();
    Integer getStartUur();
    Integer getStartMin();
    LocalDate getEindDatum();
    Integer getEindUur();
    Integer getEindMin();
    int getMaxPlaatsen();
    int getAantalAanwezigen();
    int getDagenOpVoorhand();
    SessieType getStatus();
    boolean getStuurHerrinering();
    int getOpenPlaatsen();
    ObservableList<Aankondiging> getGeplaatsteAankondigingen();
    String getMededeling();
    List<Inschrijving> getInschrijvingen();
    int getAantalIngeschreven();
    int getAantalAfwezigen();
}
