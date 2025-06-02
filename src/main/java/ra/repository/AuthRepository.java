package ra.repository;

import ra.entity.Customer;

import java.util.List;

public interface AuthRepository {
    void register(Customer user);
    List<Customer> findAll();
    Customer login(String username, String password);
}
