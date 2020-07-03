package domein;

import persistentie.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Inschrijving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Gebruiker gebruiker;

    @OneToOne
    private SessieITLab sessie;

    @Enumerated(EnumType.STRING)
    private GebruikerAanwezigheidStatus status;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime datum;

    private boolean aanwezig;

    public Inschrijving() {
    }

    public Inschrijving(Gebruiker gebruiker, LocalDateTime datum, SessieITLab sessie) {
        this.gebruiker = gebruiker;
        this.gebruiker.incrementInschrijving();
        this.datum = datum;
        this.sessie = sessie;
        aanwezig = false;
        this.status = GebruikerAanwezigheidStatus.INGESCHREVEN;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public boolean isAanwezig() {
        return aanwezig;
    }

    public void setAanwezig(boolean aanwezig) {
        this.aanwezig = aanwezig;
    }

    public GebruikerAanwezigheidStatus getStatus() {
        return status;
    }

    public void setStatus(GebruikerAanwezigheidStatus status) {

        //gebruiker kan niet terug naar de ingeschreven status eenmaal hij afwezig of aanwezig gezet is

        if(status == GebruikerAanwezigheidStatus.AANWEZIG) {
            if(this.status == GebruikerAanwezigheidStatus.INGESCHREVEN) {
                gebruiker.incrementAanwezig();
            } else if (this.status == GebruikerAanwezigheidStatus.AFWEZIG)  {
                gebruiker.incrementAanwezig();
                gebruiker.verminderAfwezig();
            }
            this.status = status;
        }

        if(status == GebruikerAanwezigheidStatus.AFWEZIG) {
            if(this.status == GebruikerAanwezigheidStatus.INGESCHREVEN) {
                gebruiker.incrementAfwezig();
            } else if (this.status == GebruikerAanwezigheidStatus.AANWEZIG)  {
                gebruiker.incrementAfwezig();
                gebruiker.verminderAanwezig();
            }
            this.status = status;
        }
    }

    @Override
    public String toString() {
        return "Inschrijving{" +
                "gebruiker=" + gebruiker +
                ", status=" + status +
                ", sessie= " + sessie +
                ", datum=" + datum +
                ", aanwezig=" + aanwezig +
                ", hashcode" + hashCode() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inschrijving that = (Inschrijving) o;

        if (!gebruiker.equals(that.gebruiker)) return false;
        if (status != that.status) return false;
        return datum.equals(that.datum);
    }

    @Override
    public int hashCode() {
        int result = gebruiker.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + datum.hashCode();
        return result;
    }
}
