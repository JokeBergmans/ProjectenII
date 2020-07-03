using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class AankondigingConfiguration : IEntityTypeConfiguration<Aankondiging>
    {
        public void Configure(EntityTypeBuilder<Aankondiging> builder)
        {
            builder.ToTable("AANKONDIGING");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.Datum)
                .HasColumnName("DATUM")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.PublicistId)
                .HasColumnName("PUBLICIST_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.Tekst)
                .HasColumnName("TEKST")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.HasOne(d => d.Publicist)
                .WithMany(p => p.Aankondigingen)
                .HasForeignKey(d => d.PublicistId)
                .HasConstraintName("ANKONDIGINGPUBLICISTID");
        }
    }
}
