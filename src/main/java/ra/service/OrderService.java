package ra.service;

import ra.entity.Order;

import java.util.List;

public interface OrderService {
    void saveOrder(Order order);

    List<Long> getProductIdsFromCart(Long customerId);

    Long getTotalMoneyFromCart(Long customerId);

    void clearCart(Long customerId);
}
