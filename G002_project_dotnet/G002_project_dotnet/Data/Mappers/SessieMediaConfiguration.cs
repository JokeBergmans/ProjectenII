using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class SessieMediaConfiguration : IEntityTypeConfiguration<SessieMedia>
    {
        public void Configure(EntityTypeBuilder<SessieMedia> builder)
        {
            builder.ToTable("SessieITLab_MEDIA");

            builder.Property(e => e.Media)
                .HasColumnName("MEDIA")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.SessieId)
                .HasColumnName("SessieITLab_ID")
                .HasColumnType("numeric(19, 0)");

            builder.HasOne(d => d.Sessie)
                .WithMany(p => p.SessieMedia)
                .HasForeignKey(d => d.SessieId)
                .HasConstraintName("SssTLabMEDIASssITLabID");
        }
    }
}
