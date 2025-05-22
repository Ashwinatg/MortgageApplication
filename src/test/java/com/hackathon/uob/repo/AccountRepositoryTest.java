package com.hackathon.uob.repo;

import com.hackathon.uob.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Long customerId1;
    private Long customerId2;

    @BeforeEach
    void setUp() {
        // In a real scenario, you might persist Customer entities and get their IDs.
        // For this test, we'll assume customer IDs 1L and 2L exist or are used directly.
        customerId1 = 1L;
        customerId2 = 2L;
    }

    private Account createAndPersistAccount(Long custId, String accNum, String accType, double balance) {
        Account account = new Account();
        account.setCustomerId(custId);
        account.setAccountNumber(accNum);
        account.setAccountType(accType);
        account.setAccountBalance(balance);
        account.setCreatedTs(LocalDateTime.now());
        account.setUpdatedTs(LocalDateTime.now());
        return entityManager.persistAndFlush(account);
    }

    @Test
    void whenFindByCustomerId_andAccountsExist_thenReturnAccounts() {
        // given
        createAndPersistAccount(customerId1, "ACC001", "Savings", 1000.0);
        createAndPersistAccount(customerId1, "ACC002", "Current", 2000.0);
        createAndPersistAccount(customerId2, "ACC003", "Savings", 1500.0);

        // when
        List<Account> foundAccounts = accountRepository.findByCustomerId(customerId1);

        // then
        assertNotNull(foundAccounts);
        assertEquals(2, foundAccounts.size());
        assertTrue(foundAccounts.stream().allMatch(acc -> acc.getCustomerId().equals(customerId1)));
    }

    @Test
    void whenFindByCustomerId_andNoAccountsExist_thenReturnEmptyList() {
        // given
        createAndPersistAccount(customerId2, "ACC004", "Savings", 3000.0); // Account for another customer

        // when
        List<Account> foundAccounts = accountRepository.findByCustomerId(customerId1);

        // then
        assertNotNull(foundAccounts);
        assertTrue(foundAccounts.isEmpty());
    }

    @Test
    void whenFindByAccountNumber_andAccountExists_thenReturnAccount() {
        // given
        String accountNumber = "ACCUNIQUE001";
        createAndPersistAccount(customerId1, accountNumber, "Savings", 500.0);

        // when
        Account foundAccount = accountRepository.findByAccountNumber(accountNumber);

        // then
        assertNotNull(foundAccount);
        assertEquals(accountNumber, foundAccount.getAccountNumber());
    }

    @Test
    void whenFindByAccountNumber_andAccountDoesNotExist_thenReturnEmpty() {
        // when
        Account foundAccount = accountRepository.findByAccountNumber("NONEXISTENTACC");

        // then
        assertNull(foundAccount);
    }

    @Test
    void whenSave_thenPersistAndGenerateIdAndSetTimestamps() {
        // given
        Account account = new Account();
        account.setCustomerId(customerId1);
        account.setAccountNumber("SAVEACC001");
        account.setAccountType("Savings");
        account.setAccountBalance(100.0);
        LocalDateTime preSaveTimestamp = LocalDateTime.now().minusSeconds(1); // Ensure 'now' is after this
        account.setCreatedTs(LocalDateTime.now());
        account.setUpdatedTs(LocalDateTime.now());


        // when
        Account savedAccount = accountRepository.save(account);
        entityManager.flush(); // Ensure save is processed
        entityManager.clear(); // Ensure we get a fresh read

        Account retrievedAccount = entityManager.find(Account.class, savedAccount.getId());


        // then
        assertNotNull(savedAccount.getId());
        assertEquals("SAVEACC001", savedAccount.getAccountNumber());
        assertNotNull(retrievedAccount);
        assertNotNull(retrievedAccount.getCreatedTs());
        assertNotNull(retrievedAccount.getUpdatedTs());
        assertTrue(retrievedAccount.getCreatedTs().isAfter(preSaveTimestamp));
        assertTrue(retrievedAccount.getUpdatedTs().isAfter(preSaveTimestamp));
    }

    @Test
    void whenFindById_thenReturnAccount() {
        // given
        Account persistedAccount = createAndPersistAccount(customerId1, "FINDBYID001", "Current", 250.0);

        // when
        Optional<Account> foundAccount = accountRepository.findById(persistedAccount.getId());

        // then
        assertTrue(foundAccount.isPresent());
        assertEquals(persistedAccount.getId(), foundAccount.get().getId());
        assertEquals("FINDBYID001", foundAccount.get().getAccountNumber());
    }

    @Test
    void whenFindAll_thenReturnAllAccounts() {
        // given
        createAndPersistAccount(customerId1, "FINDALL001", "Savings", 100.0);
        createAndPersistAccount(customerId2, "FINDALL002", "Current", 200.0);

        // when
        List<Account> allAccounts = accountRepository.findAll();

        // then
        assertNotNull(allAccounts);
        assertEquals(2, allAccounts.size());
    }

    @Test
    void whenDeleteById_thenRemoveAccount() {
        // given
        Account persistedAccount = createAndPersistAccount(customerId1, "DELETE001", "Savings", 50.0);
        Long accountId = persistedAccount.getId();

        // when
        accountRepository.deleteById(accountId);
        entityManager.flush(); // Ensure delete is processed
        entityManager.clear(); // Clear persistence context for fresh read

        // then
        Optional<Account> foundAccount = accountRepository.findById(accountId);
        assertFalse(foundAccount.isPresent());
    }
}
