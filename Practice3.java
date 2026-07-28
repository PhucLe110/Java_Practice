import java.time.Year;
import java.util.*;
import java.util.regex.Pattern;

// LỚP CHỦ XE (VEHICLE OWNER)
class ChuSoHuu {
    private String cmnd;
    private String hoTen;
    private String email;

    public ChuSoHuu(String cmnd, String hoTen, String email) {
        if (!validateCMND(cmnd)) {
            throw new IllegalArgumentException("Số CMND/CCCD phải đúng 12 chữ số!");
        }
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Email không đúng định dạng!");
        }
        this.cmnd = cmnd;
        this.hoTen = hoTen;
        this.email = email;
    }

    public static boolean validateCMND(String cmnd) {
        return cmnd != null && cmnd.matches("\\d{12}");
    }

    public static boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    public String getCmnd() { return cmnd; }
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("[Chủ xe - CMND: %s | Họ tên: %s | Email: %s]", cmnd, hoTen, email);
    }
}

// LỚP TRỪU TƯỢNG PHƯƠNG TIỆN 
abstract class PhuongTien {
    protected String bienSo;
    protected String hangSanXuat;
    protected int namSanXuat;
    protected String mauXe;
    protected ChuSoHuu chuSoHuu;

    private static final List<String> HANG_HOP_LE = Arrays.asList("Honda", "Yamaha", "Toyota", "Suzuki");

    public PhuongTien(String bienSo, String hangSanXuat, int namSanXuat, String mauXe, ChuSoHuu chuSoHuu) {
        if (bienSo == null || bienSo.trim().length() != 5) {
            throw new IllegalArgumentException("Biển số xe phải đúng 5 ký tự!");
        }
        if (!validateHang(hangSanXuat)) {
            throw new IllegalArgumentException("Hãng sản xuất chỉ bao gồm: Honda, Yamaha, Toyota, Suzuki!");
        }
        int currentYear = Year.now().getValue();
        if (namSanXuat <= 2000 || namSanXuat > currentYear) {
            throw new IllegalArgumentException("Năm sản xuất phải > 2000 và <= " + currentYear + "!");
        }
        this.bienSo = bienSo;
        this.hangSanXuat = normalizeHang(hangSanXuat);
        this.namSanXuat = namSanXuat;
        this.mauXe = mauXe;
        this.chuSoHuu = chuSoHuu;
    }

    public static boolean validateHang(String hang) {
        if (hang == null) return false;
        for (String h : HANG_HOP_LE) {
            if (h.equalsIgnoreCase(hang.trim())) return true;
        }
        return false;
    }

    private static String normalizeHang(String hang) {
        for (String h : HANG_HOP_LE) {
            if (h.equalsIgnoreCase(hang.trim())) return h;
        }
        return hang;
    }

    public String getBienSo() { return bienSo; }
    public String getHangSanXuat() { return hangSanXuat; }
    public ChuSoHuu getChuSoHuu() { return chuSoHuu; }

    @Override
    public String toString() {
        return String.format("Biển số: %-5s | Hãng: %-8s | Năm SX: %-4d | Màu: %-6s | %s",
                bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
    }
}

// 1. Ô tô
class OTo extends PhuongTien {
    private int soChoNgoi;
    private String kieuDongCo;

    public OTo(String bienSo, String hangSanXuat, int namSanXuat, String mauXe, ChuSoHuu chuSoHuu, int soChoNgoi, String kieuDongCo) {
        super(bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
        this.soChoNgoi = soChoNgoi;
        this.kieuDongCo = kieuDongCo;
    }

    @Override
    public String toString() {
        return String.format("[Ô TÔ] %s | Số chỗ: %d | Động cơ: %s", super.toString(), soChoNgoi, kieuDongCo);
    }
}

// 2. Xe máy
class XeMay extends PhuongTien {
    private double dungTich;

