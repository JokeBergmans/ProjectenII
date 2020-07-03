package persistentie;

import domein.SessieITLab;
import domein.SessieKalender;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public class KalenderDaoJpa extends GenericDaoJpa<SessieKalender> implements KalenderDao {
    public KalenderDaoJpa() {
        super(SessieKalender.class);
    }

}
