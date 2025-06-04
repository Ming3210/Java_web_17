package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.entity.Order;
import ra.service.OrderService;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkoutForm(Model model, HttpSession session) {
        Object customerIdObj = session.getAttribute("customerId");
        if (customerIdObj == null) {
            return "redirect:/login";
        }
        Long customerId = ((Number) customerIdObj).longValue();

        List<Long> productIds = orderService.getProductIdsFromCart(customerId);
        Long totalMoney = orderService.getTotalMoneyFromCart(customerId);

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setListProduct(productIds);
        order.setTotalMoney(totalMoney);
        order.setStatus("Chờ xử lý");

        model.addAttribute("order", order);
        return "checkout";
    }

    // Xử lý form thanh toán
    @PostMapping("/submit")
    public String submitOrder(@ModelAttribute Order order, HttpSession session) {
        Object customerIdObj = session.getAttribute("customerId");
        if (customerIdObj == null) {
            return "redirect:/login";
        }
        Long customerId = ((Number) customerIdObj).longValue();

        order.setCustomerId(customerId);


        // Lưu order
        orderService.saveOrder(order);

        orderService.clearCart(customerId);

        return "redirect:/order_success";
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order_success";
    }
}
