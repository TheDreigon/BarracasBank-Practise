package org.academiadecodigo.barracasbank.exceptions;

import org.academiadecodigo.javabank.errors.ErrorMessage;

/**
 * Thrown to indicate that the account was not found
 */
public class AccountNotFoundException extends BarracasBankException {

    /**
     * @see BarracasBankException#BarracasBankException(String)
     */
    public AccountNotFoundException() {
        super(ErrorMessage.ACCOUNT_NOT_FOUND);
    }
}
