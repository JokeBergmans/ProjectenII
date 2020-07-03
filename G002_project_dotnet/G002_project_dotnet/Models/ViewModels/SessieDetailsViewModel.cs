using G002_project_dotnet.Models.Domain;
using System;

namespace G002_project_dotnet.Models.ViewModels
{
    public class SessieDetailsViewModel
    {
        public string Beschrijving { get; set; }
        public string Gastspreker { get; set; }
        public int OpenPlaatsen { get; set; }
        public string Status { get; set; }
        public string StatusString { get; set; }
        public string Titel { get; set; }
        public string Lokaal { get; set; }
        public Gebruiker Verantwoordelijke { get; set; }
        public Decimal Id { get; set; }
        public string Datum { get; set; }
        public DateTime Start { get; set; }

        public SessieDetailsViewModel(Sessie s)
        {
            Beschrijving = s.Beschrijving;
            Gastspreker = s.Gastspreker;
            OpenPlaatsen = s.GetOpenPlaatsen();
            Status = s.Status.ToString();
            StatusString = s.GetStatusString();
            Titel = s.Titel;
            Lokaal = s.GetLokaalString();
            Datum = s.GetDatumString();
            Start = s.Start;
            Id = s.Id;
            Verantwoordelijke = s.Verantwoordelijke;
        }
    }
}
