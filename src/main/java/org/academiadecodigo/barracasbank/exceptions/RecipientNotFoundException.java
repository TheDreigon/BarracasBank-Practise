package org.academiadecodigo.barracasbank.exceptions;

import org.academiadecodigo.barracasbank.errors.ErrorMessage;

/**
 * Thrown to indicate that the recipient was not found
 */
public class RecipientNotFoundException extends BarracasBankException {

    /**
     * @see BarracasBankException#BarracasBankException(String)
     */
    public RecipientNotFoundException() {
        super(ErrorMessage.RECIPIENT_NOT_FOUND);
    }
}
