package ra.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.entity.CartItemDTO;
import ra.entity.Product;
import ra.entity.ProductCart;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartRepositoryImp implements CartRepository {
    private final SessionFactory sessionFactory;

    public CartRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ProductCart> getCartByCustomerId(Long customerId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM ProductCart pc WHERE pc.customerId = :customerId";
            return session.createQuery(hql, ProductCart.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        }
    }

    @Override
    public void updateQuantity(Long cartId, int quantity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ProductCart cartItem = session.get(ProductCart.class, cartId);
            if (cartItem != null) {
                cartItem.setQuantity(quantity);
                session.update(cartItem);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeItem(Long cartId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ProductCart cartItem = session.get(ProductCart.class, cartId);
            if (cartItem != null) {
                session.delete(cartItem);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public double calculateTotalPrice(List<ProductCart> cartItems) {
        // Giả sử bạn có Product entity, và ProductCart chỉ lưu productId,
        // nên cần truy xuất Product để lấy giá. Cách đơn giản là lấy giá từng sản phẩm.
        double total = 0;
        try (Session session = sessionFactory.openSession()) {
            for (ProductCart item : cartItems) {
                Long productId = item.getProductId();
                int quantity = item.getQuantity();

                Product product = session.get(Product.class, productId);
                if (product != null) {
                    total += product.getPrice() * quantity;
                }
            }
        }
        return total;
    }


    @Override
    public ProductCart findByCustomerIdAndProductId(Long customerId, Long productId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM ProductCart WHERE customerId = :cid AND productId = :pid";
            Query<ProductCart> query = session.createQuery(hql, ProductCart.class);
            query.setParameter("cid", customerId);
            query.setParameter("pid", productId);
            return query.uniqueResult();
        }
    }

    @Override
    public void saveOrUpdate(ProductCart cart) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(cart);
            session.getTransaction().commit();
        }
    }

    @Override
    public void addToCart(Long customerId, Long productId, int quantity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String hql = "FROM ProductCart WHERE customerId = :cid AND productId = :pid";
            Query<ProductCart> query = session.createQuery(hql, ProductCart.class);
            query.setParameter("cid", customerId);
            query.setParameter("pid", productId);

            ProductCart existing = query.uniqueResult();

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + quantity);
                session.update(existing);
            } else {
                ProductCart newCart = new ProductCart(null, customerId, productId, quantity);
                session.save(newCart);
            }

            session.getTransaction().commit();
        }
    }
    @Override
    public List<CartItemDTO> getCartItemsWithProduct(Long customerId) {
        List<ProductCart> carts = getCartByCustomerId(customerId);
        List<CartItemDTO> result = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            for (ProductCart item : carts) {
                Product product = session.get(Product.class, item.getProductId());
                if (product != null) {
                    result.add(new CartItemDTO(item.getId(), product, item.getQuantity()));
                }
            }
        }

        return result;
    }


}


