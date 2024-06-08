package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private int roleId;

    @Column(name = "roleName",length = 256)
    private String roleName;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "roleId"),inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> userList;
}