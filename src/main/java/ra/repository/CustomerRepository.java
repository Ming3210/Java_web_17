package ra.repository;

import ra.entity.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer findById(Long id);
    boolean delete(Customer customer);
    boolean edit(Customer customer);
}
