package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_voucher")
public class UserVoucher {
    @EmbeddedId
    private UserVoucherId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("voucherId")
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    private int quantity;
}

