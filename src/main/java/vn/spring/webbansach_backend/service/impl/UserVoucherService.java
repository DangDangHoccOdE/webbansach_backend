package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.UserVoucherRepository;
import vn.spring.webbansach_backend.entity.UserVoucher;
import vn.spring.webbansach_backend.service.inter.IUserVoucherService;

import java.util.List;

@Service
public class UserVoucherService implements IUserVoucherService {
    private final UserVoucherRepository userVoucherRepository;

    @Autowired
    public UserVoucherService(UserVoucherRepository userVoucherRepository) {
        this.userVoucherRepository = userVoucherRepository;
    }

    @Override
    public List<UserVoucher> saveAll(List<UserVoucher> userVouchers) {
        return userVoucherRepository.saveAll(userVouchers);
    }
}
