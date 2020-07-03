using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace G002_project_dotnet.Data.Repositories
{
    public class FeedbackRepository : IFeedbackRepository
    {

        private readonly ApplicationDbContext _context;
        private readonly DbSet<Feedback> _feedback;

        public FeedbackRepository(ApplicationDbContext dbContext)
        {
            _context = dbContext;
            _feedback = dbContext.Feedback;
        }
        public void Add(Feedback feedback)
        {
            _feedback.Add(feedback); 
        }

        public List<Feedback> GetBySessieId(int id)
        {
            return _feedback.Where(f => f.SessieId == id).ToList();
        }
        public void SaveChanges()
        {
            _context.SaveChanges();
        }
    }
}
