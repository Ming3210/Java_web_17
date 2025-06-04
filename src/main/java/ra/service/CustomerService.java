package ra.service;

import ra.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(Long id);
    boolean delete(Customer customer);
    boolean edit(Customer customer);
}
