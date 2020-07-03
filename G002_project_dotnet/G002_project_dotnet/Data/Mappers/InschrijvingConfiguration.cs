using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class InschrijvingConfiguration : IEntityTypeConfiguration<Inschrijving>
    {
        public void Configure(EntityTypeBuilder<Inschrijving> builder)
        {
            builder.ToTable("INSCHRIJVING");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.Aanwezig)
                .HasColumnName("AANWEZIG")
                .HasDefaultValueSql("((0))");

            builder.Property(e => e.Datum)
                .HasColumnName("DATUM")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.GebruikerId)
                .HasColumnName("GEBRUIKER_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.SessieId)
                .HasColumnName("SESSIE_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.Status)
                .HasColumnName("STATUS")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.HasOne(d => d.Gebruiker)
                .WithMany(p => p.Inschrijvingen)
                .HasForeignKey(d => d.GebruikerId)
                .HasConstraintName("NSCHRIJVINGGEBRUIKERID");

            builder.HasOne(d => d.Sessie)
                .WithMany(p => p.Inschrijvingen)
                .HasForeignKey(d => d.SessieId)
                .HasConstraintName("INSCHRIJVING_SESSIE_ID");
        }
    }
}
