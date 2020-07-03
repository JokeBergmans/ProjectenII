package domein;

import exceptions.OngeldigeSessieKalenderGegevensException;
import persistentie.LocalDateConverter;
import persistentie.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "SessieKalender.geefAlle", query = "SELECT s FROM SessieKalender s")
})
public class SessieKalender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String academiejaar;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate start;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate einde;

    public SessieKalender(LocalDate start, LocalDate einde) throws OngeldigeSessieKalenderGegevensException {
        setAcademiejaar(start, einde);
        setStart(start);
        setEinde(einde);
    }

    public SessieKalender() {
    }

    public String geefAcademiejaar() {
        return academiejaar;
    }

    public void setAcademiejaar(LocalDate start, LocalDate einde) throws OngeldigeSessieKalenderGegevensException {

        if (einde.isBefore(LocalDate.now()))
            throw new OngeldigeSessieKalenderGegevensException("Academiejaar mag niet in het verleden liggen");
        if (einde.isBefore(start))
            throw new OngeldigeSessieKalenderGegevensException("Academiejaar moet chronologisch zijn");
        if (einde.getYear() - start.getYear() > 1)
            throw new OngeldigeSessieKalenderGegevensException("Academiejaar mag slechts over 1 jaar spannen");
        this.academiejaar = start.getYear() + " - " + einde.getYear();
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEinde() {
        return einde;
    }

    public void setEinde(LocalDate einde) {
        this.einde = einde;
    }

    public void pasAan(LocalDate start, LocalDate einde) throws OngeldigeSessieKalenderGegevensException {
        setAcademiejaar(start, einde);
        setStart(start);
        setEinde(einde);
    }
}