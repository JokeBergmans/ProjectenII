package persistentie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class GenericDaoJpa<T> implements GenericDao<T> {

    private static final String PU_NAME = "G02";
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
    protected static final EntityManager em = emf.createEntityManager();
    private final Class<T> type;

    public GenericDaoJpa(Class<T> type) {
        this.type = type;
    }

    public static void sluitPersistentie() {
        em.close();
        emf.close();
    }

    public static void startTransactie() {
        em.getTransaction().begin();
    }

    public static void commitTransactie() {
        em.getTransaction().commit();
    }

    public static void rollbackTransactie() {
        em.getTransaction().rollback();
    }


    @Override
    public List<T> geefAlle() {
        return em.createNamedQuery(type.getSimpleName() + ".geefAlle", type).getResultList();
    }

    @Override
    public T geef(Long id) {
        return em.find(type, id);
    }

    @Override
    public T pasAan(T object) {
        return em.merge(object);
    }

    @Override
    public void verwijder(T object) {
        em.remove(em.merge(object));
    }

    @Override
    public void voegToe(T object) {
        em.persist(object);
    }

    @Override
    public boolean bestaat(Long id) {
        return em.find(type, id) != null;
    }
}
