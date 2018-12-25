package org.academiadecodigo.barracasbank.exceptions;

import org.academiadecodigo.javabank.errors.ErrorMessage;

/**
 * Thrown to indicate that an association still exists
 */
public class AssociationExistsException extends BarracasBankException {

    /**
     * @see BarracasBankException#BarracasBankException(String)
     */
    public AssociationExistsException() {
        super(ErrorMessage.ASSOCIATION_EXISTS);
    }
}
