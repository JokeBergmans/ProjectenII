using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;

namespace G002_project_dotnet.Data.Repositories
{
    public class InschrijvingRepository : IInschrijvingRepository
    {

        private readonly ApplicationDbContext _context;
        private readonly DbSet<Inschrijving> _inschrijvingen;


        public InschrijvingRepository(ApplicationDbContext dbContext)
        {
            _context = dbContext;
            _inschrijvingen = dbContext.Inschrijvingen;
        }

        public void Delete(Inschrijving i)
        {
            _inschrijvingen.Remove(i);
        }

        public Inschrijving GetBy(decimal inschrijvingId)
        {
            return _inschrijvingen.Include(i => i.Gebruiker).SingleOrDefault(i => i.Id == inschrijvingId);
        }

        public Inschrijving GetByEmailAndSessieId(string email, int sessieID)
        {
            return _inschrijvingen.Include(i => i.Gebruiker).SingleOrDefault(i => i.Gebruiker.Email == email && i.SessieId == sessieID);
        }

        public IEnumerable<Inschrijving> GetBySessie(int sessieId)
        {
            return _inschrijvingen.Include(i => i.Gebruiker).Where(i => i.SessieId == sessieId).OrderBy(i => i.Gebruiker.Naam);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }
    }
}
