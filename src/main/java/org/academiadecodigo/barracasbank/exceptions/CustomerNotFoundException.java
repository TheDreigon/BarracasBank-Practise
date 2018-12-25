package org.academiadecodigo.barracasbank.exceptions;

import org.academiadecodigo.javabank.errors.ErrorMessage;

/**
 * Thrown to indicate that the customer was not found
 */
public class CustomerNotFoundException extends BarracasBankException {

    /**
     * @see BarracasBankException#BarracasBankException(String)
     */
    public CustomerNotFoundException() {
        super(ErrorMessage.CUSTOMER_NOT_FOUND);
    }
}
