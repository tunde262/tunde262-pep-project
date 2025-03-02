package Service;

import DAO.AccountsDAO;
import Model.Account;

public class AccountService {
    AccountsDAO accountsDAO;

    public AccountService(){
        accountsDAO = new AccountsDAO();
    }

    public AccountService(AccountsDAO accountsDAO){
        this.accountsDAO = accountsDAO;
    }

    public Account register(Account account){
        String username = account.getUsername();

        if(username == null || username.length() == 0) {
            return null;
        } 

        if(accountsDAO.getAccountByUsername(username) != null) {
            return null;
        }

        String password = account.getPassword();

        if(password.length() < 4) {
            return null;
        }

        return accountsDAO.insertAccount(account);
   
    }

    public Account login(Account account){
        return accountsDAO.authenticate(account.getUsername(), account.getPassword());
    }
}
