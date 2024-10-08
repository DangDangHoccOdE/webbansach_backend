package vn.spring.webbansach_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Float discountValue;

    @Column(name = "minimumSingleValue")
    private Double minimumSingleValue;

    @Column(name = "maximumOrderDiscount")
    private Double maximumOrderDiscount;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "expiredDate",nullable = false)
    private Date expiredDate;

    @Column(name = "voucherImage",columnDefinition = "LONGTEXT")
    @Lob
    private String voucherImage;

    @Column(name = "isActive",nullable = false)
    private Boolean isActive;

    @Column(name = "isAvailable",nullable = false)
    private Boolean isAvailable;

    @Column(name = "typeVoucher",nullable = false)
    private String typeVoucher;

    @JsonIgnore
    @OneToMany(mappedBy ="voucher",fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    private List<UserVoucher> userVouchers;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "order_voucher",joinColumns = @JoinColumn(name = "voucherId"),inverseJoinColumns = @JoinColumn(name = "orderId"))
    private List<Order> orders;
}
