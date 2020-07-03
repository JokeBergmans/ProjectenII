using System.Collections.Generic;

namespace G002_project_dotnet.Models.Domain
{
    public interface IInschrijvingRepository
    {
        IEnumerable<Inschrijving> GetBySessie(int sessieId);
        Inschrijving GetBy(decimal inschrijvingId);
        void SaveChanges();
        Inschrijving GetByEmailAndSessieId(string email, int sessieID);

        void Delete(Inschrijving i); 
    }
}
