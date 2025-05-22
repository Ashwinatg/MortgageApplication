package com.hackathon.uob.repo;

import com.hackathon.uob.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void whenFindByLoginId_thenReturnCustomer() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("testuser");
        customer.setEmail("testuser@example.com");
        customer.setPassword("password");
        customer.setMobile("1234567890");
        entityManager.persistAndFlush(customer);

        // when
        Optional<Customer> found = customerRepository.findByLoginId("testuser");

        // then
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getLoginId());
    }

    @Test
    void whenFindByLoginId_withNonExistingLoginId_thenReturnEmpty() {
        // when
        Optional<Customer> found = customerRepository.findByLoginId("nonexistinguser");

        // then
        assertFalse(found.isPresent());
    }

    @Test
    void whenFindByEmail_thenReturnCustomer() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("testemailuser");
        customer.setEmail("testemail@example.com");
        customer.setPassword("password");
        customer.setMobile("0987654321");
        entityManager.persistAndFlush(customer);

        // when
        Customer found = customerRepository.findByEmail("testemail@example.com");

        // then
        assertNotNull(found);
        assertEquals("testemail@example.com", found.getEmail());
    }

    @Test
    void whenFindByEmail_withNonExistingEmail_thenReturnEmpty() {
        // when
        Customer found = customerRepository.findByEmail("nonexistingemail@example.com");

        // then
        assertNull(found);
    }

    @Test
    void whenFindByMobile_thenReturnCustomer() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("testmobileuser");
        customer.setEmail("testmobile@example.com");
        customer.setPassword("password");
        customer.setMobile("1122334455");
        entityManager.persistAndFlush(customer);

        // when
        Customer found = customerRepository.findByMobile("1122334455");

        // then
        assertNotNull(found);
        assertEquals("1122334455", found.getMobile());
    }

    @Test
    void whenFindByMobile_withNonExistingMobile_thenReturnEmpty() {
        // when
        Customer found = customerRepository.findByMobile("0000000000");

        // then
        assertNull(found);
    }

    @Test
    void whenExistsByLoginId_thenReturnTrue() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("existsuser");
        customer.setEmail("existsuser@example.com");
        customer.setPassword("password");
        customer.setMobile("1231231234");
        entityManager.persistAndFlush(customer);

        // when
        boolean exists = customerRepository.existsByLoginId("existsuser");

        // then
        assertTrue(exists);
    }

    @Test
    void whenExistsByLoginId_withNonExistingLoginId_thenReturnFalse() {
        // when
        boolean exists = customerRepository.existsByLoginId("nonexistingexistsuser");

        // then
        assertFalse(exists);
    }

    @Test
    void whenSave_thenPersistAndGenerateId() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("saveuser");
        customer.setEmail("saveuser@example.com");
        customer.setPassword("password");
        customer.setMobile("1234567890");

        // when
        Customer savedCustomer = customerRepository.save(customer);

        // then
        assertNotNull(savedCustomer.getId());
        assertEquals("saveuser", savedCustomer.getLoginId());
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("findbyiduser");
        customer.setEmail("findbyiduser@example.com");
        customer.setPassword("password");
        customer.setMobile("0987654321");
        Customer persistedCustomer = entityManager.persistAndFlush(customer);

        // when
        Optional<Customer> found = customerRepository.findById(persistedCustomer.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals(persistedCustomer.getId(), found.get().getId());
    }

    @Test
    void whenFindAll_thenReturnAllCustomers() {
        // given
        Customer customer1 = new Customer();
        customer1.setLoginId("findalluser1");
        customer1.setEmail("findalluser1@example.com");
        customer1.setPassword("password");
        customer1.setMobile("1112223333");
        entityManager.persist(customer1);

        Customer customer2 = new Customer();
        customer2.setLoginId("findalluser2");
        customer2.setEmail("findalluser2@example.com");
        customer2.setPassword("password");
        customer2.setMobile("4445556666");
        entityManager.persist(customer2);
        entityManager.flush();

        // when
        List<Customer> customers = customerRepository.findAll();

        // then
        assertEquals(2, customers.size());
    }

    @Test
    void whenDeleteById_thenRemoveCustomer() {
        // given
        Customer customer = new Customer();
        customer.setLoginId("deleteuser");
        customer.setEmail("deleteuser@example.com");
        customer.setPassword("password");
        customer.setMobile("7778889999");
        Customer persistedCustomer = entityManager.persistAndFlush(customer);
        Long customerId = persistedCustomer.getId();

        // when
        customerRepository.deleteById(customerId);
        entityManager.flush(); // Ensure delete is processed
        entityManager.clear(); // Clear persistence context to ensure fresh read

        // then
        Optional<Customer> found = customerRepository.findById(customerId);
        assertFalse(found.isPresent());
    }
}
