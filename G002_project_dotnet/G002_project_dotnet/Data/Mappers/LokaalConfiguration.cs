using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class LokaalConfiguration : IEntityTypeConfiguration<Lokaal>
    {
        public void Configure(EntityTypeBuilder<Lokaal> builder)
        {
            builder.ToTable("LOKAAL");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.Campus)
                .HasColumnName("CAMPUS")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Gebouw)
                .HasColumnName("GEBOUW")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Maxplaatsen).HasColumnName("MAXPLAATSEN");

            builder.Property(e => e.Naam)
                .HasColumnName("NAAM")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Opmerking)
                .HasColumnName("OPMERKING")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Type)
                .HasColumnName("TYPE")
                .HasMaxLength(255)
                .IsUnicode(false);
        }
    }
}
