package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.VoucherDto;

public interface IVoucherService {
    ResponseEntity<?> addVoucherAdmin(VoucherDto voucherDto);
    ResponseEntity<?> editVoucherAdmin(long voucherId,VoucherDto voucherDto);
    ResponseEntity<?> deleteVoucherAdmin(long voucherId);
}
