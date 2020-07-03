package persistentie;

import domein.Gebruiker;
import domein.SessieITLab;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public class GebruikerDaoJpa extends GenericDaoJpa<Gebruiker> implements GebruikerDao {

    public GebruikerDaoJpa() {
        super(Gebruiker.class);
    }

    @Override
    public Gebruiker getGebruikerByGebruikernaam(String naam) throws EntityNotFoundException {
        try{
            return em.createNamedQuery("Gebruiker.zoekOpGebruikerNaam", Gebruiker.class)
                    .setParameter("naamGebruiker",naam)
                    .getSingleResult();
        }catch (NoResultException ex){
            throw new EntityNotFoundException();
        }
    }

}
