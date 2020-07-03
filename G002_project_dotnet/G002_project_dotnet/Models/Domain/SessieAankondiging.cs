namespace G002_project_dotnet.Models.Domain
{
    public class SessieAankondiging
    {
        public decimal SessieId { get; set; }
        public decimal AankondigingId { get; set; }

        public Aankondiging Aankondiging { get; set; }
        public Sessie Sessie { get; set; }
    }
}
