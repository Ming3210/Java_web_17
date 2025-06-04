package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.entity.Customer;
import ra.service.CustomerService;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "admin/users";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "admin/edit";
    }

    // Xử lý form cập nhật
    @PostMapping("/edit")
    public String editCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.edit(customer);
        return "redirect:/admin/users";
    }

    // Xóa khách hàng
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        customerService.delete(customer);
        return "redirect:/admin/users";
    }
}
