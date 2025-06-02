package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.entity.Product;
import ra.entity.ProductCart;
import ra.repository.CartRepositoryImp;
import ra.service.ProductService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartRepositoryImp cartRepositoryImp;

    @Autowired
    public ProductController(ProductService productService, CartRepositoryImp cartRepositoryImp) {
        this.productService = productService;
        this.cartRepositoryImp = cartRepositoryImp;
    }

    @GetMapping("/home")
    public String listProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            Model model) {

        List<Product> products = productService.getProducts(page, size);
        long totalProducts = productService.countProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "product-list";
    }

    @GetMapping("/products/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products?error=notfound";
        }
        model.addAttribute("product", product);
        return "product-detail"; // Tên file HTML hiển thị chi tiết
    }



}
