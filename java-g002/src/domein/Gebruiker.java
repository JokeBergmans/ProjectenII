package domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Gebruiker.zoekOpGebruikerNaam", query = "SELECT g FROM Gebruiker g where g.gebruikersnaam = :naamGebruiker"),
        @NamedQuery(name = "Gebruiker.geefAlle", query =  "SELECT g FROM Gebruiker  g")
})
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private GebruikerType type;

    private String naam;
    private String gebruikersnaam;
    private String email;

    @Enumerated(EnumType.STRING)
    private GebruikersStatusType status;

    @OneToMany(mappedBy = "publicist")
    private List<Aankondiging> aankondigingen;

    @OneToMany(mappedBy = "auteur")
    private List<Feedback> feedbacks;

    private int aantalIngeschreven;

    private int aantalAanwezig;

    private int aantalAfwezig;

    @Lob
    private byte[] profielFoto;

    private String encryptedPassword;

    public int getAantalIngeschreven() {
        return aantalIngeschreven;
    }


    public Gebruiker(GebruikerType type, String naam, String email) {
        this.type = type;
        this.naam = naam;
        this.gebruikersnaam = email;
        this.status = GebruikersStatusType.ACTIEF;
        this.aankondigingen = new ArrayList<>();
        this.aantalIngeschreven = 0;
        this.aantalAanwezig = 0;
        this.aantalAfwezig = 0;
        this.email = email;
    }

    public Gebruiker(GebruikerType type, String naam, String email, GebruikersStatusType status, String encryptedPassword) {
        setType(type);
        setNaam(naam);
        setStatus(status);
        setGebruikersnaam(email);
        setEmail(email);
        setEncryptedPassword(encryptedPassword);
        this.aankondigingen = new ArrayList<>();
        this.aantalIngeschreven = 0;
        this.aantalAanwezig = 0;
        this.aantalAfwezig = 0;

    }



    public Gebruiker(GebruikerType type, String naam, String email, GebruikersStatusType status, byte[] foto) {
        setType(type);
        setNaam(naam);
        setStatus(status);
        setGebruikersnaam(email);
        setEmail(email);
        setProfielFoto(foto);
        this.aankondigingen = new ArrayList<>();
        this.aantalIngeschreven = 0;
        this.aantalAanwezig = 0;
        this.aantalAfwezig = 0;

    }

    public Gebruiker(GebruikerType type, String naam, String email, GebruikersStatusType status, String encryptedPassword, byte[] foto) {
        setType(type);
        setNaam(naam);
        setStatus(status);
        setGebruikersnaam(email);
        setEmail(email);
        setEncryptedPassword(encryptedPassword);
        setProfielFoto(foto);
        this.aankondigingen = new ArrayList<>();
        this.aantalIngeschreven = 0;
        this.aantalAanwezig = 0;
        this.aantalAfwezig = 0;

    }
    public Gebruiker() {
    }

    public GebruikerType getType() {
        return type;
    }

    public String getNaam() {
        return naam;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public GebruikersStatusType getStatus() {
        return status;
    }

    public void setGebruikersnaam(String gebruikersnaam){
        this.gebruikersnaam = gebruikersnaam;
    }
    private void setEmail(String email) {
        this.email = email;
    }
    public void setEncryptedPassword(String encryptedPassword){
        this.encryptedPassword = encryptedPassword;
    }

    public void setType(GebruikerType type) {
        this.type = type;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setStatus(GebruikersStatusType status) {
        this.status = status;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }


    //Sessies bijhouden
    //public List<Sessie> getSessie(){return sessies;}

    public void setAantalIngeschreven(int aantalIngeschreven) {
        this.aantalIngeschreven = aantalIngeschreven;
    }

    public int getAantalAanwezig() {
        return aantalAanwezig;
    }

    public void setAantalAanwezig(int aantalAanwezig) {
        this.aantalAanwezig = aantalAanwezig;
    }


    public int getAantalAfwezig() {
        return aantalAfwezig;
    }

    public void setAantalAfwezig(int aantalAfwezig) {
        this.aantalAfwezig = aantalAfwezig;
    }

    public void incrementInschrijving() {
        this.aantalIngeschreven++;
    }

    public void incrementAanwezig() {
        this.aantalAanwezig++;
    }

    public void verminderAfwezig() {
        this.aantalAfwezig--;
    }

    public void incrementAfwezig() {
        this.aantalAfwezig++;
    }

    public void verminderAanwezig() {
        this.aantalAanwezig--;
    }

    public byte[] getProfielFoto() {
        return profielFoto;
    }

    public void setProfielFoto(byte[] imageBytes) {
        this.profielFoto = imageBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gebruiker gebruiker = (Gebruiker) o;

        if(type != gebruiker.type) return false;
        if(!naam.equals(gebruiker.naam)) return false;
        return gebruikersnaam.equals(gebruiker.gebruikersnaam);
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + naam.hashCode();
        result = 31 * result + gebruikersnaam.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "Gebruiker{" +
                "type=" + type +
                ", naam='" + naam + '\'' +
                ", gebruikersnaam='" + gebruikersnaam + '\'' +
                ", status=" + status +
                '}';
    }
}
