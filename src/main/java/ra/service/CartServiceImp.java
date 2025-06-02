package ra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.entity.CartItemDTO;
import ra.entity.ProductCart;
import ra.repository.CartRepository;

import java.util.List;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;


    @Override
    public List<ProductCart> getCartByCustomerId(Long customerId) {
        return cartRepository.getCartByCustomerId(customerId);
    }

    @Override
    public void updateQuantity(Long cartId, int quantity) {
        cartRepository.updateQuantity(cartId, quantity);
    }

    @Override
    public void removeItem(Long cartId) {
        cartRepository.removeItem(cartId);
    }

    @Override
    public double calculateTotalPrice(List<ProductCart> cartItems) {
        return cartRepository.calculateTotalPrice(cartItems);
    }

    @Override
    public ProductCart findByCustomerIdAndProductId(Long customerId, Long productId) {
        return cartRepository.findByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    public void saveOrUpdate(ProductCart cart) {
        cartRepository.saveOrUpdate(cart);
    }

    @Override
    public void addToCart(Long customerId, Long productId, int quantity) {
        cartRepository.addToCart(customerId, productId, quantity);
    }

    @Override
    public List<CartItemDTO> getCartItemsWithProduct(Long customerId) {
        return cartRepository.getCartItemsWithProduct(customerId);
    }

}
