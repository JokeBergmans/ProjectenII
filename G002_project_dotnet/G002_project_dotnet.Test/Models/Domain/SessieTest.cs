using System;
using System.Collections.Generic;
using System.Linq;
using G002_project_dotnet.Models.Domain;
using G002_project_dotnet.Test.Data;
using Xunit;

namespace G002_project_dotnet.Test.Models.Domain
{
    public class SessieTest
    {
        private Gebruiker _gebruiker;
        private Sessie _sessie;
        private ICollection<Inschrijving> _inschrijvingen;
        private DummyDbContext _context;

        public SessieTest()
        {
            _context = new DummyDbContext();
            _sessie = _context.sessie1;
            _gebruiker = _context.Jan;
            _inschrijvingen = _context.sessie1.Inschrijvingen;
        }

        [Fact]
        public void SessieVoegNieuweInschrijvingToe()
        {
            _sessie.VoegInschrijvingToe(_gebruiker);
            Assert.Equal(1, _sessie.Inschrijvingen.Count);
        }


        [Fact]
        public void InschrijvenSessieVerledenTijd()
        {
            _sessie.Start = DateTime.Now.AddMonths(-3);
            Assert.Throws<Exception>(
                () => _sessie.VoegInschrijvingToe(_gebruiker));
        }       
    }
}
