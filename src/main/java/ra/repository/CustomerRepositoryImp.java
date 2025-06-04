package ra.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ra.entity.Customer;

import java.util.List;
@Repository
public class CustomerRepositoryImp implements CustomerRepository{

    private final SessionFactory sessionFactory;

    public CustomerRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Customer> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    @Override
    public Customer findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Customer.class, id);
        }
    }

    @Override
    public boolean delete(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(customer);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean edit(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(customer);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
