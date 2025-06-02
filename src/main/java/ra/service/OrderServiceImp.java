package ra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.entity.Order;
import ra.repository.CartRepository;
import ra.repository.OrderRepository;

import java.util.List;
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepository;


    @Override
    public void saveOrder(Order order) {
        orderRepository.saveOrder(order);
    }

    @Override
    public List<Long> getProductIdsFromCart(Long customerId) {
        return orderRepository.getProductIdsFromCart(customerId);
    }

    @Override
    public Long getTotalMoneyFromCart(Long customerId) {
        return orderRepository.getTotalMoneyFromCart(customerId);
    }

    @Override
    public void clearCart(Long customerId) {
        orderRepository.clearCart(customerId);
    }
}
