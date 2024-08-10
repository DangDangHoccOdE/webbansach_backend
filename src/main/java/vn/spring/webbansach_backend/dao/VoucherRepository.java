package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.Voucher;

import java.awt.print.Pageable;
import java.util.*;

@RepositoryRestResource(path = "vouchers")
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
    List<Voucher> findByCodeContaining(@RequestParam("code") String code);
    List<Voucher> findByCodeContainingAndIsAvailable(@RequestParam("code") String code,@RequestParam("isAvailable") boolean isAvailable);
    List<Voucher> findByCodeContainingAndIsActive(@RequestParam("code") String code,@RequestParam("isActive") boolean isActive);
    List<Voucher> findByIsAvailableAndIsActive(@RequestParam("isAvailable") boolean isAvailable,@RequestParam("isActive") boolean isActive);
    Voucher findByCode(String code);
    Voucher findByVoucherId(Long voucherId);

    boolean existsByCode(String code);
}
