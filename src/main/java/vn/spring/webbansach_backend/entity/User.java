package vn.spring.webbansach_backend.entity;
import jakarta.persistence.*;
import lombok.*;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "userName")
    private String userName;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "password",length = 512)
    private String password;

    @Column(name = "sex")
    private String sex;

    @Column(name = "email")
    private String email;

    @Column(name = "deliveryAddress")
    private String deliveryAddress;

    @Column(name = "purchaseAddress")
    private String purchaseAddress;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL},mappedBy = "user")
    private List<Remark> remarkList;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL},mappedBy = "user")
    private List<WishList> wishListList;

    @OneToMany(fetch = FetchType.LAZY,cascade ={CascadeType.ALL},mappedBy = "user")
    private List<Order> orderList;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "userId"),inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roleList;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "activationCode")
    private String activationCode;

    @Column(name = "activationExpiry")
    private LocalDateTime activationExpiry;

    @Column(name = "emailCode")
    private String emailCode;

    @Column(name = "emailExpiry")
    private LocalDateTime emailExpiry;

    @Column(name = "forgotPasswordCode")
    private String forgotPasswordCode;

    @Column(name = "forgotPasswordExpiry")
    private LocalDateTime forgotPasswordExpiry;

    @Column(name = "avatar", columnDefinition = "LONGTEXT")
    @Lob
    private String avatar;
}