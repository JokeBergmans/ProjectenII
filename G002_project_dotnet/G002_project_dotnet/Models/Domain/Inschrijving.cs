using System;
using System.Collections.Generic;

namespace G002_project_dotnet.Models.Domain
{
    public class Inschrijving
    { 
        public Inschrijving()
        {
            SessieInschrijvingen = new HashSet<SessieInschrijving>();
        }

        public Inschrijving(Gebruiker gebruiker, Sessie sessie, DateTime date)
        {
            Gebruiker = gebruiker;
            Sessie = sessie;
            Datum = date;
            Status = "INGESCHREVEN";
        }

        public decimal Id { get; set; }
        public bool Aanwezig { get; set; }
        public DateTime Datum { get; set; }
        public string Status { get; set; }
        public decimal? GebruikerId { get; set; }
        public decimal? SessieId { get; set; }

        public virtual Gebruiker Gebruiker { get; set; }
        public virtual Sessie Sessie { get; set; }
        public virtual ICollection<SessieInschrijving> SessieInschrijvingen { get; set; }

     
    }
}
