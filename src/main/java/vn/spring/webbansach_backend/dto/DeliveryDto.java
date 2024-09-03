package vn.spring.webbansach_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryDto {
    private int deliveryId;

    private String deliveryName;
    private double shippingFee;


}
