using G002_project_dotnet.Models.Domain;
using G002_project_dotnet.Models.Extensions;
using G002_project_dotnet.Models.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;

namespace G002_project_dotnet.Controllers
{
    public class SessieController : Controller
    {
        private readonly ISessieRepository _sessieRepository;
        private readonly IGebruikerRepository _gebruikerRepository;
        private readonly IInschrijvingRepository _inschrijvingRepository;
        private readonly IFeedbackRepository _feedbackRepository;
        private readonly UserManager<IdentityUser> _userManager;

        public SessieController(ISessieRepository sessieRepository, 
            IGebruikerRepository gebruikerRepository, 
            IInschrijvingRepository inschrijvingRepository, 
            IFeedbackRepository feedbackRepository, 
            UserManager<IdentityUser> userManager)
        {
            _sessieRepository = sessieRepository;
            _gebruikerRepository = gebruikerRepository;
            _inschrijvingRepository = inschrijvingRepository;
            _feedbackRepository = feedbackRepository;
            _userManager = userManager;
        }

        [AllowAnonymous]
        public async Task<IActionResult> Index(int? maand)
        {

            if (!maand.HasValue)
                maand = DateTime.Today.Month;
            IEnumerable<Sessie> sessies;
            ViewData["maand"] = maand;
            ViewData["maandString"] = new CultureInfo("nl-BE").DateTimeFormat.GetMonthName(maand.Value).ToFirstCharUpper();

            if (!User.Identity.IsAuthenticated || User.IsInRole("gebruiker"))
            {
                sessies = _sessieRepository.GetAangemaakteSessies(maand.Value);
            }
            else if (User.IsInRole("verantwoordelijke"))
            {
                IdentityUser user = await _userManager.GetUserAsync(User);
                sessies = _sessieRepository.GetByVerantwoordelijke(maand.Value, _gebruikerRepository.GetByEmail(user.Email));
            }
            else
                sessies = _sessieRepository.GetByMonth(maand.Value);

            if (sessies.Count() == 0)
            {
                TempData["sessieError"] = "Er werden geen sessies gevonden";
                return View(null);
            }
            return View(sessies);

        }

        [AllowAnonymous]
        public IActionResult Detail(int id)
        {
            Sessie sessie = _sessieRepository.GetBy(id);
            if (sessie == null)
                return NotFound();

            if (User.Identity.IsAuthenticated)
            {
                veranderSchrijfStatus(sessie);
                System.Threading.Thread.Sleep(500);
            }


            return View(new SessieDetailsViewModel(sessie));
        }

        [Authorize(Policy = "Gebruiker")]
        public async void veranderSchrijfStatus(Sessie s)
        {
            IdentityUser user = await _userManager.GetUserAsync(User);
            Debug.WriteLine(user.ToString());
            ViewData["schrijfStatus"] = s.Schrijfstatus(_gebruikerRepository.GetByEmail(user.Email).Email);
        }


        [Authorize(Policy = "Gebruiker")]
        public async Task<IActionResult> SchrijfIn(int id)
        {
            Sessie sessie = _sessieRepository.GetBy(id);

            if (sessie != null)
            {
                IdentityUser user = await _userManager.GetUserAsync(User);
                try
                {
                    sessie.VoegInschrijvingToe(_gebruikerRepository.GetByEmail(user.Email));
                    _sessieRepository.SaveChanges();
                    TempData["message"] = $"Je bent nu ingeschreven voor sessie {sessie.Titel}";
                    ViewData["schrijfStatus"] = "Uitschrijven";
                }
                catch (ArgumentException)
                {
                    await SchrijfUit(id);
                }

                catch(InvalidOperationException)
                {
                    TempData["error"] = "De sessie is reeds volzet.";
                }
            }
            else
                TempData["error"] = "Er is iets fout gelopen";

            //string Path = HttpContext.Request.GetDisplayUrl().ToString();

            return RedirectToAction(nameof(Detail), new { id });
        }

