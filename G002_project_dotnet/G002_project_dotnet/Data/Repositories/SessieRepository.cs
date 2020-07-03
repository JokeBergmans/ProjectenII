using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;

namespace G002_project_dotnet.Data.Repositories
{
    public class SessieRepository : ISessieRepository
    {
        private readonly ApplicationDbContext _context;
        private readonly DbSet<Sessie> _sessies;

        public SessieRepository(ApplicationDbContext dbContext)
        {
            _context = dbContext;
            _sessies = dbContext.Sessies;
        }

        public IEnumerable<Sessie> GetAll()
        {
            return _sessies.Include(s => s.Lokaal).Include(s => s.Verantwoordelijke).Include(s => s.Inschrijvingen).ThenInclude(i => i.Gebruiker).OrderBy(s => s.Start).ToList();
        }

        public Sessie GetBy(int id)
        {
            return _sessies.Include(s => s.Lokaal).Include(s => s.Verantwoordelijke).Include(s => s.Inschrijvingen).ThenInclude(i => i.Gebruiker).SingleOrDefault(r => r.Id == id);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }


        public void Update(Sessie sessie)
        {
            _context.Update(sessie);
        }

        public IEnumerable<Sessie> GetByMonth(int maand)
        {
            return _sessies.Include(s => s.Lokaal).Include(s => s.Inschrijvingen).ThenInclude(i => i.Gebruiker).Where(r => r.Start.Month.Equals(maand)).OrderBy(s => s.Start).ToList();

        }

        public IEnumerable<Sessie> GetAfgelopenSessies()
        {
            return GetAll().Where(s => s.Status == SessieType.AFGELOPEN).ToList();
        }

        public IEnumerable<Sessie> GetAfgelopenSessiesGebruiker(Gebruiker g)
        {
            return GetAll().Where(s => s.Status == SessieType.AFGELOPEN && s.Inschrijvingen.Any(i => i.Gebruiker == g)).ToList();
        }

        public IEnumerable<Sessie> GetAfgelopenSessieVerantwoordelijke(Gebruiker g)
        {
            return GetAll().Where(s => s.Verantwoordelijke == g).ToList();
        }

        public IEnumerable<Sessie> GetAangemaakteSessies(int maand)
        {
            return GetByMonth(maand).Where(s => s.Status == SessieType.AANGEMAAKT).ToList();
        }

        public IEnumerable<Sessie> GetByVerantwoordelijke(int maand, Gebruiker g)
        {
            return GetByMonth(maand).Where(s => s.Verantwoordelijke == g).ToList();
        }
    }
}
