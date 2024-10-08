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

    @Column(name = "orderCode",nullable = false)
    private String orderCode;

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

    @Column(name = "cancelOrderTime")
    private LocalDateTime cancelOrderTime;

    @Column(name = "reasonCancelOrder")
    private String reasonCancelOrder;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "paymentId",nullable = false)
    private Payment payment;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "deliveryId",nullable = false)
    private Delivery delivery;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "order")
    private OrderReview orderReview;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "order_voucher",joinColumns = @JoinColumn(name = "orderId"),inverseJoinColumns = @JoinColumn(name = "voucherId"))
    private List<Voucher> vouchers;

}
