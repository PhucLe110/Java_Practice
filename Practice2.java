import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// YÊU CẦU 1: THIẾT KẾ CÁC LỚP 

// 1. Lớp trừu tượng HangHoa (Goods)
abstract class HangHoa {
    protected String maHang;
    protected String tenHang;
    protected int soLuongTon;
    protected double donGia;

    public HangHoa(String maHang, String tenHang, int soLuongTon, double donGia) {
        if (maHang == null || maHang.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hàng không được để trống!");
        }
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Số lượng tồn phải >= 0!");
        }
        if (donGia <= 0) {
            throw new IllegalArgumentException("Đơn giá phải > 0!");
        }
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
    }

    public String getMaHang() {
        return maHang;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public double getDonGia() {
        return donGia;
    }

    // Phương thức tính VAT 
    public abstract double tinhVAT();

    // YÊU CẦU 2: Phương thức đánh giá tiêu thụ 
    public abstract String danhGiaTieuThu();

    @Override
    public String toString() {
        return String.format("Mã: %-8s | Tên: %-15s | Tồn: %-5d | Đơn giá: %-10.2f | VAT: %-10.2f | Đánh giá: %s",
                maHang, tenHang, soLuongTon, donGia, tinhVAT(), danhGiaTieuThu());
    }
}

// 2. Lớp Hàng Thực Phẩm
class HangThucPham extends HangHoa {
    private LocalDate ngaySanXuat;
    private LocalDate ngayHetHan;
    private String nhaCungCap;

    public HangThucPham(String maHang, String tenHang, int soLuongTon, double donGia,
                        LocalDate ngaySanXuat, LocalDate ngayHetHan, String nhaCungCap) {
        super(maHang, tenHang, soLuongTon, donGia);
        if (ngayHetHan.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Ngày hết hạn phải >= Ngày sản xuất!");
        }
        this.ngaySanXuat = ngaySanXuat;
        this.ngayHetHan = ngayHetHan;
        this.nhaCungCap = nhaCungCap;
    }

    @Override
    public double tinhVAT() {
        return donGia * 0.05; // 5%
    }

    // Đánh giá tiêu thụ Thực Phẩm
    @Override
    public String danhGiaTieuThu() {
        boolean isExpired = LocalDate.now().isAfter(ngayHetHan);
        if (soLuongTon > 0 && isExpired) {
            return "Khó bán (Đã hết hạn)";
        }
        return "Không đánh giá";
    }
}

// 3. Lớp Hàng Điện Máy
class HangDienMay extends HangHoa {
    private int thoiGianBaoHanh; // số tháng
    private double congSuat;     // KW

    public HangDienMay(String maHang, String tenHang, int soLuongTon, double donGia,
                       int thoiGianBaoHanh, double congSuat) {
        super(maHang, tenHang, soLuongTon, donGia);
        if (thoiGianBaoHanh < 0) {
            throw new IllegalArgumentException("Thời gian bảo hành phải >= 0!");
        }
        if (congSuat < 0) {
            throw new IllegalArgumentException("Công suất phải >= 0!");
        }
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.congSuat = congSuat;
    }

    @Override
    public double tinhVAT() {
        return donGia * 0.10; // 10%
    }

    // Đánh giá tiêu thụ Điện Máy
    @Override
    public String danhGiaTieuThu() {
        if (soLuongTon < 3) {
            return "Bán được";
        }
        return "Không đánh giá";
    }
}

// 4. Lớp Hàng Sành Sứ
class HangSanhSu extends HangHoa {
    private String nhaSanXuat;
    private LocalDate ngayNhapKho;

    public HangSanhSu(String maHang, String tenHang, int soLuongTon, double donGia,
                      String nhaSanXuat, LocalDate ngayNhapKho) {
        super(maHang, tenHang, soLuongTon, donGia);
        this.nhaSanXuat = nhaSanXuat;
        this.ngayNhapKho = ngayNhapKho;
    }

    @Override
    public double tinhVAT() {
        return donGia * 0.10; // 10%
    }

    // Đánh giá tiêu thụ Sành Sứ
    @Override
    public String danhGiaTieuThu() {
        long soNgayLuuKho = ChronoUnit.DAYS.between(ngayNhapKho, LocalDate.now());
        if (soLuongTon > 50 && soNgayLuuKho > 10) {
            return "Bán chậm";
        }
        return "Không đánh giá";
    }
}

// YÊU CẦU 3: LỚP QUẢN LÝ DSHH 

class DanhSachHangHoa {
    private List<HangHoa> listHangHoa;

    public DanhSachHangHoa() {
        this.listHangHoa = new ArrayList<>();
    }

    // Tìm kiếm xem mã hàng đã tồn tại chưa
    public HangHoa timKiem(String maHang) {
        for (HangHoa hh : listHangHoa) {
            if (hh.getMaHang().equalsIgnoreCase(maHang)) {
                return hh;
            }
        }
        return null;
    }

    // Thêm hàng hóa (Kiểm tra trùng mã)
    public boolean themHangHoa(HangHoa hh) {
        if (timKiem(hh.getMaHang()) != null) {
            System.out.println("Thêm thất bại: Mã hàng '" + hh.getMaHang() + "' đã tồn tại!");
            return false;
        }
        listHangHoa.add(hh);
        System.out.println("Thêm thành công sản phẩm: " + hh.tenHang);
        return true;
    }

    // Hiển thị danh sách
    public void hienThiDanhSach() {
        System.out.println("\n------------------- DANH SÁCH HÀNG HÓA SIÊU THỊ -------------------");
        if (listHangHoa.isEmpty()) {
            System.out.println("Danh sách hiện tại đang trống!");
            return;
        }
        for (HangHoa hh : listHangHoa) {
            System.out.println(hh);
        }
        System.out.println("-------------------------------------------------------------------");
    }
}

//LỚP CHÍNH

public class Practice2 {
    public static void main(String[] args) {

        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        DanhSachHangHoa qlhh = new DanhSachHangHoa();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("=== KHỞI TẠO DỮ LIỆU MẪU ===");
        
        // 1. Thêm Hàng Thực Phẩm mẫu
        HangHoa tp1 = new HangThucPham("TP01", "Sữa tươi", 10, 25000,
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 6, 1), "Vinamilk");
        qlhh.themHangHoa(tp1);

        // 2. Thêm Hàng Điện Máy mẫu
        HangHoa dm1 = new HangDienMay("DM01", "Tủ lạnh", 2, 12000000, 24, 1.5);
        qlhh.themHangHoa(dm1);

        // 3. Thêm Hàng Sành Sứ mẫu
        HangHoa ss1 = new HangSanhSu("SS01", "Bộ bát đĩa", 60, 350000,
                "Bát Tràng", LocalDate.now().minusDays(15));
        qlhh.themHangHoa(ss1);

        // 4. Thử thêm trùng mã để kiểm tra Yêu cầu 3
        System.out.println("\n=== KIỂM TRA TRÙNG MÃ HÀNG ===");
        HangHoa tpTrung = new HangThucPham("TP01", "Sữa chua", 5, 10000,
                LocalDate.now(), LocalDate.now().plusDays(10), "TH True Milk");
        qlhh.themHangHoa(tpTrung);

        // Hiển thị danh sách và kết quả đánh giá tiêu thụ
        qlhh.hienThiDanhSach();
    }
}