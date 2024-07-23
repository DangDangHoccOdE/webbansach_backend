package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.VoucherRepository;
import vn.spring.webbansach_backend.dto.VoucherDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.Voucher;
import vn.spring.webbansach_backend.service.inter.IVoucherService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherService implements IVoucherService {
    private final VoucherRepository voucherRepository;
    private final UserService userService;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository, UserService userService) {
        this.voucherRepository = voucherRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ResponseEntity<?> giftVouchersTouUsers(List<Long> vouchersId) {
        List<User> users = userService.findAllUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người cần tặng"));
        }

        List<Voucher> vouchers = voucherRepository.findAllById(vouchersId);
        if(vouchers.size()!=vouchersId.size()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần tặng"));
        }

        int batchSize = 20; // Đặt max người là 20 => Lưu theo lô
        List<User> batch =new ArrayList<>(batchSize);

        for(User user : users){
            List<Voucher> userVouchers = user.getVouchers();
            userVouchers.addAll(vouchers);
            batch.add(user);

            if(batch.size() == batchSize){
                userService.saveAllUser(batch);
                batch.clear();
            }
        }

        if(!batch.isEmpty()){
            userService.saveAllUser(batch);
        }
        return ResponseEntity.ok(new Notice("Đã tặng thành công voucher cho tất cả người dùng"));
    }

    @Override
    public ResponseEntity<?> addVoucherAdmin(VoucherDto voucherDto) {
        if(voucherRepository.existsByCode(voucherDto.getCode())){
            return ResponseEntity.badRequest().body(new Notice("Mã voucher đã tồn tại!"));
        }
        String expiredDate = voucherDto.getExpiredDate();
        Date date = ConvertStringToDate.convert(expiredDate);

        Voucher voucher = new Voucher();
        voucher.setCode(voucherDto.getCode());
        voucher.setVoucherImage(voucherDto.getVoucherImage());
        voucher.setDiscountValue(voucherDto.getDiscountValue());
        voucher.setIsActive(voucherDto.getIsActive());
        voucher.setDescribe(voucherDto.getDescribe());
        voucher.setQuantity(voucherDto.getQuantity());
        voucher.setExpiredDate(date);

        voucherRepository.save(voucher);
        return ResponseEntity.ok(new Notice("Đã tạo voucher thành công"));
    }

    @Override
    public ResponseEntity<?> editVoucherAdmin(long voucherId, VoucherDto voucherDto) {
        Voucher voucher = voucherRepository.findByVoucherId(voucherId);
        if(voucherRepository.existsByCode(voucherDto.getCode())){
            return ResponseEntity.badRequest().body(new Notice("Mã voucher đã tồn tại!"));
        }
        if(voucher==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần chỉnh sửa!"));
        }
        String expiredDate =voucherDto.getExpiredDate();
        voucher.setExpiredDate(ConvertStringToDate.convert(expiredDate));
        voucher.setVoucherImage(voucherDto.getVoucherImage());
        voucher.setDiscountValue(voucherDto.getDiscountValue());
        voucher.setCode(voucherDto.getCode());
        voucher.setDescribe(voucherDto.getDescribe());
        voucher.setIsActive(voucherDto.getIsActive());
        voucher.setQuantity(voucherDto.getQuantity());
        voucherRepository.save(voucher);
        return ResponseEntity.ok(new Notice("Chỉnh sửa voucher thành công"));
    }

    @Override
    public ResponseEntity<?> deleteVoucherAdmin(long voucherId) {
        Voucher voucher = voucherRepository.findByVoucherId(voucherId);

        if(voucher==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần xóa!"));
        }

        voucherRepository.delete(voucher);
        return ResponseEntity.ok(new Notice("Đã xóa voucher thành công!"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteVouchersSelected(List<Long> vouchersId) {
        List<Voucher> vouchers = voucherRepository.findAllById(vouchersId);

        if(vouchers.size()!=vouchersId.size()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần xóa!"));
        }

        voucherRepository.deleteAll(vouchers);
        return ResponseEntity.ok(new Notice("Đã xóa voucher đã chọn thành công!"));
    }
}
