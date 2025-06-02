package ra.service;

import ra.entity.CartItemDTO;
import ra.entity.ProductCart;

import java.util.List;

public interface CartService {
    List<ProductCart> getCartByCustomerId(Long customerId);
    void updateQuantity(Long cartId, int quantity);
    void removeItem(Long cartId);
    double calculateTotalPrice(List<ProductCart> cartItems);
    ProductCart findByCustomerIdAndProductId(Long customerId, Long productId);
    void saveOrUpdate(ProductCart cart);
    void addToCart(Long customerId, Long productId, int quantity);
    List<CartItemDTO> getCartItemsWithProduct(Long customerId);

}
