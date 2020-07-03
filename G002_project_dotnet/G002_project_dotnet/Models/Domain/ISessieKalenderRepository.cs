namespace G002_project_dotnet.Models.Domain
{
    public interface ISessieKalenderRepository
    {
        SessieKalender GetBy(int id);

        SessieKalender GetByYear(int year);
    }
}