    public XeMay(String bienSo, String hangSanXuat, int namSanXuat, String mauXe, ChuSoHuu chuSoHuu, double dungTich) {
        super(bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
        this.dungTich = dungTich;
    }

    @Override
    public String toString() {
        return String.format("[XE MÁY] %s | Dung tích: %.1f cc", super.toString(), dungTich);
    }
}

// 3. Xe tải
class XeTai extends PhuongTien {
    private double trongTai;

    public XeTai(String bienSo, String hangSanXuat, int namSanXuat, String mauXe, ChuSoHuu chuSoHuu, double trongTai) {
        super(bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
        this.trongTai = trongTai;
    }

    @Override
    public String toString() {
        return String.format("[XE TẢI] %s | Trọng tải: %.1f tấn", super.toString(), trongTai);
    }
}

// LỚP QUẢN LÝ PHƯƠNG TIỆN 
class QuanLyPhuongTien {
    private List<PhuongTien> danhSach;

    public QuanLyPhuongTien() {
        this.danhSach = new ArrayList<>();
    }

    // Task 1: Thêm phương tiện (Kiểm tra trùng biển số)
    public boolean themPhuongTien(PhuongTien pt) {
        for (PhuongTien item : danhSach) {
            if (item.getBienSo().equalsIgnoreCase(pt.getBienSo())) {
                System.out.println("Thêm thất bại! Biển số xe '" + pt.getBienSo() + "' đã tồn tại!");
                return false;
            }
        }
        danhSach.add(pt);
        System.out.println("Thêm thành công phương tiện có biển số: " + pt.getBienSo());
        return true;
    }

    // Task 2: Tìm kiếm phương tiện theo biển số
    public PhuongTien timKiemTheoBienSo(String bienSo) {
        for (PhuongTien pt : danhSach) {
            if (pt.getBienSo().equalsIgnoreCase(bienSo)) {
                return pt;
            }
        }
        return null;
    }

    // Task 3: Tìm danh sách xe theo số CMND/CCCD của chủ xe
    public List<PhuongTien> timKiemTheoCMND(String cmnd) {
        List<PhuongTien> res = new ArrayList<>();
        for (PhuongTien pt : danhSach) {
            if (pt.getChuSoHuu().getCmnd().equals(cmnd)) {
                res.add(pt);
            }
        }
        return res;
    }

    // Task 4: Xóa tất cả các xe thuộc một hãng sản xuất
    public boolean xoaTheoHang(String hang) {
        boolean removed = danhSach.removeIf(pt -> pt.getHangSanXuat().equalsIgnoreCase(hang));
        if (removed) {
            System.out.println("✅ Đã xóa thành công tất cả xe thuộc hãng: " + hang);
        } else {
            System.out.println("⚠️ Không tìm thấy xe nào thuộc hãng: " + hang);
        }
        return removed;
    }

    // Task 5: Tìm hãng có nhiều phương tiện nhất
    public String timHangCoNhieuXeNhat() {
        if (danhSach.isEmpty()) return "Danh sách trống";
        Map<String, Integer> countMap = new HashMap<>();
        for (PhuongTien pt : danhSach) {
            countMap.put(pt.getHangSanXuat(), countMap.getOrDefault(pt.getHangSanXuat(), 0) + 1);
        }

        String maxHang = "";
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxHang = entry.getKey();
            }
        }
        return maxHang + " (" + maxCount + " xe)";
    }

