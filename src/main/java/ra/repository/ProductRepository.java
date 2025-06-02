package ra.repository;

import ra.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getProducts(int page, int pageSize);
    long countProducts();
    Product findById(Long id);

}
