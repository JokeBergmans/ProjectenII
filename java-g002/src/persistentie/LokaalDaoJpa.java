package persistentie;

import domein.Lokaal;

public class LokaalDaoJpa extends GenericDaoJpa<Lokaal> implements LokaalDao {

    public LokaalDaoJpa() {
        super(Lokaal.class);
    }

}
