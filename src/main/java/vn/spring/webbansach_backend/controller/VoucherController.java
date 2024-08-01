package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.VoucherDto;
import vn.spring.webbansach_backend.service.impl.VoucherService;
import vn.spring.webbansach_backend.service.inter.IVoucherService;

import java.util.*;

@RequestMapping("/vouchers")
@RestController
public class VoucherController {
    private final IVoucherService iVoucherService;

    @Autowired
    public VoucherController(VoucherService iVoucherService) {
        this.iVoucherService = iVoucherService;
    }
    @GetMapping("/findVoucherByVoucherCodeAndUserId/{code}/{userId}")
    public ResponseEntity<?> findVoucherByVoucherCodeAndUserId(@PathVariable String code,@PathVariable Long userId){
        return iVoucherService.findVoucherByVoucherCodeAndUserId(code,userId);
    }



    @GetMapping("/showVoucherByUserId/{userId}")
    public ResponseEntity<?> showVoucherByUserId(@PathVariable Long userId){
        return iVoucherService.showVoucherByUserId(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/giftVouchersToUsers")
    public ResponseEntity<?> giftVouchersToUsers( @RequestBody List<Long> selectedVouchers){
        return iVoucherService.giftVouchersTouUsers(selectedVouchers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addVoucherAdmin")
    public ResponseEntity<?> addVoucherAdmin(@Validated @RequestBody VoucherDto voucherDto){
        return iVoucherService.addVoucherAdmin(voucherDto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addVouchersToVoucherAvailable")
    public ResponseEntity<?> addVouchersToVoucherAvailable(@RequestBody List<Long> selectedVouchers){
        return iVoucherService.addVouchersToVoucherAvailable(selectedVouchers);
    }

    @PostMapping("/saveVoucherByUser")
    public ResponseEntity<?> saveVoucherByUser(@RequestBody Map<String,Integer> voucherByUserMap){
        return iVoucherService.saveVoucherByUser(voucherByUserMap);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editVoucherAdmin/{voucherId}")
    public ResponseEntity<?> editVoucherAdmin(@PathVariable long voucherId,@Validated @RequestBody VoucherDto voucherDto){
        return iVoucherService.editVoucherAdmin(voucherId,voucherDto);
    }
    @PutMapping("/updateIsActive/{voucherId}")
    public ResponseEntity<?> updateIsActive(@PathVariable long voucherId){
        return iVoucherService.updateIsActive(voucherId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteVoucherAdmin/{voucherId}")
    public ResponseEntity<?> addVoucherAdmin(@PathVariable long voucherId){
        return iVoucherService.deleteVoucherAdmin(voucherId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteVouchersSelected")
    public ResponseEntity<?> deleteVouchersSelected(@RequestBody List<Long> selectedVouchers){
        return iVoucherService.deleteVouchersSelected(selectedVouchers);
    }
}
