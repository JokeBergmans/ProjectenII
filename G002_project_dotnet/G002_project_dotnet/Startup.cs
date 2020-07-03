using G002_project_dotnet.Data;
using G002_project_dotnet.Data.Repositories;
using G002_project_dotnet.Models.Domain;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System;
using System.Security.Claims;

namespace G002_project_dotnet
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllersWithViews()
                .AddRazorRuntimeCompilation();

            services.AddDbContext<ApplicationDbContext>(options =>
                options.UseSqlServer(
                    Configuration.GetConnectionString("DefaultConnection")));

            services.AddDefaultIdentity<IdentityUser>(options => options.SignIn.RequireConfirmedAccount = false)
                .AddEntityFrameworkStores<ApplicationDbContext>();

            services.Configure<IdentityOptions>(options =>
            {
                // Default Password settings.
                options.Password.RequireDigit = false;
                options.Password.RequireLowercase = false;
                options.Password.RequireNonAlphanumeric = false;
                options.Password.RequireUppercase = false;
                options.Password.RequiredLength = 4;
                options.Password.RequiredUniqueChars = 1;
            });


            /*            services.AddAuthentication(AzureADDefaults.AuthenticationScheme)
                            .AddAzureAD(options => Configuration.Bind("AzureAd", options));

                        services.Configure<OpenIdConnectOptions>(AzureADDefaults.OpenIdScheme, options =>
                        {
                            options.Authority = options.Authority + "/v2.0/";
                            options.TokenValidationParameters.ValidateIssuer = false;
                            options.SignInScheme = IdentityConstants.ExternalScheme;
                        });*/

            services.AddAuthorization(options =>
            {
                options.AddPolicy("Hoofdverantwoordelijke", policy => policy.RequireClaim(ClaimTypes.Role, "hoofdverantwoordelijke"));
                options.AddPolicy("Verantwoordelijke", policy => policy.RequireClaim(ClaimTypes.Role, "verantwoordelijke", "hoofdverantwoordelijke"));
                options.AddPolicy("Gebruiker", policy => policy.RequireClaim(ClaimTypes.Role, "gebruiker", "verantwoordelijke", "hoofdverantwoordelijke"));
            });

            services.AddScoped<DataInitializer>();
            services.AddScoped<ISessieRepository, SessieRepository>();
            services.AddScoped<ISessieKalenderRepository, SessieKalenderRepository>();
            services.AddScoped<IGebruikerRepository, GebruikerRepository>();
            services.AddScoped<IInschrijvingRepository, InschrijvingRepository>();
            services.AddScoped<IFeedbackRepository, FeedbackRepository>(); 
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env, DataInitializer dataInitializer, IServiceProvider serviceProvider)
        {
            serviceProvider.GetService<ApplicationDbContext>().Database.EnsureCreated();

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }
            app.UseHttpsRedirection();
            app.UseStaticFiles();

            app.UseRouting();

            app.UseAuthentication();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(
                    name: "default",
                    pattern: "{controller=Home}/{action=Index}/{id?}");
                endpoints.MapRazorPages();
            });

            dataInitializer.InitializeData().Wait();
        }
    }
}
