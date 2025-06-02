package ra.repository;

import org.springframework.stereotype.Repository;
import ra.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
@Repository
public class OrderRepositoryImp implements OrderRepository {

    private EntityManagerFactory emf;

    public OrderRepositoryImp(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void saveOrder(Order order) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Long> getProductIdsFromCart(Long customerId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c.product.id FROM CartItem c WHERE c.customer.id = :customerId";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("customerId", customerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Long getTotalMoneyFromCart(Long customerId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT SUM(c.product.price * c.quantity) FROM CartItem c WHERE c.customer.id = :customerId";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("customerId", customerId);
            Long total = query.getSingleResult();
            return total != null ? total : 0L;
        } finally {
            em.close();
        }
    }

    @Override
    public void clearCart(Long customerId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String jpql = "DELETE FROM CartItem c WHERE c.customer.id = :customerId";
            int deletedCount = em.createQuery(jpql)
                    .setParameter("customerId", customerId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
