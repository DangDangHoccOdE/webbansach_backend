package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    private int paymentId;

    @Column(name = "paymentName")
    private String paymentName;

    @Column(name = "price")
    private double price;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "payment")
    private List<Order> orderList;
}
