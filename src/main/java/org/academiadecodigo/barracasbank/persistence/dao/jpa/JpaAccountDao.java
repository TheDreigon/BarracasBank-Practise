package org.academiadecodigo.barracasbank.persistence.dao.jpa;

import org.academiadecodigo.barracasbank.persistence.dao.AccountDao;
import org.academiadecodigo.barracasbank.persistence.model.account.Account;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link AccountDao} implementation
 */
@Repository
public class JpaAccountDao extends GenericJpaDao<Account> implements AccountDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(Class)
     */
    public JpaAccountDao() {
        super(Account.class);
    }
}
