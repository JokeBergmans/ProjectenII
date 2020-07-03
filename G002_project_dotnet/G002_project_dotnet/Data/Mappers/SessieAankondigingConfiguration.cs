using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class SessieAankondigingConfiguration : IEntityTypeConfiguration<SessieAankondiging>
    {
        public void Configure(EntityTypeBuilder<SessieAankondiging> builder)
        {
            builder.HasKey(e => new { e.SessieId, e.AankondigingId })
                .HasName("PK__SESSIEIT__D6836B1F3432DBD5");

            builder.ToTable("SESSIEITLAB_AANKONDIGING");

            builder.Property(e => e.SessieId)
                .HasColumnName("SessieITLab_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.AankondigingId)
                .HasColumnName("aankondigingen_ID")
                .HasColumnType("numeric(19, 0)");

            builder.HasOne(d => d.Aankondiging)
                .WithMany(p => p.SessieAankondiging)
                .HasForeignKey(d => d.AankondigingId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("SSSTLBNKNDGNGnkndgngnD");

            builder.HasOne(d => d.Sessie)
                .WithMany(p => p.SessieAankondiging)
                .HasForeignKey(d => d.SessieId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("SSSTLBNKNDIGINGSssTLbD");
        }
    }
}
