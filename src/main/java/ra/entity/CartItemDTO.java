package ra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDTO {
    private Long id;
    private Product product;
    private int quantity;



}
