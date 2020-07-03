using G002_project_dotnet.Models.Domain;
using System;

namespace G002_project_dotnet.Models.ViewModels
{
    public class InschrijvingViewModel
    {
        public decimal Id { get; set; }
        public bool Aanwezig { get; set; }
        public Gebruiker Gebruiker { get; set; }
        public DateTime Datum { get; set; }
        public InschrijvingViewModel(Inschrijving i)
        {
            Id = i.Id;
            Aanwezig = i.Aanwezig;
            Gebruiker = i.Gebruiker;
            Datum = i.Datum;
        }

        public InschrijvingViewModel()
        {

        }
    }
}
