using G002_project_dotnet.Models.Extensions;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

namespace G002_project_dotnet.Models.Domain
{
    public class Sessie
    {

        public Sessie()
        {
            Inschrijvingen = new HashSet<Inschrijving>();
            SessieMedia = new HashSet<SessieMedia>();
            SessieAankondiging = new HashSet<SessieAankondiging>();
            SessieInschrijving = new HashSet<SessieInschrijving>();
        }
        public decimal Id { get; set; }
        public string Beschrijving { get; set; }
        public int? Dagenopvoorhand { get; set; }
        public DateTime Einde { get; set; }
        public string Gastspreker { get; set; }
        public int? Maxplaatsen { get; set; }
        public string Mededeling { get; set; }
        public DateTime Start { get; set; }
        public SessieType Status { get; set; }
        public bool? Stuurherinnering { get; set; }
        public string Titel { get; set; }
        public decimal? LokaalId { get; set; }
        public decimal? VerantwoordelijkeId { get; set; }
        public Lokaal Lokaal { get; set; }
        public Gebruiker Verantwoordelijke { get; set; }
        public ICollection<Inschrijving> Inschrijvingen { get; set; }
        public ICollection<SessieMedia> SessieMedia { get; set; }
        public ICollection<SessieAankondiging> SessieAankondiging { get; set; }
        public ICollection<SessieInschrijving> SessieInschrijving { get; set; }

        public void VoegInschrijvingToe(Gebruiker gebruiker)
        {

            if(Inschrijvingen.Count < Maxplaatsen && gebruiker.Status == GebruikerStatus.ACTIEF)
            {
                if (Inschrijvingen.Any(i => i.Gebruiker == gebruiker))
                {
                    throw new ArgumentException($"{gebruiker.Naam} is reeds ingeschreven voor deze sessie");
                }
                Inschrijving inschrijving = new Inschrijving(gebruiker, this, DateTime.Now);
                Inschrijvingen.Add(inschrijving);
            } else
            {
                throw new InvalidOperationException();  
            }   
        }

        public Inschrijving GeefSessieInschrijving(Gebruiker g)
        {
            return Inschrijvingen.Where(i => i.Gebruiker == g).FirstOrDefault();
        }

        public void VerwijderInschrijving(Inschrijving inschrijving)
        {
            if (Inschrijvingen.Any(i => i.Gebruiker == inschrijving.Gebruiker))
            {
                Inschrijvingen.Remove(inschrijving);
            }
        }

        public string Schrijfstatus(string usermail)
        {
            if (Inschrijvingen.Any(i => i.Gebruiker.Email == usermail))
            {
                return "Uitschrijven";
            }
            return "Inschrijven";

        }

        public string KiesHuidigeMaand()
        {
            return DateTime.Now.Month.ToString();
        }

        public int GetOpenPlaatsen()
        {

            if (Status != SessieType.GESLOTEN && Status != SessieType.AFGELOPEN)
            {
                if (Maxplaatsen.HasValue)
                    return (int)Maxplaatsen - Inschrijvingen.Count();
                else
                    return (int)Lokaal.Maxplaatsen - Inschrijvingen.Count();
            }
            return 0;
        }

        public void ZetOpen()
        {
            if ((Start.TimeOfDay.TotalHours - DateTime.Now.TimeOfDay.TotalHours < 1) && (Start.Date == DateTime.Now.Date))
            {
                if (Status == SessieType.AANGEMAAKT)
                    Status = SessieType.OPEN;
            }
            else
            {
                throw new InvalidOperationException("Sessie mag pas een uur voor aanvang opengezet worden");
            }

        }

        public void Sluit()
        {
            if (Status == SessieType.OPEN)
                Status = SessieType.GESLOTEN;
        }

        public string GetDatumString()
        {
            return string.Format("{0} van {1} tot {2}", Start.ToLocalTime().ToLongDateString(), Start.ToString("HH:mm"), Einde.ToString("HH:mm"));

        }

        public string GetLokaalString()
        {
            return string.Format("{0} {1}{2}", Lokaal.Campus.ToFirstCharUpper(), Lokaal.Gebouw, Lokaal.Naam);
        }

        public string GetStatusString()
        {
            if (Status == SessieType.AFGELOPEN)
                return "Sessie is afgelopen";
            else if (Status == SessieType.GESLOTEN)
                return "Sessie is bezig";
            else
            {
                if (GetOpenPlaatsen() == 0)
                    return "Sessie is volzet";
                else if (GetOpenPlaatsen() == 1)
                    return "Nog 1 plaats";
                else
                    return "Nog " + GetOpenPlaatsen() + " plaatsen";
            }
        }

    }
}
