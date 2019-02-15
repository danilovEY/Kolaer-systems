package ru.kolaer.server.account.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AccountAccessRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public AccountAccessRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Set<String> findByAccountId(long accountId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<String> accessCodes = entityManager
                .createNativeQuery("SELECT access FROM account_access WHERE account_id = :accountId")
                .setParameter("accountId", accountId)
                .getResultList();

        return new HashSet<>(accessCodes);
    }

}