    // Task 6: Sắp xếp các hãng theo số lượng xe giảm dần
    public void hienThiCacHangTheoSoLuongGiamDan() {
        Map<String, Integer> countMap = new HashMap<>();
        for (PhuongTien pt : danhSach) {
            countMap.put(pt.getHangSanXuat(), countMap.getOrDefault(pt.getHangSanXuat(), 0) + 1);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(countMap.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println("\n--- BẢNG XẾP HẠNG HÃNG XE THEO SỐ LƯỢNG GIẢM DẦN ---");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println("Hãng " + entry.getKey() + ": " + entry.getValue() + " xe");
        }
    }

    // Task 7: Thống kê từng loại phương tiện
    public void thongKeTheoLoaiXe() {
        int oToCount = 0, xeMayCount = 0, xeTaiCount = 0;
        for (PhuongTien pt : danhSach) {
            if (pt instanceof OTo) oToCount++;
            else if (pt instanceof XeMay) xeMayCount++;
            else if (pt instanceof XeTai) xeTaiCount++;
        }
        System.out.println("\n--- THỐNG KÊ SỐ LƯỢNG THEO LOẠI XE ---");
        System.out.println("1. Ô tô   : " + oToCount + " xe");
        System.out.println("2. Xe máy : " + xeMayCount + " xe");
        System.out.println("3. Xe tải : " + xeTaiCount + " xe");
    }

    public void hienThiTieuChuan() {
        System.out.println("\n--- DANH SÁCH TOÀN BỘ PHƯƠNG TIỆN ---");
        if (danhSach.isEmpty()) {
            System.out.println("Không có phương tiện nào!");
            return;
        }
        for (PhuongTien pt : danhSach) {
            System.out.println(pt);
        }
    }
}

// LỚP MAIN 
public class Practice3 {
    public static void main(String[] args) {

        QuanLyPhuongTien qlpt = new QuanLyPhuongTien();

        System.out.println("=== THÊM DỮ LIỆU MẪU MÔ PHỎNG ===");
        ChuSoHuu owner1 = new ChuSoHuu("012345678901", "Nguyễn Văn A", "nguyena@gmail.com");
        ChuSoHuu owner2 = new ChuSoHuu("098765432109", "Trần Thị B", "tranb@yahoo.com");

        // 1. Thêm Ô tô
        qlpt.themPhuongTien(new OTo("29A01", "Toyota", 2021, "Đen", owner1, 5, "V6"));
        qlpt.themPhuongTien(new OTo("30F02", "Honda", 2022, "Trắng", owner2, 7, "2.0 Turbo"));

        // 2. Thêm Xe máy
        qlpt.themPhuongTien(new XeMay("29X03", "Honda", 2019, "Đỏ", owner1, 125.0));
        qlpt.themPhuongTien(new XeMay("29X04", "Yamaha", 2020, "Xanh", owner2, 150.0));

        // 3. Thêm Xe tải
        qlpt.themPhuongTien(new XeTai("30T05", "Suzuki", 2018, "Xám", owner1, 3.5));

        qlpt.hienThiTieuChuan();

        // Task 2: Tìm theo biển số
        System.out.println("\n=== TASK 2: TÌM KIẾM THEO BIỂN SỐ '29A01' ===");
        PhuongTien ptFound = qlpt.timKiemTheoBienSo("29A01");
        System.out.println(ptFound != null ? ptFound : "Không tìm thấy!");

        // Task 3: Tìm xe theo CMND chủ xe
        System.out.println("\n=== TASK 3: TÌM XE CỦA CHỦ XE CÓ CMND '012345678901' ===");
        List<PhuongTien> xeCuaA = qlpt.timKiemTheoCMND("012345678901");
        xeCuaA.forEach(System.out::println);

        // Task 5: Hãng có nhiều xe nhất
        System.out.println("\n=== TASK 5: HÃNG CÓ NHIỀU XE NHẤT ===");
        System.out.println("Hãng quản lý nhiều xe nhất: " + qlpt.timHangCoNhieuXeNhat());

        // Task 6: Sắp xếp các hãng theo số lượng giảm dần
        qlpt.hienThiCacHangTheoSoLuongGiamDan();

        // Task 7: Thống kê theo loại xe
        qlpt.thongKeTheoLoaiXe();

        // Task 4: Xóa theo hãng
        System.out.println("\n=== TASK 4: XÓA TẤT CẢ XE CỦA HÃNG 'Yamaha' ===");
        qlpt.xoaTheoHang("Yamaha");
        qlpt.hienThiTieuChuan();
    }
}