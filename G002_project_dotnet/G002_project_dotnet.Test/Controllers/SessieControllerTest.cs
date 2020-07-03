using G002_project_dotnet.Controllers;
using G002_project_dotnet.Test.Data;
using G002_project_dotnet.Models.ViewModels;
using G002_project_dotnet.Models.Domain;
using Xunit;
using Moq;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc.ViewFeatures;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using System.Linq;

namespace G002_project_dotnet.Test.Controllers
{
    public class SessieControllerTest
    {
        private readonly SessieController _controller;
        private readonly DummyDbContext _context;
        private readonly Mock<IFeedbackRepository> _feedbackRepository;
        private readonly Mock<IGebruikerRepository>_gebruikerRepository;
        private readonly Mock<IInschrijvingRepository> _inschrijvingRepository;
        private readonly Mock<ISessieKalenderRepository> _sessiekalenderRepository;
        private readonly Mock<ISessieRepository> _sessieRepository;
        private readonly SessieDetailsViewModel model;
        private readonly UserManager<IdentityUser> _userManager;


        public SessieControllerTest()
        {
            _context = new DummyDbContext();
            _feedbackRepository = new Mock<IFeedbackRepository>();
            _gebruikerRepository = new Mock<IGebruikerRepository>();
            _inschrijvingRepository = new Mock<IInschrijvingRepository>();
            _sessiekalenderRepository = new Mock<ISessieKalenderRepository>();
            _sessieRepository = new Mock<ISessieRepository>();
            
            _controller = new SessieController(_sessieRepository.Object, _gebruikerRepository.Object, _inschrijvingRepository.Object, _feedbackRepository.Object, _userManager)
            {
                TempData = new Mock<ITempDataDictionary>().Object
            };
            model = new SessieDetailsViewModel(_context.sessie1)
            {
                Beschrijving = "Linux sucks",
                Datum = _context.dag.ToString(),
                Id = 3,
                Titel = "Why Linux sucks",
                OpenPlaatsen = 20

            };
        }

        [Fact]
        public void Index_MaandIsNull_GeeftModelMetGeenMaandDoor()
        {
            _sessieRepository.Setup(s => s.GetAll()).Returns(_context.sessies);
            var actionResult = Assert.IsType<ViewResult>(_controller.Index(null));
            var sessies = Assert.IsAssignableFrom<IEnumerable<Sessie>>(actionResult.Model);
            Assert.Empty(sessies);
        }

        [Fact]
        public void Index_GeeftSessiesVanMaandMeiDoorViaSessieViews()
        {
            _sessieRepository.Setup(s => s.GetByMonth(5)).Returns(_context.sessies);
            var actionResult = Assert.IsType<ViewResult>(_controller.Index(5));
            var sessies = Assert.IsAssignableFrom<IEnumerable<Sessie>>(actionResult.Model);
            Assert.Equal(5, sessies.Count());
        }


        [Fact]
        public void SchrijfIn_GeeftInschrijvingViewModelDoorAanView() {
            _sessieRepository.Setup(v => v.GetBy(1)).Returns(_context.sessie1);
            var actionResult = Assert.IsType<ViewResult>(_controller.SchrijfIn(1));
            Assert.IsType<InschrijvingViewModel>(actionResult.Model);
        }

        [Fact]
        public void SchrijfUit_GeeftUitschrijvingViewModelDoorAanView()
        {
            _sessieRepository.Setup(v => v.GetBy(1)).Returns(_context.sessie1);
            var actionResult = Assert.IsType<ViewResult>(_controller.SchrijfUit(1));
            Assert.IsType<InschrijvingViewModel>(actionResult.Model);
        }

        [Fact]
        public void Inschrijving_GeldigeSessieGegevens()
        {
            _inschrijvingRepository.Setup(b => b.GetBy(3)).Returns(_context.inschrijving);
            _sessieRepository.Setup(v => v.GetBy(1)).Returns(_context.sessie1);
            var result = Assert.IsType<RedirectToActionResult>(_controller.SchrijfIn(1));
            Assert.Equal("Index", result.ActionName);
        }

        [Fact]
        public void Index_GeeftMaandDoorViaViewData()
        {
            _sessieRepository.Setup(v => v.GetByMonth(5)).Returns(_context.sessie5);
            var actionResult = Assert.IsType<ViewResult>(_controller.Index(20));
            Assert.Equal(20, actionResult.ViewData["aantalPersonen"]);
        }
    }
}
