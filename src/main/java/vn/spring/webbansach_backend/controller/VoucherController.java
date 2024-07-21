package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.webbansach_backend.dto.VoucherDto;
import vn.spring.webbansach_backend.service.impl.VoucherService;

@RequestMapping("/vouchers")
@RestController
public class VoucherController {
    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/addVoucherAdmin")
    public ResponseEntity<?> addVoucherAdmin(@Validated @RequestBody VoucherDto voucherDto){
        System.out.println("VoucherDto"+voucherDto);
        return voucherService.addVoucherAdmin(voucherDto);
    }
}
