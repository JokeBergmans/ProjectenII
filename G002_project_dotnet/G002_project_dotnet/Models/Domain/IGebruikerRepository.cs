namespace G002_project_dotnet.Models.Domain
{
    public interface IGebruikerRepository
    {
        Gebruiker GetByEmail(string email);
        void Add(Gebruiker gebruiker);
        void SaveChanges();
    }
}
