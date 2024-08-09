package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private long orderId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "purchaseAddress")
    private String purchaseAddress;

    @Column(name = "deliveryAddress")
    private String deliveryAddress;

    @Column(name = "totalProduct")
    private int totalProduct;

    @Column(name = "totalPrice")
    private double totalPrice;

    @Column(name = "shippingFee")
    private double shippingFee;

    @Column(name = "paymentCost")
    private double paymentCost;

    @Column(name = "orderStatus")
    private String orderStatus;

    @Column(name = "deliveryStatus")
    private String deliveryStatus;

    @Column(name = "noteFromUser")
    private String noteFromUser;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "paymentId",nullable = false)
    private Payment payment;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "deliveryId",nullable = false)
    private Delivery delivery;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "order")
    private List<OrderReview> orderReview;
}
