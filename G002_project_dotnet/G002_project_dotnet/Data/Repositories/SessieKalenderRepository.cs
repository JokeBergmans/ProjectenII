using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace G002_project_dotnet.Data.Repositories
{
    public class SessieKalenderRepository : ISessieKalenderRepository
    {
        private readonly ApplicationDbContext _context;
        private readonly DbSet<SessieKalender> _sessieKalenders;

        public SessieKalenderRepository(ApplicationDbContext dbContext)
        {
            _context = dbContext;
            _sessieKalenders = dbContext.SessieKalenders;
        }

        public SessieKalender GetBy(int id)
        {
            return _sessieKalenders.SingleOrDefault(sk => sk.Id == id);
        }

        public SessieKalender GetByYear(int year)
        {
            return _sessieKalenders.FirstOrDefault(sk => sk.Start.Year <= year && sk.Einde.Year >= year);
        }
    }
}
