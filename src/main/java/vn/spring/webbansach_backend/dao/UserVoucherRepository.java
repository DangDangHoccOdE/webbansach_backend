package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.UserVoucher;
import vn.spring.webbansach_backend.entity.UserVoucherId;

@RepositoryRestResource(path = "user-voucher")
public interface UserVoucherRepository extends JpaRepository<UserVoucher, UserVoucherId> {
    UserVoucher findByVoucher_CodeAndUser_UserId(@RequestParam("code") String code,@RequestParam("userId") Long userId);
}
