package domein;

import exceptions.OngeldigeFeedbackException;

import javax.persistence.*;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gebruiker auteur;

    private String tekst;

    public Feedback(Gebruiker auteur, String tekst) throws OngeldigeFeedbackException {
        this.auteur = auteur;
        setTekst(tekst);
    }

    public Feedback() {
    }

    public Gebruiker getAuteur() {
        return auteur;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) throws OngeldigeFeedbackException {
        if (tekst.isEmpty())
            throw new OngeldigeFeedbackException("Tekst mag niet leeg zijn");
        this.tekst = tekst;
    }
}