        [Authorize(Policy = "Gebruiker")]
        public async Task<IActionResult> SchrijfUit(int id)
        {
            Sessie sessie = _sessieRepository.GetBy(id);
            if (sessie != null)
            {
                IdentityUser user = await _userManager.GetUserAsync(User);
                try
                {

                    Inschrijving inschrijving = new Inschrijving();
                    inschrijving = sessie.GeefSessieInschrijving(_gebruikerRepository.GetByEmail(user.Email));
                    sessie.VerwijderInschrijving(inschrijving);
                    _inschrijvingRepository.Delete(inschrijving);
                    _inschrijvingRepository.SaveChanges();
                    _sessieRepository.SaveChanges();
                    TempData["message"] = $"Je bent nu uitgeschreven voor sessie {sessie.Titel}";
                }
                catch (ArgumentException e)
                {
                    TempData["error"] = "Je bent niet ingeschreven voor deze sessie";
                }
            }
            else
                TempData["error"] = "Er is iets fout gelopen";
            return RedirectToAction(nameof(Detail), new { id });
        }

        [Authorize(Policy = "Verantwoordelijke")]
        public async Task<IActionResult> ZetOpen(int id)
        {
            Sessie sessie = _sessieRepository.GetBy(id);
            if (sessie != null)
            {
                IdentityUser user = await _userManager.GetUserAsync(User);
                if ((User.IsInRole("verantwoordelijke") && sessie.Verantwoordelijke.Email == user.Email) || User.IsInRole("hoofdverantwoordelijke"))
                {
                    try
                    {
                        sessie.ZetOpen();
                        _sessieRepository.SaveChanges();
                        TempData["message"] = $"De sessie {sessie.Titel} is nu openbaar";
                    }
                    catch (InvalidOperationException e)
                    {
                        TempData["error"] = e.Message;
                    }
                }
                else
                    TempData["error"] = "U heeft niet de juiste bevoegdheid om deze sessie te openen";
            }
            else
                TempData["error"] = "Er is iets fout gelopen";
            return RedirectToAction(nameof(Detail), new { id });
        }

        [Authorize(Policy = "Verantwoordelijke")]
        public async Task<IActionResult> Sluit(int id)
        {
            Sessie sessie = _sessieRepository.GetBy(id);
            if (sessie != null)
            {
                IdentityUser user = await _userManager.GetUserAsync(User);
                if ((User.IsInRole("verantwoordelijke") && sessie.Verantwoordelijke.Email == user.Email) || User.IsInRole("hoofdverantwoordelijke"))
                {
                    sessie.Sluit();
                    _sessieRepository.SaveChanges();
                    TempData["message"] = $"De sessie {sessie.Titel} is nu gesloten";
                }
                else
                    TempData["error"] = "U heeft niet de juiste bevoegdheid om deze sessie te sluiten";
            }
            else
                TempData["error"] = "Er is iets fout gelopen";
            return RedirectToAction(nameof(Detail), new { id });
        }

        [AllowAnonymous]
        public IActionResult Aanwezigheden(int id)
        {
            IEnumerable<Inschrijving> inschrijvingen = _inschrijvingRepository.GetBySessie(id);
            Sessie sessie = _sessieRepository.GetBy(id);
            ViewData["titel"] = sessie.Titel;
            List<InschrijvingViewModel> inschrijvingViewModels = new List<InschrijvingViewModel>();

            foreach (Inschrijving i in inschrijvingen)
            {
                inschrijvingViewModels.Add(new InschrijvingViewModel(i));

            }
            if (inschrijvingen.Count() == 0)
                TempData["errorInschrijvingen"] = "Er zijn momenteel geen inschrijvingen voor deze sessie.";

            return View(inschrijvingViewModels);
        }

        [HttpPost]
        public IActionResult WijzigInschrijvingen(List<InschrijvingViewModel> inschrijvingenModels)
        {
            try
            {
                foreach (InschrijvingViewModel vm in inschrijvingenModels)
                {
                    Inschrijving i = _inschrijvingRepository.GetBy(vm.Id);
                    i.Aanwezig = vm.Aanwezig;
                    if (i.Aanwezig)
                        i.Status = "AANWEZIG";
                    else
                        i.Status = "AFWEZIG";
                }
                _inschrijvingRepository.SaveChanges();
                TempData["message"] = $"De aanwezigheden werden succesvol opgeslagen.";
            }
            catch
            {
                TempData["error"] = $"Sorry, er ging iets mis bij het updaten van de aanwezigheden. De aanwezigheden werden niet opgeslagen.";
            }
            return RedirectToAction(nameof(Index));
        }

