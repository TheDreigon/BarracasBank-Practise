package org.academiadecodigo.barracasbank.services;

import org.academiadecodigo.barracasbank.exceptions.*;
import org.academiadecodigo.barracasbank.persistence.dao.AccountDao;
import org.academiadecodigo.barracasbank.persistence.dao.CustomerDao;
import org.academiadecodigo.barracasbank.persistence.dao.RecipientDao;
import org.academiadecodigo.barracasbank.persistence.model.Customer;
import org.academiadecodigo.barracasbank.persistence.model.Recipient;
import org.academiadecodigo.barracasbank.persistence.model.account.Account;
import org.academiadecodigo.barracasbank.persistence.model.account.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;
    private RecipientDao recipientDao;
    private AccountDao accountDao;

    /**
     * Sets the customer data access object
     *
     * @param customerDao the account DAO to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * Sets the recipient data access object
     *
     * @param recipientDao the recipient DAO to set
     */
    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    /**
     * Sets the account data access object
     *
     * @param accountDao the account DAO to set
     */
    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * @see CustomerService#get(Integer)
     */
    @Override
    public Customer get(Integer id) {
        return customerDao.findById(id);
    }

    /**
     * @see CustomerService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) throws CustomerNotFoundException {

        Customer customer = customerDao.findById(id);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        List<Account> accounts = customer.getAccounts();

        double balance = 0;

        for (Account account : accounts) {
            balance += account.getBalance();
        }

        return balance;
    }

    /**
     * @see CustomerService#save(Customer)
     */
    @Transactional
    @Override
    public Customer save(Customer customer) {
        return customerDao.saveOrUpdate(customer);
    }

    /**
     * @see CustomerService#delete(Integer)
     */
    @Transactional
    @Override
    public void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException {

        Customer customer = customerDao.findById(id);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        if (!customer.getAccounts().isEmpty()) {
            throw new AssociationExistsException();
        }

        customerDao.delete(id);
    }

    /**
     * @see CustomerService#list()
     */
    @Override
    public List<Customer> list() {
        return customerDao.findAll();
    }

    /**
     * @see CustomerService#listRecipients(Integer)
     */
    @Transactional(readOnly = true)
    @Override
    public List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException {

        // check then act logic requires transaction,
        // even if read only

        Customer customer = customerDao.findById(id);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        return new ArrayList<>(customerDao.findById(id).getRecipients());
    }

    /**
     * @see CustomerService#addRecipient(Integer, Recipient)
     */
    @Transactional
    @Override
    public Recipient addRecipient(Integer id, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException {

        Customer customer = customerDao.findById(id);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        if (accountDao.findById(recipient.getAccountNumber()) == null ||
                getAccountIds(customer).contains(recipient.getAccountNumber())) {
            throw new AccountNotFoundException();
        }

        if (recipient.getId() == null) {
            customer.addRecipient(recipient);
            customerDao.saveOrUpdate(customer);
        } else {
            recipientDao.saveOrUpdate(recipient);
        }
        return customer.getRecipients().get(customer.getRecipients().size() - 1);
    }

    /**
     * @see CustomerService#removeRecipient(Integer, Integer)
     */
    @Transactional
    @Override
    public void removeRecipient(Integer id, Integer recipientId) throws CustomerNotFoundException, RecipientNotFoundException {

        Customer customer = customerDao.findById(id);
        Recipient recipient = recipientDao.findById(recipientId);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        if (recipient == null || !recipient.getCustomer().getId().equals(id)) {
            throw new RecipientNotFoundException();
        }

        customer.removeRecipient(recipient);
        customerDao.saveOrUpdate(customer);
    }

    /**
     * @see CustomerService#addAccount(Integer, Account)
     */
    @Transactional
    @Override
    public Account addAccount(Integer id, Account account) throws CustomerNotFoundException, TransactionInvalidException {

        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }


        if (!account.canWithdraw() &&
                account.getBalance() < SavingsAccount.MIN_BALANCE) {
            throw new TransactionInvalidException();
        }


        customer.addAccount(account);
        customerDao.saveOrUpdate(customer);

        return customer.getAccounts().get(customer.getAccounts().size() - 1);
    }

    /**
     * @see CustomerService#closeAccount(Integer, Integer)
     */
    @Transactional
    @Override
    public void closeAccount(Integer id, Integer accountId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = customerDao.findById(id);
        Account account = accountDao.findById(accountId);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        if (account == null || !account.getCustomer().getId().equals(id)) {
            throw new AccountNotFoundException();
        }

        //different from 0 in case we later decide that negative values are acceptable
        if (account.getBalance() != 0) {
            throw new TransactionInvalidException();
        }

        customer.removeAccount(account);
        customerDao.saveOrUpdate(customer);
    }

    private Set<Integer> getAccountIds(Customer customer) {
        Set<Integer> accountIds = new HashSet<>();
        List<Account> accounts = customer.getAccounts();

        for (Account account : accounts) {
            accountIds.add(account.getId());
        }
        return accountIds;
    }
}

