package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.Voucher;

@RepositoryRestResource(path = "vouchers")
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
    Voucher findByCode(String code);
    Voucher findByVoucherId(long id);

    boolean existsByCode(String code);
}
