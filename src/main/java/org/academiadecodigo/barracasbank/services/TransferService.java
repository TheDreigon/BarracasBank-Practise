package org.academiadecodigo.barracasbank.services;

import org.academiadecodigo.barracasbank.domain.Transfer;
import org.academiadecodigo.barracasbank.exceptions.AccountNotFoundException;
import org.academiadecodigo.barracasbank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.barracasbank.exceptions.TransactionInvalidException;
import org.academiadecodigo.barracasbank.persistence.model.account.Account;

/**
 * Common interface for transfer services, provides methods to perform account transfers
 */
public interface TransferService {

    /**
     * Performs a transfer between two {@link Account}
     *
     * @param transfer the transfer object
     * @throws AccountNotFoundException
     * @throws TransactionInvalidException
     */
    void transfer(Transfer transfer)
            throws AccountNotFoundException, TransactionInvalidException;

    /**
     * Performs a transfer between two {@link Account}, if possible
     *
     * @param transfer   the transfer object
     * @param customerId the customer id
     * @throws CustomerNotFoundException
     * @throws AccountNotFoundException
     * @throws TransactionInvalidException
     */
    void transfer(Transfer transfer, Integer customerId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException;
}
