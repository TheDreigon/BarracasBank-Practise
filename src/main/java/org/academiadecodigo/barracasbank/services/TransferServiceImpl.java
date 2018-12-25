package org.academiadecodigo.barracasbank.services;

import org.academiadecodigo.barracasbank.domain.Transfer;
import org.academiadecodigo.barracasbank.exceptions.AccountNotFoundException;
import org.academiadecodigo.barracasbank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.barracasbank.exceptions.TransactionInvalidException;
import org.academiadecodigo.barracasbank.persistence.dao.AccountDao;
import org.academiadecodigo.barracasbank.persistence.dao.CustomerDao;
import org.academiadecodigo.barracasbank.persistence.model.Customer;
import org.academiadecodigo.barracasbank.persistence.model.Recipient;
import org.academiadecodigo.barracasbank.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link TransferService} implementation
 */
@Service
public class TransferServiceImpl implements TransferService {

    private CustomerDao customerDao;
    private AccountDao accountDao;

    /**
     * Sets the customer data access object
     *
     * @param customerDao the customer dao to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * Sets the account data access object
     *
     * @param accountDao the account dao to set
     */
    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * @see TransferService#transfer(Transfer)
     */
    @Transactional
    @Override
    public void transfer(Transfer transfer) throws AccountNotFoundException, TransactionInvalidException {

        Account srcAccount = accountDao.findById(transfer.getSrcId());
        Account dstAccount = accountDao.findById(transfer.getDstId());

        accountTransfer(srcAccount, dstAccount, transfer.getAmount());
    }

    /**
     * @see TransferService#transfer(Transfer, Integer)
     */
    @Transactional
    @Override
    public void transfer(Transfer transfer, Integer customerId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = customerDao.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        Account srcAccount = accountDao.findById(transfer.getSrcId());
        Account dstAccount = accountDao.findById(transfer.getDstId());

        if (!customer.getAccounts().contains(srcAccount)) {
            throw new AccountNotFoundException();
        }

        // make sure destination account is a part of the recipient list
        verifyRecipientId(customer, dstAccount);

        accountTransfer(srcAccount, dstAccount, transfer.getAmount());
    }

    private void accountTransfer(Account srcAccount, Account dstAccount, Double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        // make sure transaction can be performed
        verifyTransferAccountInformation(srcAccount, dstAccount, amount);

        srcAccount.debit(amount);
        dstAccount.credit(amount);

        accountDao.saveOrUpdate(srcAccount);
        accountDao.saveOrUpdate(dstAccount);
    }

    private void verifyTransferAccountInformation(Account srcAccount, Account dstAccount, double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        if (srcAccount == null || dstAccount == null) {
            throw new AccountNotFoundException();
        }

        if (!srcAccount.canDebit(amount) || !dstAccount.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

    }

    private void verifyRecipientId(Customer customer, Account dstAccount) throws AccountNotFoundException {

        List<Integer> recipientAccountIds = listRecipientAccountIds(customer);

        if (!customer.getAccounts().contains(dstAccount) &&
                !recipientAccountIds.contains(dstAccount.getId())) {
            throw new AccountNotFoundException();
        }
    }

    private List<Integer> listRecipientAccountIds(Customer customer) {

        List<Integer> recipientAccountIds = new ArrayList<>();
        for (Recipient recipient : customer.getRecipients()) {
            recipientAccountIds.add(recipient.getAccountNumber());
        }

        return recipientAccountIds;
    }
}
