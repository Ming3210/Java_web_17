package ra.repository;

import ra.entity.Order;

import java.util.List;

public interface OrderRepository {
    void saveOrder(Order order);

    List<Long> getProductIdsFromCart(Long customerId);

    Long getTotalMoneyFromCart(Long customerId);

    void clearCart(Long customerId);
}
