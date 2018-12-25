package org.academiadecodigo.barracasbank.exceptions;

import org.academiadecodigo.javabank.errors.ErrorMessage;

/**
 * Thrown to indicate that the transaction was not valid
 */
public class TransactionInvalidException extends BarracasBankException {

    /**
     * @see BarracasBankException#BarracasBankException(String)
     */
    public TransactionInvalidException() {
        super(ErrorMessage.TRANSACTION_INVALID);
    }
}
