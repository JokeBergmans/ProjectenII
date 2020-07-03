using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace G002_project_dotnet.Models.Domain
{
    public interface IFeedbackRepository
    {

        public List<Feedback> GetBySessieId(int id);
        public void Add(Feedback feedback);
        void SaveChanges();

    }
}
