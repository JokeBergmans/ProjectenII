using Microsoft.AspNetCore.Hosting;

[assembly: HostingStartup(typeof(G002_project_dotnet.Areas.Identity.IdentityHostingStartup))]
namespace G002_project_dotnet.Areas.Identity
{
    public class IdentityHostingStartup : IHostingStartup
    {
        public void Configure(IWebHostBuilder builder)
        {
            builder.ConfigureServices((context, services) =>
            {
            });
        }
    }
}