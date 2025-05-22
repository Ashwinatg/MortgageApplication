package com.hackathon.uob.repo;

import com.hackathon.uob.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction createAndPersistTransaction(Long custId, String fromAcc, String toAcc, double amount, String remark) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(custId);
        transaction.setFromAccount(fromAcc);
        transaction.setToAccount(toAcc);
        transaction.setAmount(amount);
        transaction.setRemark(remark);
        transaction.setCreatedTs(LocalDateTime.now());
        transaction.setUpdatedTs(LocalDateTime.now());
        return entityManager.persistAndFlush(transaction);
    }

    @Test
    void whenSave_thenPersistAndGenerateIdAndSetTimestamps() {
        // given
        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        transaction.setFromAccount("FROMACC001");
        transaction.setToAccount("TOACC001");
        transaction.setAmount(150.75);
        transaction.setRemark("Test transaction");
        LocalDateTime preSaveTimestamp = LocalDateTime.now().minusSeconds(1);
        transaction.setCreatedTs(LocalDateTime.now());
        transaction.setUpdatedTs(LocalDateTime.now());

        // when
        Transaction savedTransaction = transactionRepository.save(transaction);
        entityManager.flush();
        entityManager.clear();

        Transaction retrievedTransaction = entityManager.find(Transaction.class, savedTransaction.getId());

        // then
        assertNotNull(savedTransaction.getId());
        assertEquals("FROMACC001", savedTransaction.getFromAccount());
        assertEquals(150.75, savedTransaction.getAmount());
        assertNotNull(retrievedTransaction);
        assertNotNull(retrievedTransaction.getCreatedTs());
        assertNotNull(retrievedTransaction.getUpdatedTs());
        assertTrue(retrievedTransaction.getCreatedTs().isAfter(preSaveTimestamp));
        assertTrue(retrievedTransaction.getUpdatedTs().isAfter(preSaveTimestamp));
    }

    @Test
    void whenFindById_thenReturnTransaction() {
        // given
        Transaction persistedTransaction = createAndPersistTransaction(2L, "FROMACC002", "TOACC002", 200.0, "Find By Id Test");

        // when
        Optional<Transaction> foundTransaction = transactionRepository.findById(persistedTransaction.getId());

        // then
        assertTrue(foundTransaction.isPresent());
        assertEquals(persistedTransaction.getId(), foundTransaction.get().getId());
        assertEquals(200.0, foundTransaction.get().getAmount());
        assertEquals("Find By Id Test", foundTransaction.get().getRemark());
    }

    @Test
    void whenFindById_withNonExistingId_thenReturnEmpty() {
        // when
        Optional<Transaction> foundTransaction = transactionRepository.findById(-99L); // Non-existent ID

        // then
        assertFalse(foundTransaction.isPresent());
    }

    @Test
    void whenFindAll_thenReturnAllTransactions() {
        // given
        createAndPersistTransaction(1L, "FROMACC003", "TOACC003", 50.0, "Txn 1");
        createAndPersistTransaction(2L, "FROMACC004", "TOACC004", 75.0, "Txn 2");

        // when
        List<Transaction> allTransactions = transactionRepository.findAll();

        // then
        assertNotNull(allTransactions);
        assertEquals(2, allTransactions.size());
    }

    @Test
    void whenFindAll_andNoTransactionsExist_thenReturnEmptyList() {
        // when
        List<Transaction> allTransactions = transactionRepository.findAll();

        // then
        assertNotNull(allTransactions);
        assertTrue(allTransactions.isEmpty());
    }

    @Test
    void whenDeleteById_thenRemoveTransaction() {
        // given
        Transaction persistedTransaction = createAndPersistTransaction(3L, "FROMACC005", "TOACC005", 120.0, "Delete Test");
        Long transactionId = persistedTransaction.getId();

        // when
        transactionRepository.deleteById(transactionId);
        entityManager.flush();
        entityManager.clear();

        // then
        Optional<Transaction> foundTransaction = transactionRepository.findById(transactionId);
        assertFalse(foundTransaction.isPresent());
    }

    @Test
    void whenSaveAll_thenPersistAllTransactions() {
        // given
        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(1L);
        transaction1.setFromAccount("SAVEALL001");
        transaction1.setToAccount("SAVEALL002");
        transaction1.setAmount(10.0);
        transaction1.setCreatedTs(LocalDateTime.now());
        transaction1.setUpdatedTs(LocalDateTime.now());

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(2L);
        transaction2.setFromAccount("SAVEALL003");
        transaction2.setToAccount("SAVEALL004");
        transaction2.setAmount(20.0);
        transaction2.setCreatedTs(LocalDateTime.now());
        transaction2.setUpdatedTs(LocalDateTime.now());

        List<Transaction> transactionsToSave = Arrays.asList(transaction1, transaction2);

        // when
        List<Transaction> savedTransactions = transactionRepository.saveAll(transactionsToSave);
        entityManager.flush();
        entityManager.clear();

        // then
        assertNotNull(savedTransactions);
        assertEquals(2, savedTransactions.size());
        savedTransactions.forEach(t -> assertNotNull(t.getId()));

        Optional<Transaction> found1 = transactionRepository.findById(savedTransactions.get(0).getId());
        assertTrue(found1.isPresent());
        assertEquals(10.0, found1.get().getAmount());

        Optional<Transaction> found2 = transactionRepository.findById(savedTransactions.get(1).getId());
        assertTrue(found2.isPresent());
        assertEquals(20.0, found2.get().getAmount());
    }

    @Test
    void whenDeleteAll_thenRemoveAllTransactions() {
        // given
        createAndPersistTransaction(1L, "DELALL001", "DELALL002", 30.0, "Delete All 1");
        createAndPersistTransaction(2L, "DELALL003", "DELALL004", 40.0, "Delete All 2");
        entityManager.flush(); // Ensure they are persisted before deleteAll

        // when
        transactionRepository.deleteAll();
        entityManager.flush(); // Ensure delete is processed
        entityManager.clear();

        // then
        List<Transaction> remainingTransactions = transactionRepository.findAll();
        assertTrue(remainingTransactions.isEmpty());
    }
}
