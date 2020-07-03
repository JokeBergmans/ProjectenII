namespace G002_project_dotnet.Models.ViewModels
{
    public class RegistreerAanwezigheidViewModel
    {

        public string Email { get; set; }

        public int SessieID { get; set; }
        public RegistreerAanwezigheidViewModel(int sessieID)
        {
            SessieID = sessieID;
        }

        public RegistreerAanwezigheidViewModel()
        {

        }
    }
}
