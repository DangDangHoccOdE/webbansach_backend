package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    private int orderId;

    @Column(name = "date")
    private Date date;

    @Column(name = "purchaseAddress")
    private String purchaseAddress;

    @Column(name = "deliveryAddress")
    private String deliveryAddress;

    @Column(name = "totalProductCost")
    private double totalProductCost;

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
}
