package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.VoucherDto;
import java.util.*;

public interface IVoucherService {
    ResponseEntity<?> showVoucherByUserId(Long userId);
    ResponseEntity<?> findVoucherByVoucherCodeAndUserId(String code,Long userId);
    ResponseEntity<?> addVoucherAdmin(VoucherDto voucherDto);
    ResponseEntity<?> saveVoucherByUser(Map<String,Integer> voucherByUserMap);
    ResponseEntity<?> addVouchersToVoucherAvailable(List<Long> vouchersId);
    ResponseEntity<?> giftVouchersTouUsers(List<Long> vouchersId);
    ResponseEntity<?> updateIsActive(Long voucherId);
    ResponseEntity<?> editVoucherAdmin(long voucherId,VoucherDto voucherDto);
    ResponseEntity<?> deleteVoucherAdmin(long voucherId);
    ResponseEntity<?> deleteVouchersSelected(List<Long> vouchersId);
}
