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

    @Override
    public boolean save(Product product) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(product);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.delete(product);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


}
