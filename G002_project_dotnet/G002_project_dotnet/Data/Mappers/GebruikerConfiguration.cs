using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;

namespace G002_project_dotnet.Data.Mappers
{
    public class GebruikerConfiguration : IEntityTypeConfiguration<Gebruiker>
    {
        public void Configure(EntityTypeBuilder<Gebruiker> builder)
        {
            builder.ToTable("GEBRUIKER");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.Aantalaanwezig).HasColumnName("AANTALAANWEZIG");

            builder.Property(e => e.Aantalafwezig).HasColumnName("AANTALAFWEZIG");

            builder.Property(e => e.Aantalingeschreven).HasColumnName("AANTALINGESCHREVEN");

            builder.Property(e => e.Encryptedpassword)
                .HasColumnName("ENCRYPTEDPASSWORD")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Gebruikersnaam)
                .HasColumnName("GEBRUIKERSNAAM")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Naam)
                .HasColumnName("NAAM")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Email).HasColumnName("EMAIL");

            builder.Property(e => e.Profielfoto)
                .HasColumnName("PROFIELFOTO")
                .HasColumnType("image");

            builder.Property(e => e.Status)
                .HasColumnName("STATUS")
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasConversion(
                    g => g.ToString(),
                    g => (GebruikerStatus)Enum.Parse(typeof(GebruikerStatus), g));

            builder.Property(e => e.Type)
                .HasColumnName("TYPE")
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasConversion(
                    g => g.ToString(),
                    g => (GebruikerType)Enum.Parse(typeof(GebruikerType), g));
        }
    }
}
