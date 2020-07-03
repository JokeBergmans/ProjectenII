using G002_project_dotnet.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;

namespace G002_project_dotnet.Data.Mappers
{
    public class SessieConfiguration : IEntityTypeConfiguration<Sessie>
    {
        public void Configure(EntityTypeBuilder<Sessie> builder)
        {
            builder.ToTable("SESSIEITLAB");

            builder.Property(e => e.Id)
                .HasColumnName("ID")
                .HasColumnType("numeric(19, 0)")
                .ValueGeneratedOnAdd();

            builder.Property(e => e.Beschrijving)
                .HasColumnName("BESCHRIJVING")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Dagenopvoorhand).HasColumnName("DAGENOPVOORHAND");

            builder.Property(e => e.Einde)
                .HasColumnName("EINDE")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Gastspreker)
                .HasColumnName("GASTSPREKER")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.LokaalId)
                .HasColumnName("LOKAAL_ID")
                .HasColumnType("numeric(19, 0)");

            builder.Property(e => e.Maxplaatsen).HasColumnName("MAXPLAATSEN");

            builder.Property(e => e.Mededeling)
                .HasColumnName("MEDEDELING")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Start)
                .HasColumnName("START")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.Status)
                .HasColumnName("STATUS")
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasConversion(
                    s => s.ToString(),
                    s => (SessieType)Enum.Parse(typeof(SessieType), s));

            builder.Property(e => e.Stuurherinnering)
                .HasColumnName("STUURHERINNERING")
                .HasDefaultValueSql("((0))");

            builder.Property(e => e.Titel)
                .HasColumnName("TITEL")
                .HasMaxLength(255)
                .IsUnicode(false);

            builder.Property(e => e.VerantwoordelijkeId)
                .HasColumnName("VERANTWOORDELIJKE_ID")
                .HasColumnType("numeric(19, 0)");

            builder.HasOne(d => d.Lokaal)
                .WithMany(p => p.Sessies)
                .HasForeignKey(d => d.LokaalId)
                .HasConstraintName("SESSIEITLAB_LOKAAL_ID");

            builder.HasOne(d => d.Verantwoordelijke)
                .WithMany(p => p.Sessies)
                .HasForeignKey(d => d.VerantwoordelijkeId)
                .HasConstraintName("SSSTLABVRNTWRDELIJKEID");
        }
    }
}
