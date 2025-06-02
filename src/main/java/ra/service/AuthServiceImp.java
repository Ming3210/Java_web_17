package ra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.entity.Customer;
import ra.repository.AuthRepository;

import java.util.List;

@Service
public class AuthServiceImp implements AuthService{

    @Autowired
    private AuthRepository authRepository;

    @Override
    public void register(Customer user) {
        authRepository.register(user);
    }

    @Override
    public List<Customer> findAll() {
        return authRepository.findAll();
    }

    @Override
    public Customer login(String username, String password) {
        return authRepository.login(username, password);
    }
}
