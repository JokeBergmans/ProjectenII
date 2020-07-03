package domein;

import exceptions.*;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import persistentie.GebruikerDaoJpa;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.persistence.EntityNotFoundException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.*;


public class DomeinController {

    private Kalender kalender;
    private Property<Sessie> sessieProperty;
    private Property<Gebruiker> gebruikerProperty;
    private Property<SessieType> sessieTypeProperty;
    private Gebruiker ingelogdeGebruiker;
    private LokalenRepository lokalenRepo = new LokalenRepository();
    private Map<String, List<String>> campussen = new HashMap<>();

    //encrypteren
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;


    public DomeinController() throws Exception {
        kalender = new Kalender();
        sessieProperty = new SimpleObjectProperty<>();
        sessieTypeProperty = new SimpleObjectProperty<>();
        sessieTypeProperty.setValue(SessieType.AANGEMAAKT);

        gebruikerProperty = new SimpleObjectProperty<>();
        gebruikerProperty.setValue(ingelogdeGebruiker);
        campussen.put("GENT", Arrays.asList("B", "C", "P"));
        campussen.put("AALST", Collections.singletonList("GAARB"));

        //encypteren
        myEncryptionKey = "ThisIsSpartaThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);

    }

    public void voegSessieToe(SessieDTO sessieDTO) throws OngeldigeSessieGegevensException, OngeldigLokaalException {
        SessieITLab sessie = new SessieITLab();
        Lokaal lokaal = lokalenRepo.geefLokaalOpGegevens(sessieDTO.getCampus(), sessieDTO.getGebouw(), sessieDTO.getLokaalNaam());
        sessieDTO.setVerantwoordelijke(this.ingelogdeGebruiker);
        sessie.setGegevens(sessieDTO, lokaal);
        voegSessieToe(sessie);
    }

    public void pasSessieAan(SessieDTO sessieDTO) throws OngeldigeSessieGegevensException, OngeldigLokaalException {
        Sessie s = sessieProperty.getValue();
        Lokaal lokaal = lokalenRepo.geefLokaalOpGegevens(sessieDTO.getCampus(), sessieDTO.getGebouw(), sessieDTO.getLokaalNaam());
        SessieITLab sessieITLab = (SessieITLab) sessieProperty.getValue();
        sessieDTO.setVerantwoordelijke(sessieITLab.getVerantwoordelijke());
        kalender.pasSessieAan((SessieITLab) s, sessieDTO, lokaal);
    }

    public ObservableList<Sessie> geefSessies(SessieType sessieType) {
        return (ObservableList<Sessie>) (Object) kalender.geefSessies(ingelogdeGebruiker, sessieType);
    }

    public void voegAankondigingToe(String tekst) throws OngeldigeAankondigingTekstException {
        kalender.voegAankondigingToe((SessieITLab) sessieProperty.getValue(), new Aankondiging(tekst, ingelogdeGebruiker));
    }

    public void voegSessieToe(SessieITLab sessie) throws OngeldigeSessieGegevensException {
        kalender.voegSessieToe(sessie);
    }

    public List<String> getCampussen() {
        return new ArrayList<>(campussen.keySet());
    }

    public Property<Sessie> getSessieProperty() {
        return sessieProperty;
    }


    public void setGeselecteerdeSessie(Sessie sessie) {
        sessieProperty.setValue(sessie);
    }

    public void verwijderSessie(Sessie s) throws OngeldigeVerwijderingException {
        kalender.verwijderSessie((SessieITLab) s);
    }

    public List<String> getGebouwenVanCampus(String campus) {
        return this.campussen.get(campus);
    }

    public List<String> getLokaalNamenVanGebouwInCampus(String campus, String gebouw) {
        return lokalenRepo.getLokaalNamenVanGebouwInCampus(campus, gebouw);
    }

    public ObservableList<SessieKalender> geefSessieKalenders() {
        return kalender.geefSessieKalenders();
    }

    public void voegSessieKalenderToe(LocalDate start, LocalDate einde) throws OngeldigeSessieKalenderGegevensException {
        SessieKalender sessieKalender = new SessieKalender(start, einde);
        kalender.voegSessieKalenderToe(sessieKalender);
    }

    public void pasSessieKalenderAan(SessieKalender sessieKalender, LocalDate start, LocalDate einde) throws OngeldigeSessieKalenderGegevensException {
        kalender.pasSessieKalenderAan(sessieKalender, start, einde);
    }

    public ObservableList<Sessie> geefSessieKalenderSessies(SessieKalender sessieKalender, int maand) {
        ObservableList<SessieITLab> sessies = new FilteredList<>(kalender.geefSessies(sessieKalender.getStart(), sessieKalender.getEinde()), s -> s.getStart().getMonthValue() == maand);
        return (ObservableList<Sessie>) (Object) sessies;
    }

    public Property<SessieType> getSessieStatusProperty() {
        return sessieTypeProperty;
    }

    public ObservableList<Inschrijving> geefInschrijvingenHuidigeSessie() {
        return FXCollections.observableList(sessieProperty.getValue().getInschrijvingen());
    }

    public ObservableList<Gebruiker> geefGebruikerOpNaam(ObservableList<Gebruiker> g, String naam) {
        return kalender.FilteredGebruikerOpNaam(g, naam);
    }

    public void pasGebruikerAan(Gebruiker g) {
        kalender.pasGebruikerAan(gebruikerProperty.getValue(), g);
    }

    public ObservableList<Gebruiker> geefGebruikers() {

        return kalender.geefGebruikers();
    }

    public void setGeselecteerdeGebruiker(Gebruiker newIns) {
        gebruikerProperty.setValue(newIns);
    }

    public String encrypt(String unencryptedString) throws Exception {

        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.getEncoder().encode(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;

    }

    public void voegGebruikerToe(Gebruiker g) throws Exception {
        String password = g.getEncryptedPassword();
        g.setEncryptedPassword(this.encrypt(password));
        kalender.voegGebruikerToe(g);
    }

    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.getDecoder().decode(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public boolean controleLogin(String gebruikersnaam, String wachtwoord) {
        GebruikerDaoJpa gdj = new GebruikerDaoJpa();
        Gebruiker g = null;
        try {
            g = gdj.getGebruikerByGebruikernaam(gebruikersnaam);
        } catch (EntityNotFoundException e) {
            return false;
        }

        if (g != null) {
            String passwoord = this.decrypt(g.getEncryptedPassword());
            if (passwoord.equals(wachtwoord)) {
                setIngelogdeGebruiker(g);
                return true;
            }
        }
        return false;
    }

    public void verwijderGebruiker(Gebruiker gebruiker) throws OngeldigeVerwijderingException {
        kalender.verwijderGebruiker(gebruiker);
    }

    public Gebruiker getIngelogdeGebruiker() {
        return ingelogdeGebruiker;
    }

    public void setIngelogdeGebruiker(Gebruiker ingelogdeGebruiker) {
        this.ingelogdeGebruiker = ingelogdeGebruiker;
    }

    public ObservableList<Inschrijving> geefInschrijvingenOpNaam(String naam) {
        SessieITLab sessie = (SessieITLab) this.sessieProperty.getValue();
        return sessie.geefInschrijvingenOpNaam(naam);
    }
}