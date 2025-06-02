package ra.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.entity.Product;

import java.util.List;

@Repository
public class ProductRepositoryImp implements ProductRepository{

    private final SessionFactory sessionFactory;

    public ProductRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Product> getProducts(int page, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Product";
            Query<Product> query = session.createQuery(hql, Product.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public long countProducts() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(p.id) FROM Product p";
            Long count = session.createQuery(hql, Long.class).uniqueResult();
            return count != null ? count : 0;
        }
    }

    @Override
    public Product findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, id);
        }
    }

}
