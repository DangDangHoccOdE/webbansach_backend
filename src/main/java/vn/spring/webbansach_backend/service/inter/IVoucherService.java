package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.VoucherDto;
import java.util.*;

public interface IVoucherService {
    ResponseEntity<?> addVoucherAdmin(VoucherDto voucherDto);
    ResponseEntity<?> giftVouchersTouUsers(List<Long> vouchersId);
    ResponseEntity<?> editVoucherAdmin(long voucherId,VoucherDto voucherDto);
    ResponseEntity<?> deleteVoucherAdmin(long voucherId);
    ResponseEntity<?> deleteVouchersSelected(List<Long> vouchersId);
}
