package ra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.entity.Customer;
import ra.repository.CustomerRepository;

import java.util.List;
@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean delete(Customer customer) {
        return customerRepository.delete(customer);
    }

    @Override
    public boolean edit(Customer customer) {
        return customerRepository.edit(customer);
    }
}
