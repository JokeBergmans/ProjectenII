using G002_project_dotnet.Data.Mappers;
using G002_project_dotnet.Models.Domain;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace G002_project_dotnet.Data
{
    public class ApplicationDbContext : IdentityDbContext
    {

        public DbSet<Aankondiging> Aankondigingen { get; set; }
        public DbSet<Feedback> Feedback { get; set; }
        public DbSet<Gebruiker> Gebruikers { get; set; }
        public DbSet<Inschrijving> Inschrijvingen { get; set; }
        public DbSet<Lokaal> Lokalen { get; set; }
        public DbSet<SessieMedia> SessieMedia { get; set; }
        public DbSet<Sessie> Sessies { get; set; }
        public DbSet<SessieAankondiging> SessieAankondigingen { get; set; }
        public DbSet<SessieInschrijving> SessieInschrijvingen { get; set; }
        public DbSet<SessieKalender> SessieKalenders { get; set; }

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            builder.ApplyConfiguration(new SessieConfiguration());
            builder.ApplyConfiguration(new GebruikerConfiguration());
            builder.ApplyConfiguration(new AankondigingConfiguration());
            builder.ApplyConfiguration(new InschrijvingConfiguration());
            builder.ApplyConfiguration(new FeedbackConfiguration());
            builder.ApplyConfiguration(new LokaalConfiguration());
            builder.ApplyConfiguration(new SessieMediaConfiguration());
            builder.ApplyConfiguration(new SessieKalenderConfiguration());
            builder.ApplyConfiguration(new SessieAankondigingConfiguration());
            builder.ApplyConfiguration(new SessieInschrijvingConfiguration());
        }
    }
}
