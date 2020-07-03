package domein;

import exceptions.OngeldigeSessieGegevensException;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Lokaal.zoekLokaalOpGegevens", query = "SELECT l FROM Lokaal l where l.campus = ?1 and l.gebouw = ?2 and l.naam = ?3"),
        @NamedQuery(name ="Lokaal.geefAlle", query = "SELECT l FROM Lokaal l")

})

public class Lokaal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String campus;
    private String naam;

    @Enumerated(EnumType.STRING)
    private LokaalType type;
    private String opmerking;
    private String gebouw;
    private int maxPlaatsen;

    public Lokaal() {
    }

    public Lokaal(String naam, String campus, String gebouw, LokaalType type, String opmerking, int maxPlaatsen) {
        this.campus = campus;
        this.gebouw = gebouw;
        this.naam = naam;
        this.type = type;
        this.opmerking = opmerking;
        this.maxPlaatsen = maxPlaatsen;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        if (campus == null || campus.isEmpty())
            throw new IllegalArgumentException("Naam van de campus is vereist en mag niet leeg zijn");
        this.campus = campus;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) throws OngeldigeSessieGegevensException {
        if (naam == null || naam.isEmpty())
            throw new IllegalArgumentException("Naam van het lokaal is vereist en mag niet leeg zijn");
        this.naam = naam;
    }

    public LokaalType getType() {
        return type;
    }

    public void setType(LokaalType type) {
        this.type = type;
    }

    public int getMaxPlaatsen() {
        return maxPlaatsen;
    }

    public void setMaxPlaatsen(int maxPlaatsen) {
        this.maxPlaatsen = maxPlaatsen;
    }

    public String getOpmerking() {
        return opmerking;
    }

    public void setOpmerking(String opmerking) {
        this.opmerking = opmerking;
    }

    public String getGebouw() {
        return gebouw;
    }

    public void setGebouw(String gebouw) {
        if (gebouw == null || gebouw.isEmpty())
            throw new IllegalArgumentException("Naam van het gebouw is vereist en mag niet leeg zijn");
        this.gebouw = gebouw;
    }

    @Override
    public String toString() {
        return "Lokaal{" +
                "campus='" + campus + '\'' +
                ", naam='" + naam + '\'' +
                ", type=" + type +
                ", opmerking='" + opmerking + '\'' +
                ", gebouw='" + gebouw + '\'' +
                ", maxPlaatsen=" + maxPlaatsen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lokaal lokaal = (Lokaal) o;

        if (!campus.equals(lokaal.campus)) return false;
        return naam.equals(lokaal.naam);
    }

    @Override
    public int hashCode() {
        int result = campus.hashCode();
        result = 31 * result + naam.hashCode();
        return result;
    }

}
