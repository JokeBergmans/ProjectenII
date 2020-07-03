package persistentie;

import domein.SessieITLab;

public class SessieITLabDaoJpa extends GenericDaoJpa<SessieITLab> implements SessieITLabDao {

    public SessieITLabDaoJpa() {
        super(SessieITLab.class);
    }

}
