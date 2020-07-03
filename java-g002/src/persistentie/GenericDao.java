package persistentie;

import java.util.List;

public interface GenericDao<T> {

    List<T> geefAlle();
    T geef(Long id);
    T pasAan(T object);
    void verwijder(T object);
    void voegToe(T object);
    boolean bestaat(Long id);

}
