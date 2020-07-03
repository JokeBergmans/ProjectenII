namespace G002_project_dotnet.Models.Domain
{
    public class Feedback
    {

        public decimal Id { get; set; }

        public decimal SessieId { get; set; }
        public string Tekst { get; set; }
        public decimal? AuteurId { get; set; }

        public Gebruiker Auteur { get; set; }

        public Feedback()
        {

        }
    }
}
