package vn.spring.webbansach_backend.service.inter;

import vn.spring.webbansach_backend.entity.UserVoucher;
import java.util.*;

public interface IUserVoucherService {
    List<UserVoucher> saveAll(List<UserVoucher> userVouchers);
}
