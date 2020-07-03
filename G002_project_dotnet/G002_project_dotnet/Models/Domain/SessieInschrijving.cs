namespace G002_project_dotnet.Models.Domain
{
    public class SessieInschrijving
    {
        public decimal SessieId { get; set; }
        public decimal InschrijvingId { get; set; }

        public Inschrijving Inschrijving { get; set; }
        public Sessie Sessie { get; set; }

    }
}
