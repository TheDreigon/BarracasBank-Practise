package org.academiadecodigo.barracasbank.exceptions;

/**
 * A generic java bank exception to be used as base for concrete types of exceptions
 *
 * @see Exception
 */
public class BarracasBankException extends Exception {

    /**
     * @see Exception#Exception(String)
     */
    public BarracasBankException(String message) {
        super(message);
    }
}
