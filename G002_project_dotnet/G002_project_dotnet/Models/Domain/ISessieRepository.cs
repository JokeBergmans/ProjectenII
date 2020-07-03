using System;
using System.Collections.Generic;

namespace G002_project_dotnet.Models.Domain
{
    public interface ISessieRepository
    {
        Sessie GetBy(int id);
        IEnumerable<Sessie> GetAll();
        IEnumerable<Sessie> GetByMonth(int maand);
        void Update(Sessie sessie);
        IEnumerable<Sessie> GetAfgelopenSessies();
        IEnumerable<Sessie> GetAfgelopenSessiesGebruiker(Gebruiker g);
        IEnumerable<Sessie> GetAfgelopenSessieVerantwoordelijke(Gebruiker g);
        IEnumerable<Sessie> GetAangemaakteSessies(int maand);
        IEnumerable<Sessie> GetByVerantwoordelijke(int maand, Gebruiker g);
        void SaveChanges();
    }
}
