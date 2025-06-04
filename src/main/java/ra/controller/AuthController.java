package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.entity.Customer;
import ra.service.AuthService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // Thêm mapping cho root path
    @GetMapping("/")
    public String home(HttpSession session) {
        Customer user = (Customer) session.getAttribute("loggedInUser");
        if (user != null) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/dashboard";
            } else if ("USER".equalsIgnoreCase(user.getRole())) {
                return "redirect:/home";
            }
        }
        return "redirect:/login-form";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // Nếu đã login thì redirect
        Customer user = (Customer) session.getAttribute("loggedInUser");
//        if (user != null) {
//            return "redirect:/";
//        }
        return "login-form"; // Đổi thành "login" cho consistent với HTML template
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        // Validate input
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Tên đăng nhập và mật khẩu không được để trống.");
            return "login-form";
        }

        try {
            Customer user = authService.login(username.trim(), password);

            if (user != null) {
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    session.setAttribute("loggedInUser", user);
                    return "admin/dashboard";
                } else if ("USER".equalsIgnoreCase(user.getRole())) {
                    session.setAttribute("loggedInUser", user);
                    session.setAttribute("customerId", user.getId()); // ⚠️ Gán customerId cho session
                    return "redirect:/home";

            } else {
                    model.addAttribute("error", "Vai trò không hợp lệ.");
                }
            } else {
                model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }

        return "login-form";
    }

    // Thêm logout method
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login-form";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid Customer customer, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation errors occurred. Please correct them and try again.");
            return "register";
        }
        try {
            authService.register(customer);
            model.addAttribute("message", "Registration successful!");
            return "redirect:/login"; // Redirect về login sau khi đăng ký thành công
        } catch (Exception e) {
            model.addAttribute("message", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/list")
    public String getAll(Model model, HttpSession session) {
        // Kiểm tra quyền admin
        Customer user = (Customer) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        List<Customer> customers = authService.findAll();
        model.addAttribute("customers", customers);
        return "list";
    }
}