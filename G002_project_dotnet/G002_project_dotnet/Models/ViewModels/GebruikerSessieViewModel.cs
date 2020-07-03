using G002_project_dotnet.Models.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace G002_project_dotnet.Models.ViewModels
{
    public class GebruikerSessieViewModel
    {
        public decimal Id { get; set; }
        public string Titel { get; set; }
        public string Gastspreker { get; set; }
        public string Datum { get; set; }
        public string Lokaal { get; set; }
        public bool Aanwezig { get; set; }
        public GebruikerSessieViewModel(Sessie sessie, Inschrijving inschrijving)
        {
            Id = sessie.Id;
            Titel = sessie.Titel;
            Gastspreker = sessie.Gastspreker;
            Datum = sessie.GetDatumString();
            Lokaal = sessie.GetLokaalString();
            if (inschrijving != null)
                Aanwezig = inschrijving.Aanwezig;
        }
    }
}
