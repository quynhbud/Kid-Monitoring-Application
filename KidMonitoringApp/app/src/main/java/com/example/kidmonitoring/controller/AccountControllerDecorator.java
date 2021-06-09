package com.example.kidmonitoring.controller;

public class AccountControllerDecorator {
    IAccountController accountController;

    public AccountControllerDecorator(IAccountController accountController) {
        this.accountController = accountController;
    }
}
