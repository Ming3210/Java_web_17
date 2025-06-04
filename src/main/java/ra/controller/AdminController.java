package ra.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.entity.Product;
import ra.repository.CartRepositoryImp;
import ra.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private  ProductService productService;




    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("currentPage", "dashboard");
        return "admin/dashboard";
    }

    // Quản lý sản phẩm
    @GetMapping("/products")
    public String products(  @RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "3") int size,
                             Model model) {
        List<Product> products = productService.getProducts(page, size);
        long totalProducts = productService.countProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "admin/products";
    }

    // Quản lý người dùng
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("pageTitle", "Quản lý Người dùng");
        model.addAttribute("currentPage", "users");
        return "admin/users";
    }

    // Quản lý đơn hàng
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("pageTitle", "Quản lý Đơn hàng");
        model.addAttribute("currentPage", "orders");
        return "admin/orders";
    }

    // Thống kê
    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAttribute("pageTitle", "Thống kê");
        model.addAttribute("currentPage", "analytics");
        return "admin/analytics";
    }

    // Cài đặt
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "Cài đặt");
        model.addAttribute("currentPage", "settings");
        return "admin/settings";
    }








}