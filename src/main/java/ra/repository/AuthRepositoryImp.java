package ra.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.entity.Customer;

import java.util.List;

@Repository
public class AuthRepositoryImp implements AuthRepository{

    private SessionFactory sessionFactory;

    public AuthRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void register(Customer user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    @Override
    public List<Customer> findAll() {
        Session session = sessionFactory.openSession();

        Query<Customer> query = session.createQuery("FROM Customer ", Customer.class);
        List<Customer> customers = query.getResultList();
        return customers;

    }

    @Override
    public Customer login(String username, String password) {
        Session session = sessionFactory.openSession();
        try {
            Query<Customer> query = session.createQuery(
                    "FROM Customer WHERE username = :username AND password = :password", Customer.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            List<Customer> result = query.getResultList();
            if (!result.isEmpty()) {
                return result.get(0);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }



}
