using System;

namespace G002_project_dotnet.Models.Domain
{
    public class SessieKalender
    {
        public decimal Id { get; set; }
        public string Academiejaar { get; set; }
        public DateTime Einde { get; set; }
        public DateTime Start { get; set; }
    }
}
