using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace G002_project_dotnet.Data.Mappers
{
    public class FeedbackConfiguration : IEntityTypeConfiguration<Feedback>
    {
        public void Configure(EntityTypeBuilder<Feedback> builder)
        {
            builder.ToTable("FEEDBACK");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.SessieId)
                .HasColumnName("SESSIEITLAB_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.AuteurId)
                    .HasColumnName("AUTEUR_ID")
                    .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.Tekst)
                    .HasColumnName("TEKST")
                    .HasMaxLength(255)
                    .IsUnicode(false);

            builder.HasOne(d => d.Auteur)
                    .WithMany(p => p.Feedback)
                    .HasForeignKey(d => d.AuteurId)
                    .HasConstraintName("FK_FEEDBACK_AUTEUR_ID");
        }

    }
}
