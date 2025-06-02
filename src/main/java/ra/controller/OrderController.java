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

    // Hiển thị form thanh toán, nhận dữ liệu từ giỏ hàng (trước đó bạn phải tính tổng tiền và danh sách sản phẩm)
    @GetMapping("/checkout")
    public String checkoutForm(Model model, HttpSession session) {
        Object customerIdObj = session.getAttribute("customerId");
        if (customerIdObj == null) {
            return "redirect:/login";
        }
        Long customerId = ((Number) customerIdObj).longValue();

        // Lấy thông tin giỏ hàng (ví dụ)
        // TODO: lấy danh sách sản phẩm và tổng tiền từ service/cart session
        // Giả sử bạn đã tính được:
        List<Long> productIds = orderService.getProductIdsFromCart(customerId);
        Long totalMoney = orderService.getTotalMoneyFromCart(customerId);

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setListProduct(productIds);
        order.setTotalMoney(totalMoney);
        order.setStatus("Chờ xử lý");

        model.addAttribute("order", order);
        return "checkout"; // tên file Thymeleaf
    }

    // Xử lý form thanh toán
    @PostMapping("/submit")
    public String submitOrder(@ModelAttribute Order order, HttpSession session) {
        Object customerIdObj = session.getAttribute("customerId");
        if (customerIdObj == null) {
            return "redirect:/login";
        }
        Long customerId = ((Number) customerIdObj).longValue();

        // Gán customerId để đảm bảo an toàn (tránh giả mạo form)
        order.setCustomerId(customerId);

        // Chuyển listProduct từ chuỗi nếu cần (nếu bạn gửi dưới dạng chuỗi CSV thì cần xử lý)
        // Nếu bạn truyền listProduct qua hidden field dạng chuỗi, parse lại:
        // Ví dụ:
        // String listProductString = ...;
        // List<Long> listProduct = Arrays.stream(listProductString.split(","))
        //       .map(Long::parseLong).collect(Collectors.toList());
        // order.setListProduct(listProduct);

        // Lưu order
        orderService.saveOrder(order);

        // Sau khi tạo đơn hàng thành công, có thể xóa giỏ hàng
        orderService.clearCart(customerId);

        return "redirect:/order/success";
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order_success"; // trang thông báo đặt hàng thành công
    }
}
