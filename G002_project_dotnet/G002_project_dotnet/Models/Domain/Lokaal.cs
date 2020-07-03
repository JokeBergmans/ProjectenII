using System.Collections.Generic;

namespace G002_project_dotnet.Models.Domain
{
    public class Lokaal
    {
        public Lokaal()
        {
            Sessies = new HashSet<Sessie>();
        }

        public decimal Id { get; set; }
        public string Campus { get; set; }
        public string Gebouw { get; set; }
        public int? Maxplaatsen { get; set; }
        public string Naam { get; set; }
        public string Opmerking { get; set; }
        public string Type { get; set; }

        public ICollection<Sessie> Sessies { get; set; }
    }
}
