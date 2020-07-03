namespace G002_project_dotnet.Models.Domain
{
    public class SessieMedia
    {
        public int Id { get; set; }
        public decimal? SessieId { get; set; }
        public string Media { get; set; }

        public Sessie Sessie { get; set; }
    }
}
