using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class SessieInschrijvingConfiguration : IEntityTypeConfiguration<SessieInschrijving>
    {
        public void Configure(EntityTypeBuilder<SessieInschrijving> builder)
        {
            builder.HasKey(e => new { e.SessieId, e.InschrijvingId })
                .HasName("PK__SESSIEIT__122D9C13707FADDB");

            builder.ToTable("SESSIEITLAB_INSCHRIJVING");

            builder.Property(e => e.SessieId)
                .HasColumnName("SessieITLab_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.InschrijvingId)
                .HasColumnName("inschrijvingen_ID")
                .HasColumnType("numeric(19, 0)");

            builder.HasOne(d => d.Inschrijving)
                .WithMany(p => p.SessieInschrijvingen)
                .HasForeignKey(d => d.InschrijvingId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("SSSTLBNSCHRnschrjvngnD");

            builder.HasOne(d => d.Sessie)
                .WithMany(p => p.SessieInschrijving)
                .HasForeignKey(d => d.SessieId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("SSSTLBNSCHRJVNGSssTLbD");
        }
    }
}