        [AllowAnonymous]
        public async Task<IActionResult> GebruikerIndex()
        {
            IEnumerable<Sessie> sessies;
            IList<GebruikerSessieViewModel> sessieViewModels = new List<GebruikerSessieViewModel>();
            IdentityUser user = await _userManager.GetUserAsync(User);
            Gebruiker g = _gebruikerRepository.GetByEmail(user.Email);

            if (!User.Identity.IsAuthenticated || User.IsInRole("gebruiker"))
                sessies = _sessieRepository.GetAfgelopenSessiesGebruiker(g);
            else if (User.IsInRole("verantwoordelijke"))
                sessies = _sessieRepository.GetAfgelopenSessieVerantwoordelijke(g);
            else
                sessies = _sessieRepository.GetAfgelopenSessies();

            if (sessies.Count() == 0)
            {
                TempData["error"] = "Er werden geen sessies gevonden";
                return View(null);
            }

            foreach(Sessie s in sessies)
                sessieViewModels.Add(new GebruikerSessieViewModel(s, _inschrijvingRepository.GetByEmailAndSessieId(user.Email, (int) s.Id)));
            return View(sessieViewModels);

        }

        public IActionResult RegistreerAanwezigheden(int id)
        {

            Debug.WriteLine(id);

            Sessie s = _sessieRepository.GetBy(id);

            ViewData["titel"] = s.Titel;

            return View(new RegistreerAanwezigheidViewModel(id));
        }

        [HttpPost]
        public IActionResult RegistreerAanwezigheid(RegistreerAanwezigheidViewModel rvm)
        {

            Inschrijving i = _inschrijvingRepository.GetByEmailAndSessieId(rvm.Email, rvm.SessieID);

            if (i != null)
            {
                if (i.Aanwezig)
                    TempData["message"] = "Je bent reeds geregistreerd voor deze sessie.";
                else
                {
                    i.Aanwezig = true;
                    TempData["message"] = "Je bent succesvol geregistreerd voor deze sessie.";
                }
            }
            else
                TempData["error"] = "Je bent niet ingeschreven voor deze sessie of je e-mailadres is ongeldig, probeer opnieuw";

            _inschrijvingRepository.SaveChanges();

            return RedirectToAction(nameof(RegistreerAanwezigheden), new { id = rvm.SessieID });
        }


        [Authorize(Policy = "Gebruiker")]
        public IActionResult SchrijfFeedback(int id)
        {
            Sessie sessie = _sessieRepository.GetBy(id);
            if (sessie == null)
                return NotFound();

            return View(new SessieDetailsViewModel(sessie));
        }

        [HttpPost]
        [Authorize(Policy = "Gebruiker")]
        public IActionResult VerstuurFeedback(int id, string feedback) {

            if(feedback.Length > 1000)
            {
                TempData["error"] = "De feedback mag niet langer zijn dan 1000 tekens.";
                return RedirectToAction(nameof(VerstuurFeedback));
            }
            
            else
            {
                Feedback fb = new Feedback() {
                    SessieId = id,
                    Tekst = feedback
                };
              
                _feedbackRepository.Add(fb);
                _feedbackRepository.SaveChanges(); 
                TempData["message"] = "Je feedback werd succesvol toegevoegd.";
                return RedirectToAction(nameof(GebruikerIndex));
            }
        }

        public IActionResult BekijkFeedback(int id)
        {

            Sessie s = _sessieRepository.GetBy(id);
            ViewData["titel"] = s.Titel;

            List<Feedback> fb = _feedbackRepository.GetBySessieId(id);
            List<string> inhoud = new List<string>();

            if(fb.Count == 0)
                TempData["error"] = "Er werd nog geen feedback gegeven op deze sessie.";
            foreach (Feedback f in fb)
                inhoud.Add(f.Tekst);

            return View(new FeedbackViewModel() { Feedback = inhoud }); 
        }
    }
}