package ra.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.entity.Product;
import ra.entity.ProductCart;
import ra.repository.CartRepositoryImp;
import ra.service.ProductService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartRepositoryImp cartRepositoryImp;
    @Autowired
    private Cloudinary cloudinary;
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

    @GetMapping("admin/products/new")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product";
    }


    @PostMapping("admin/products/new")
    public String addProduct(@RequestParam("productName") String productName,
                             @RequestParam("description") String description,
                             @RequestParam("price") double price,
                             @RequestParam("stock") int stock,
                             @RequestParam("image") MultipartFile imageFile,
                             Model model) {
        try {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("folder", "product_images"));

            String imageUrl = (String) uploadResult.get("secure_url");

            // Tạo và lưu sản phẩm
            Product product = new Product();
            product.setProductName(productName);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setImage(imageUrl); // Lưu URL ảnh vào DB

            if (productService.save(product)) {
                model.addAttribute("message", "Thêm sản phẩm thành công");
            } else {
                model.addAttribute("error", "Thêm sản phẩm thất bại");
            }

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi upload ảnh");
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin/edit-product";
    }

    @PostMapping("/admin/products/update")
    public String updateProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/delete")
    public String deleteProduct(@RequestParam Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }


}
