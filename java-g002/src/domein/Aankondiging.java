package domein;

import exceptions.OngeldigeAankondigingTekstException;
import persistentie.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NamedQueries({
        @NamedQuery(name = "Aankondiging.geefAlle", query =  "SELECT a FROM Aankondiging a")
})
public class Aankondiging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tekst;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime datum;

    @ManyToOne
    private Gebruiker publicist;

    public Aankondiging(String tekst, Gebruiker publicist) throws OngeldigeAankondigingTekstException {
        setTekst(tekst);
        datum = LocalDateTime.now();
        this.publicist = publicist;
    }

    public Aankondiging() {
    }

    public String getTekst() {
        return tekst;
    }

    private void setTekst(String tekst) throws OngeldigeAankondigingTekstException {
        if (tekst == null || tekst.isEmpty() || tekst.isBlank())
            throw new OngeldigeAankondigingTekstException("Tekst is vereist");
        this.tekst = tekst;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public Gebruiker getPublicist() {
        return publicist;
    }

    @Override
    public String toString() {
        return getDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " - " + getTekst();
    }
}
