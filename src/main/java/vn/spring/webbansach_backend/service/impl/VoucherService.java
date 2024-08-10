package vn.spring.webbansach_backend.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.UserVoucherRepository;
import vn.spring.webbansach_backend.dao.VoucherRepository;
import vn.spring.webbansach_backend.dto.VoucherDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.UserVoucher;
import vn.spring.webbansach_backend.entity.Voucher;
import vn.spring.webbansach_backend.service.inter.IVoucherService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class VoucherService implements IVoucherService {
    private final VoucherRepository voucherRepository;
    private final UserService userService;
    private final UserVoucherRepository userVoucherRepository;
    private final EntityManager entityManager;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository, UserService userService, UserVoucherRepository userVoucherRepository, EntityManager entityManager) {
        this.voucherRepository = voucherRepository;
        this.userService = userService;
        this.userVoucherRepository = userVoucherRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Voucher findVoucherById(long voucherId) {
        return voucherRepository.findByVoucherId(voucherId);
    }

    @Override
    public ResponseEntity<?> showVoucherByUserId(Long userId) {
        User user = userService.findUserByUserId(userId);

        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dụng"));
        }

        List<Voucher> vouchers = new ArrayList<>();
        List<UserVoucher> userVouchers = user.getUserVouchers();
        for(UserVoucher userVoucher : userVouchers){
            Voucher voucher = voucherRepository.findByVoucherId(userVoucher.getVoucher().getVoucherId());
            vouchers.add(voucher);
        }
        return ResponseEntity.ok(vouchers);
    }

    @Override
    public ResponseEntity<?> findVoucherByVoucherCodeAndUserId(String code, Long userId) {
        Voucher voucher = voucherRepository.findByCode(code);
        User user = userService.findUserByUserId(userId);

        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dụng"));
        }

        if(voucher!=null){
            // Check xem voucher có phải của user k ?
            List<UserVoucher> userVouchers = user.getUserVouchers();
            for(UserVoucher userVoucher:userVouchers){
                if(userVoucher.getVoucher().getCode().equals(voucher.getCode())){
                    List<Voucher> vouchers = new ArrayList<>();
                    vouchers.add(voucher);
                    return ResponseEntity.ok(vouchers);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveVoucherByUser(Map<String, Integer> voucherByUserMap) {
        long userId = voucherByUserMap.get("userId");
        long voucherId = voucherByUserMap.get("voucherId");

        User user = userService.findUserByUserId(userId);
        Voucher voucher = voucherRepository.findByVoucherId(voucherId);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dụng!"));
        }if(voucher == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher"));
        }

        UserVoucher userVoucher = userVoucherRepository.findByVoucher_CodeAndUser_UserId(voucher.getCode(),userId);
        UserVoucher userVoucherFind ;

        if(userVoucher==null){
            userVoucherFind = new UserVoucher();
            userVoucherFind.setVoucher(voucher);
            userVoucherFind.setUser(user);
            userVoucherFind.setQuantity(1);
        }else{
            userVoucherFind = userVoucher;
            userVoucherFind.setQuantity(userVoucherFind.getQuantity()+1);
        }

        userVoucherRepository.save(userVoucherFind);
        return ResponseEntity.ok(new Notice("Đã lưu voucher thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> addVouchersToVoucherAvailable(List<Long> vouchersId) {
        List<Voucher> vouchers = voucherRepository.findAllById(vouchersId);

        if(vouchers.size()!=vouchersId.size()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần thêm!"));
        }

        for(Voucher voucher:vouchers){
            if(voucher.getIsAvailable()){
                break;
            }
            voucher.setIsAvailable(true);
        }
        voucherRepository.saveAll(vouchers);
        return ResponseEntity.ok(new Notice("Đã thêm voucher vào danh sách voucher có sẵn thành công!"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> giftVouchersTouUsers(List<Long> vouchersId) {
        List<User> users = userService.findAllUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người cần tặng"));
        }

        List<Voucher> vouchers = voucherRepository.findAllById(vouchersId);
        if(vouchers.size()!=vouchersId.size() || vouchers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần tặng"));
        }

        int batchSize = 20; // Đặt max người là 20 => Lưu theo lô
        int count = 0;
        String sql;
        sql = "INSERT INTO user_voucher (user_id, voucher_id, quantity)"+
                    "VALUE (:userId, :voucherId, 1)"+
                    "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        Query query = entityManager.createNativeQuery(sql);

        for(User user : users){
            for(Voucher voucher: vouchers){
                query.setParameter("userId",user.getUserId());
                query.setParameter("voucherId",voucher.getVoucherId());
                query.executeUpdate();

                if(++count % batchSize == 0){
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }

        return ResponseEntity.ok(new Notice("Đã tặng thành công voucher cho tất cả người dùng"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateIsActive(Long voucherId) {
        Voucher voucher = voucherRepository.findByVoucherId(voucherId);

        if(voucher == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher"));
        }

        LocalDateTime nowDate = LocalDateTime.now().minusDays(1);
        LocalDateTime expiredTime = voucher.getExpiredDate().toLocalDate().atStartOfDay();
        if(expiredTime.isBefore(nowDate) && voucher.getIsActive()) {
            voucher.setIsActive(false);
            voucherRepository.save(voucher);
            return ResponseEntity.ok().body(voucher);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<?> addVoucherAdmin(VoucherDto voucherDto) {
        if(voucherRepository.existsByCode(voucherDto.getCode())){
            return ResponseEntity.badRequest().body(new Notice("Mã voucher đã tồn tại!"));
        }
        String expiredDate = voucherDto.getExpiredDate();
        Date date = ConvertStringToDate.convert(expiredDate);

        Voucher voucher = new Voucher();
        setVoucher(voucher,voucherDto,date);

        voucherRepository.save(voucher);
        return ResponseEntity.ok(new Notice("Đã tạo voucher thành công"));
    }

    private static void setVoucher(Voucher voucher,VoucherDto voucherDto, Date date) {
        voucher.setCode(voucherDto.getCode());
        voucher.setVoucherImage(voucherDto.getVoucherImage());
        voucher.setDiscountValue(voucherDto.getDiscountValue());
        voucher.setIsActive(voucherDto.getIsActive());
        voucher.setDescribe(voucherDto.getDescribe());
        voucher.setQuantity(voucherDto.getQuantity());
        voucher.setExpiredDate(date);
        voucher.setIsAvailable(voucherDto.getIsAvailable());
        voucher.setTypeVoucher(voucherDto.getTypeVoucher());
    }

    @Override
    public ResponseEntity<?> editVoucherAdmin(long voucherId, VoucherDto voucherDto) {
        Voucher voucher = voucherRepository.findByVoucherId(voucherId);
        Voucher voucherByCode = voucherRepository.findByCode(voucherDto.getCode());
        if(voucherRepository.existsByCode(voucherDto.getCode()) && !voucherByCode.getVoucherId().equals(voucherDto.getVoucherId())){
            return ResponseEntity.badRequest().body(new Notice("Mã voucher đã tồn tại!"));
        }
        if(voucher==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy voucher cần chỉnh sửa!"));
        }
        String expiredDate = voucherDto.getExpiredDate();
        Date date = ConvertStringToDate.convert(expiredDate);

        setVoucher(voucher,voucherDto,date);
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
