package ra.service;

import ra.entity.Customer;
import ra.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts(int page, int pageSize);
    long countProducts();
    Product findById(Long id);
    boolean save(Product product);
    boolean deleteById(Long id);

}
