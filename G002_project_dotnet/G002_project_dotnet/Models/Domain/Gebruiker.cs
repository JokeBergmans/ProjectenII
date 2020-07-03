using System.Collections.Generic;

namespace G002_project_dotnet.Models.Domain
{
    public class Gebruiker
    {
        public Gebruiker()
        {
            Aankondigingen = new HashSet<Aankondiging>();
            Feedback = new HashSet<Feedback>();
            Inschrijvingen = new HashSet<Inschrijving>();
            Sessies = new HashSet<Sessie>();
        }

        public decimal Id { get; set; }
        public int? Aantalaanwezig { get; set; }
        public int? Aantalafwezig { get; set; }
        public int? Aantalingeschreven { get; set; }
        public string Encryptedpassword { get; set; }
        public string Gebruikersnaam { get; set; }
        public string Naam { get; set; }
        public byte[] Profielfoto { get; set; }
        public GebruikerStatus Status { get; set; }
        public GebruikerType Type { get; set; }
        public string Email { get; set; }

        public ICollection<Aankondiging> Aankondigingen { get; set; }
        public ICollection<Feedback> Feedback { get; set; }
        public ICollection<Inschrijving> Inschrijvingen { get; set; }
        public ICollection<Sessie> Sessies { get; set; }

        #region Contructors 

        public Gebruiker(string naam, string gebruikersnaam, GebruikerStatus status, GebruikerType type)
        {
            Naam = naam;
            Gebruikersnaam = gebruikersnaam;
            Status = status;
            Type = type;
        }

        public Gebruiker(string firstName, string lastName, string email, string encryptedPassword)
        {
            Naam = firstName + " " + lastName;
            Gebruikersnaam = email;
            Email = email;
            Encryptedpassword = encryptedPassword;
            Status = GebruikerStatus.ACTIEF;
            Type = GebruikerType.GEBRUIKER;
        }
        #endregion
    }
}
