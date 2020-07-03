package domein;

import exceptions.OngeldigLokaalException;
import persistentie.GenericDao;
import persistentie.GenericDaoJpa;
import persistentie.LokaalDaoJpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LokalenRepository {


    private static final String LOKAAL_PATH = "src/data/lokalen.csv";
    private static final Map<String, LokaalType> LOKAAL_TYPE_MAP = new HashMap<>();
    private List<Lokaal> lokalen = new ArrayList<>();
    private final GenericDao<Lokaal> LOKAAL_DAO = new LokaalDaoJpa();

    public LokalenRepository() {


        LOKAAL_TYPE_MAP.put("Auditorium", LokaalType.AUDITORIUM);
        LOKAAL_TYPE_MAP.put("Leslokaal", LokaalType.LESLOKAAL);
        LOKAAL_TYPE_MAP.put("PC-lokaal", LokaalType.PCLOKAAL);
        LOKAAL_TYPE_MAP.put("Laptoplokaal", LokaalType.LAPTOPLOKAAL);
        LOKAAL_TYPE_MAP.put("Hybride", LokaalType.HYBRIDE);
        LOKAAL_TYPE_MAP.put("Taallab", LokaalType.TAALLAB);
        LOKAAL_TYPE_MAP.put("Vergaderzaal", LokaalType.VERGADERZAAL);
        vulDbMetLokalenUitCsv();
        laadLokalen();

    }

    private void laadLokalen() {
        this.lokalen = this.LOKAAL_DAO.geefAlle();
    }

    private void vulDbMetLokalenUitCsv() {

        //Excel exporterted csv file voegen blijkbaar een hidden bit flag toe aan de eerste line van de file hierdoor klopt
        //de regex niet volledig als er dingen op de eerste lijn staan, hou eerste lijn open en skip hem \

        try (Stream<String> stream = Files.lines(Paths.get(LOKAAL_PATH)).skip(1)) {
            stream.forEach(l -> {

                List<String> data = Arrays.asList(l.trim().split(","));

                String naam = data.get(0);
                LokaalType type = LOKAAL_TYPE_MAP.get(data.get(1));
                int capaciteit = Integer.parseInt(data.get(2));
                String opmerking = data.get(3);
                String gebouw = data.get(4);
                String campus = data.get(5);

                Lokaal lokaal = new Lokaal(formatteerLokaalnaam(naam), campus, gebouw, type, opmerking, capaciteit);
                GenericDaoJpa.startTransactie();
                LOKAAL_DAO.voegToe(lokaal);
                GenericDaoJpa.commitTransactie();

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatteerLokaalnaam(String naam) {
        if(naam.matches("^[0-9]{4}")) {
            return naam.substring(0,1) + "." + naam.substring(1);
        }
        else {
            return naam;
        }
    }

    public List<String> getLokaalNamenVanGebouwInCampus(String campus, String gebouw) {
        return this.lokalen.stream()
                .filter(l -> l.getCampus().equals(campus) && l.getGebouw().equals(gebouw))
                .map(Lokaal::getNaam)
                .sorted(Comparator.naturalOrder())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getLokaalNamenVanCampus(String campus) {
        return this.lokalen.stream()
                .filter(l -> l.getCampus().equals(campus))
                .map(Lokaal::getNaam)
                .sorted(Comparator.naturalOrder())
                .distinct()
                .collect(Collectors.toList());
    }

    public Lokaal geefLokaalOpGegevens(String campus, String gebouw, String naam) throws OngeldigLokaalException {
        Optional<Lokaal> lokaal =  this.lokalen.stream()
                .filter(l -> l.getCampus().equals(campus) && l.getGebouw().equals(gebouw) && l.getNaam().equals(naam))
                .findFirst();

        if(lokaal.isPresent()) {
            return lokaal.get();
        } else {
            throw new OngeldigLokaalException("Lokaal bestaat niet");
        }
    }

    public List<Lokaal> getLokalen() {
        return this.lokalen;
    }

}
