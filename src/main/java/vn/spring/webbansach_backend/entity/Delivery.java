package vn.spring.webbansach_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryId")
    private int deliveryId;

    @Column(name = "deliveryName")
    private String deliveryName;


    @Column(name = "shippingFee")
    private double shippingFee;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "delivery")
    private List<Orders> ordersList;
}
