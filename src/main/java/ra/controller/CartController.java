package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.entity.CartItemDTO;
import ra.entity.ProductCart;
import ra.repository.CartRepositoryImp;
import ra.service.CartService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepositoryImp cartRepositoryImp;

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        Long customerId = ((Number) session.getAttribute("customerId")).longValue();
        if (customerId == null) return "redirect:/login";

        List<CartItemDTO> cartItems = cartService.getCartItemsWithProduct(customerId);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long cartId,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        Long customerId = ((Number) session.getAttribute("customerId")).longValue();

        if (quantity > 0) {
            cartService.updateQuantity(cartId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeItem(@RequestParam Long cartId, HttpSession session) {
        Long customerId = ((Number) session.getAttribute("customerId")).longValue();

        cartService.removeItem(cartId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int quantity,
                            HttpSession session) {
        Long customerId = ((Number) session.getAttribute("customerId")).longValue();


        cartService.addToCart(customerId, productId, quantity);
        return "redirect:/cart";
    }



}
