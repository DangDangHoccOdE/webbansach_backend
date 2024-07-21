package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "code",nullable = false)
    private String code;

    @Column(name = "`describe`",columnDefinition = "text")
    private String describe;

    @Column(name = "discountValue")
    private Double discountValue;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "expiredDate",nullable = false)
    private Date expiredDate;

    @Column(name = "voucherImage",columnDefinition = "LONGTEXT")
    @Lob
    private String voucherImage;

    @Column(name = "isActive",nullable = false)
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "user_voucher",joinColumns = @JoinColumn(name = "voucherId"),inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> users;
}
