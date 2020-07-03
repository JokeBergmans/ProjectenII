using G002_project_dotnet.Models.Domain;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace G002_project_dotnet.Data
{
    public class DataInitializer
    {
        private readonly ApplicationDbContext _dbContext;
        private readonly UserManager<IdentityUser> _userManager;

        public DataInitializer(ApplicationDbContext dbContext, UserManager<IdentityUser> userManager)
        {
            _dbContext = dbContext;
            _userManager = userManager;
        }

        public async Task InitializeData()
        {
            if (_dbContext.Database.CanConnect())
            {
                await InitializeUsers();
                foreach (Sessie sessie in _dbContext.Sessies.Include(s => s.Inschrijvingen).ToList())
                {
                    if (DateTime.Compare(sessie.Einde, DateTime.Now) < 0 )
                    {
                        sessie.Status = SessieType.AFGELOPEN;
                        foreach(Inschrijving i in sessie.Inschrijvingen)
                        {
                            if (i.Status == "INGESCHREVEN")
                                i.Status = "AFWEZIG";
                        }
                        _dbContext.SaveChanges();
                    }
                    else if (DateTime.Compare(sessie.Start, DateTime.Now) < 0)
                    {
                        sessie.Status = SessieType.GESLOTEN;
                        _dbContext.SaveChanges();
                    }
                }

            }

        }

        private async Task InitializeUsers()
        {

            foreach (Gebruiker gebruiker in _dbContext.Gebruikers.ToList())
            {
                var user = await _userManager.FindByEmailAsync(gebruiker.Email);
                if (user == null)
                {
                    user = new IdentityUser { UserName = gebruiker.Gebruikersnaam, Email = gebruiker.Email };
                    var result = await _userManager.CreateAsync(user, DecryptPassword(gebruiker.Encryptedpassword));
                    if (result.Succeeded)
                    {
                        if (gebruiker.Type == GebruikerType.HOOFDVERANTWOORDELIJKE)
                            await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "hoofdverantwoordelijke"));
                        else if (gebruiker.Type == GebruikerType.VERANTWOORDELIJKE)
                            await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "verantwoordelijke"));
                        else
                            await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "gebruiker"));
                    }

                }
            }
        }

        private string DecryptPassword(string encrypted)
        {
            TripleDESCryptoServiceProvider desCryptoProvider = new TripleDESCryptoServiceProvider();
            MD5CryptoServiceProvider hashMD5Provider = new MD5CryptoServiceProvider();

            byte[] byteHash;
            byte[] byteBuff;

            byteHash = hashMD5Provider.ComputeHash(Encoding.UTF8.GetBytes("ThisIsSpartaThisIsSparta"));
            desCryptoProvider.Key = byteHash;
            desCryptoProvider.Mode = CipherMode.ECB; //CBC, CFB
            desCryptoProvider.Padding = PaddingMode.None;
            byteBuff = Convert.FromBase64String(encrypted);

            string plaintext = Encoding.UTF8.GetString(desCryptoProvider.CreateDecryptor().TransformFinalBlock(byteBuff, 0, byteBuff.Length));
            return plaintext;
        }
    }
}
