package com.example.kidmonitoring.model;

public class Accounts {
    private String Username;
    private String Password;

    public Accounts(String username, String password) {
        Username = username;
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public static class AccountsBuilder{
        private String Username;
        private String Password;

        public AccountsBuilder (String username){
            this.Username = username;
        }

        public AccountsBuilder Password(String password){
            this.Password = password;
            return this;
        }
        public Accounts Build(){
            Accounts accounts = new Accounts(this.Username,this.Password);
            return accounts;
        }
    }
}
