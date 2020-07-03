using System;
using System.Collections.Generic;
using System.Linq;
using G002_project_dotnet.Models.Domain;

namespace G002_project_dotnet.Test.Data
{
    public class DummyDbContext
    {
        public ICollection<Sessie> sessies { get; }
        public IEnumerable<Sessie> sessie5 => sessies.Where(v => v.Start.Month == 5);
        //public ICollection<Inschrijving> inschrijvingen { get; }
        public Gebruiker Jan { get;  }
        public Gebruiker Bart { get; }
        public Sessie sessie1 { get; }
        public Feedback feedback { get; }
        public Inschrijving inschrijving { get; }
        public DateTime dag { get; }

        public DummyDbContext()
        {
            sessies = new List<Sessie>();
            Jan = new Gebruiker()
            {
                Naam = "Jan",
                Email = "Jan@hogent.be",
                Gebruikersnaam = "jan1234",
                Id = 1,
                Type = GebruikerType.GEBRUIKER
            };
            Bart = new Gebruiker()
            {
                Naam = "Bart",
                Email = "Bart@hogent.be",
                Gebruikersnaam = "bart1234",
                Id = 2,
                Type = GebruikerType.GEBRUIKER
            };

            sessie1 = new Sessie()
            {
                Id = 1,
                Beschrijving = "deze sessie gaat over Linux",
                Gastspreker = "Bill Gates",
                Maxplaatsen = 20,
                Start = DateTime.Now,
                Einde = DateTime.Now.AddDays(1),
                Status = SessieType.AANGEMAAKT,
                Titel = "Why Linux sucks"
            };

            feedback = new Feedback()
            {
                Auteur = Jan,
                AuteurId = 1,
                Id = 1,
                SessieId = 1,
                Tekst = "het was een interessant sessie"
            };
            dag = new DateTime(DateTime.Now.Day);

            sessie1 = sessies.First();
            inschrijving = new Inschrijving(){
                Datum = dag,
                Gebruiker = Jan,
                GebruikerId = Jan.Id,
                Sessie = sessie1,
                Id = 1,
                SessieId = sessie1.Id,
                Status = "INGESCHREVEN"
            };
            sessie1.VoegInschrijvingToe(Jan);
            sessies.Add(sessie1);
            inschrijving = new Inschrijving()
            {
                Datum = dag,
                Gebruiker = Bart,
                GebruikerId = Bart.Id,
                Sessie = sessie1,
                Id = 1,
                SessieId = sessie1.Id,
                Status = "INGESCHREVEN"
            };
            sessie1.VoegInschrijvingToe(Bart);
            

        }
        
    }
}
