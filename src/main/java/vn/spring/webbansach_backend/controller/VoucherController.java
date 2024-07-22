package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.VoucherDto;
import vn.spring.webbansach_backend.service.impl.VoucherService;
import java.util.*;

@RequestMapping("/vouchers")
@RestController
public class VoucherController {
    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/giftVouchersToUsers")
    public ResponseEntity<?> giftVouchersToUsers( @RequestBody List<Long> selectedVouchers){
        return voucherService.giftVouchersTouUsers(selectedVouchers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addVoucherAdmin")
    public ResponseEntity<?> addVoucherAdmin(@Validated @RequestBody VoucherDto voucherDto){
        return voucherService.addVoucherAdmin(voucherDto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editVoucherAdmin/{voucherId}")
    public ResponseEntity<?> addVoucherAdmin(@PathVariable long voucherId,@Validated @RequestBody VoucherDto voucherDto){
        return voucherService.editVoucherAdmin(voucherId,voucherDto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteVoucherAdmin/{voucherId}")
    public ResponseEntity<?> addVoucherAdmin(@PathVariable long voucherId){
        return voucherService.deleteVoucherAdmin(voucherId);
    }
}
