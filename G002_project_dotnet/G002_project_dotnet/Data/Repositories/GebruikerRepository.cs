using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace G002_project_dotnet.Data.Repositories
{
    public class GebruikerRepository : IGebruikerRepository
    {
        private readonly ApplicationDbContext _context;
        private readonly DbSet<Gebruiker> _gebruikers;

        public GebruikerRepository(ApplicationDbContext dbContext)
        {
            _context = dbContext;
            _gebruikers = dbContext.Gebruikers;
        }
        public Gebruiker GetByEmail(string email)
        {
            return _gebruikers.FirstOrDefault(g => g.Email == email);
        }

        public void Add(Gebruiker gebruiker)
        {
            _gebruikers.Add(gebruiker);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }
    }
}
