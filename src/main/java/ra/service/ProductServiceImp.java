package ra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.entity.Customer;
import ra.entity.Product;
import ra.repository.ProductRepository;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> getProducts(int page, int pageSize) {
        return productRepository.getProducts(page, pageSize);
    }

    @Override
    public long countProducts() {
        return productRepository.countProducts();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id);
    }
}
