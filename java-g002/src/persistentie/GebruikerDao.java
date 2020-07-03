package persistentie;

import domein.Gebruiker;
import domein.SessieITLab;

import javax.persistence.EntityNotFoundException;

public interface GebruikerDao extends GenericDao<Gebruiker> {
    Gebruiker getGebruikerByGebruikernaam(String naam ) throws EntityNotFoundException;

}
