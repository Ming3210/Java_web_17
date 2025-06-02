package ra.service;

import ra.entity.Customer;

import java.util.List;

public interface AuthService {
    void register(Customer user);
    List<Customer> findAll();
    Customer login(String username, String password);

}
