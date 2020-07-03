using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class SessieKalenderConfiguration : IEntityTypeConfiguration<SessieKalender>
    {
        public void Configure(EntityTypeBuilder<SessieKalender> builder)
        {
            builder.ToTable("SESSIEKALENDER");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.Academiejaar)
                .HasColumnName("ACADEMIEJAAR")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Einde)
                .HasColumnName("EINDE")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Start)
                .HasColumnName("START")
                .HasMaxLength(255)
                .IsUnicode(false);
        }
    }
}
