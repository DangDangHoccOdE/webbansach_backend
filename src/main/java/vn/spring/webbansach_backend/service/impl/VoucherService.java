package vn.spring.webbansach_backend.service.impl;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.VoucherRepository;
import vn.spring.webbansach_backend.dto.VoucherDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.Voucher;
import vn.spring.webbansach_backend.service.inter.IVoucherService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;

import java.sql.Date;

@Service
public class VoucherService implements IVoucherService {
    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
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
}
