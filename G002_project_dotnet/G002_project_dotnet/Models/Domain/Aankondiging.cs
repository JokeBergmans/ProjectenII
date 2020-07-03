using System;
using System.Collections.Generic;

namespace G002_project_dotnet.Models.Domain
{
    public class Aankondiging
    {
        public Aankondiging()
        {
            SessieAankondiging = new HashSet<SessieAankondiging>();
        }

        public decimal Id { get; set; }
        public DateTime Datum { get; set; }
        public string Tekst { get; set; }
        public decimal? PublicistId { get; set; }

        public Gebruiker Publicist { get; set; }
        public ICollection<SessieAankondiging> SessieAankondiging { get; set; }
    }
}
