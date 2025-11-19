package gui.controllers;

import bus.QuanLiKhachHang_Business;
import bus.QuanLyHoaDon_Business;
import bus.QuanLyPhong_Bussiness;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import dto.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import utils.NumberFormat;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ThuePhong_Controller implements Initializable {
    @FXML
    private Button btn_after;

    @FXML
    private Button btn_pre;

    @FXML
    private JFXButton btn_thuePhong;

    @FXML
    private JFXButton btn_timKH;

    @FXML
    private FlowPane fp_displayPhong;

    @FXML
    private FlowPane fl_displayPhongThue;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField tf_cccd;

    @FXML
    private TextField tf_ngaysinh;

    @FXML
    private TextField tf_sdt;

    @FXML
    private TextField tf_soNguoiThue;

    @FXML
    private TextField tf_tang;

    @FXML
    private TextField tf_tenKH;

    @FXML
    private TextField tf_timKH;

    @FXML
    private TextField tf_thanhTien;

    @FXML
    private DatePicker dp_ngayTraPhong;

    private KhachHang khachHang = null;
    private ArrayList<Phong> listPhong = null;
    private NhanVien nhanVien;
    private int tang = 1;
    private int soNguoiThue = 0;
    private ArrayList<Phong> listPhongDuocChon = new ArrayList<>();
    private Set<KhachHang> set_khachHang = new HashSet<>();
    private ArrayList<PhongThue> listPhongThue = new ArrayList<>();
    private CaLamViec caLamViec;
    private String maHoaDon;
    private boolean isCoHoaDon;
    private LocalDate ngaTraPhong;

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public int getSoNguoiThue() {
        return soNguoiThue;
    }

    public void setSet_khachHang(Set<KhachHang> set_khachHang) {
        this.set_khachHang = set_khachHang;
    }

    public Set<KhachHang> getSet_khachHang() {
        return set_khachHang;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void themPhongThue(Phong phong, PhongThue phongThue) {
        this.listPhongDuocChon.add(phong);
        this.listPhongThue.add(phongThue);
    }

    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public LocalDate getNgayTraPhong() {
        return ngaTraPhong;
    }

    public FlowPane getFp_displayPhongThue() {
        return fl_displayPhongThue;
    }

    public ArrayList getListPhongThue() {
        return listPhongThue;
    }

    public ArrayList getListPhongDuocChon() {
        return listPhongDuocChon;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            tf_tang.setText(String.valueOf(tang));
            tf_thanhTien.setText("0 VND");
            tf_soNguoiThue.setText("0");
            btn_pre.setVisible(false);

            dp_ngayTraPhong.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    // Không cho phép chọn các ngày trước ngày hiện tại
                    if (date.isBefore(LocalDate.now().plusDays(1))) {
                        setDisable(true); // Vô hiệu hóa ngày
                        setStyle("-fx-background-color: #d3d3d3;"); // Màu xám để biểu thị ngày không hợp lệ
                    }

                    // Tô màu đỏ cho ngày hiện tại
                    if (date.equals(LocalDate.now())) {
                        setStyle("-fx-background-color: #ffaaaa; -fx-text-fill: black;"); // Màu đỏ
                    }
                }
            });

            dp_ngayTraPhong.setValue(LocalDate.now().plusDays(1));
            ngaTraPhong = dp_ngayTraPhong.getValue();

            listPhong = QuanLyPhong_Bussiness.getAllPhongConTrongTheoTang(tang, dp_ngayTraPhong.getValue());
            updateDSPhong();

            btn_timKH.setOnAction(event -> {
                timKhachHang();
            });

            btn_pre.setOnAction(event -> {
                if (tang != 1){
                    tf_tang.setText(String.valueOf(--tang));
                    listPhong.clear();
                    listPhong = QuanLyPhong_Bussiness.getAllPhongConTrongTheoTang(tang, dp_ngayTraPhong.getValue());
                    updateDSPhong();
                    btn_after.setVisible(true);

                    if (tang == 1){
                        btn_pre.setVisible(false);
                    }
                }
            });

            btn_after.setOnAction(event -> {
                tang++;
                int check = tang + 1;
                if (QuanLyPhong_Bussiness.getAllPhongConTrongTheoTang(check, dp_ngayTraPhong.getValue()).isEmpty()){
                    btn_after.setVisible(false);
                }

                tf_tang.setText(String.valueOf(tang));
                listPhong.clear();
                listPhong = QuanLyPhong_Bussiness.getAllPhongConTrongTheoTang(tang, dp_ngayTraPhong.getValue());
                updateDSPhong();
                btn_pre.setVisible(true);
            });

            tf_soNguoiThue.onActionProperty().set(event -> {
                if (!tf_soNguoiThue.getText().matches("\\d*") || tf_soNguoiThue.getText().isEmpty()) {
                    showDialog(stackPane, "Lỗi", "Vui lòng nhập số nguyên dương và không được trống", "OK");
                    tf_soNguoiThue.setText("0");
                    tf_soNguoiThue.requestFocus();
                    return;
                }
                soNguoiThue = Integer.parseInt(tf_soNguoiThue.getText());
            });

            dp_ngayTraPhong.setOnAction(event -> {
                ngaTraPhong = dp_ngayTraPhong.getValue();
                listPhong.clear();
                listPhong = QuanLyPhong_Bussiness.getAllPhongConTrongTheoTang(tang, dp_ngayTraPhong.getValue());
                updateDSPhong();
            });

            btn_thuePhong.setOnAction(event -> {
                if(khachHang == null) {
                    showDialog(stackPane, "Lỗi", "Vui lòng tìm thông tin khách hàng trước!", "OK");
                    tf_timKH.requestFocus();
                    return;
                }
                if (soNguoiThue == 0) {
                    showDialog(stackPane, "Lỗi", "Vui lòng nhập số người thuê", "OK");
                    tf_soNguoiThue.requestFocus();
                    return;
                }
                if (listPhongThue.isEmpty()) {
                    showDialog(stackPane, "Lỗi", "Vui lòng chọn phòng trước khi thuê", "OK");
                    return;
                }
                if (set_khachHang.size() < soNguoiThue) {
                    showDialog(stackPane, "Lỗi", "Số lượng khách hàng chưa đủ", "OK");
                    return;
                }

                if (khachHang.getNhomTuoi().equals("TREEM")){
                    showDialog(stackPane, "Lỗi", "Khách hàng chưa là người lớn không thể thuê phòng", "OK");
                    return;
                }

                if (taoHoaDon()){
                    showDialog(stackPane, "Thông báo", "Thuê phòng thành công", "OK");
                }
            });
        });
    }

    public void lockDP_ngayTraPhong(boolean lock){
        dp_ngayTraPhong.setDisable(lock);
    }

    public String taoMaHoaDonMoiNhat(){
        String maHoaDon = "HDKC" + caLamViec.getThoiGianBatDau().getYear()
                + String.format("%02d", caLamViec.getThoiGianBatDau().getMonthValue())
                + String.format("%02d", caLamViec.getThoiGianBatDau().getDayOfMonth())
                + (QuanLyHoaDon_Business.layKiTuCuoiCuaMaHoaDonLonNhat("KC") + 1);

        return maHoaDon;
    }

    public void callDialog(String title, String content, String buttonText) {
        showDialog(stackPane, title, content, buttonText);
    }

    public void showDialog(StackPane root, String title, String content, String buttonText) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(title));
        dialogLayout.setBody(new Text(content));

        JFXButton closeButton = new JFXButton(buttonText);
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("lbMain");

        closeButton.setOnAction(event -> dialog.close());
        dialogLayout.setActions(closeButton);
        dialog.show();
    }

    public void timKhachHang(){
        if (tf_timKH.getText().isEmpty()) {
            showDialog(stackPane, "Lỗi", "Vui lòng nhập số điện thoại hoặc căn cước công dân khách hàng", "OK");
            tf_timKH.setText("");
            tf_timKH.requestFocus();
            tf_tenKH.setText("");
            tf_cccd.setText("");
            tf_ngaysinh.setText("");
            tf_sdt.setText("");
            tf_timKH.requestFocus();
            return;
        }

        if (!tf_timKH.getText().matches("\\d*")){
            showDialog(stackPane, "Lỗi", "Số điện thoại hoặc căn cước công dân không hợp lệ", "OK");
            tf_timKH.setText("");
            tf_timKH.requestFocus();
            tf_tenKH.setText("");
            tf_cccd.setText("");
            tf_ngaysinh.setText("");
            tf_sdt.setText("");
            tf_timKH.requestFocus();
            return;
        }

        khachHang = QuanLiKhachHang_Business.timKhachHangTheoSDTorCCCD(tf_timKH.getText());

        if (khachHang == null) {
            showDialog(stackPane, "Lỗi", "Không tìm thấy khách hàng", "OK");
            tf_timKH.setText("");
            tf_timKH.requestFocus();
            tf_tenKH.setText("");
            tf_cccd.setText("");
            tf_ngaysinh.setText("");
            tf_sdt.setText("");
            tf_timKH.requestFocus();
            return;
        }

        tf_tenKH.setText(khachHang.getTenKhachHang());
        tf_cccd.setText(khachHang.getCCCD());
        tf_ngaysinh.setText(khachHang.getNgaySinh().getDayOfMonth() + "/" + khachHang.getNgaySinh().getMonthValue() + "/" + khachHang.getNgaySinh().getYear());
        tf_sdt.setText(khachHang.getSoDienThoai());

        if(QuanLyHoaDon_Business.getMaHoaDonChuaThanhToanCuaKhachHang(khachHang) == null){
            isCoHoaDon = false;
            maHoaDon = taoMaHoaDonMoiNhat();
        }
        else {
            isCoHoaDon = true;
            maHoaDon = QuanLyHoaDon_Business.getMaHoaDonChuaThanhToanCuaKhachHang(khachHang);
        }
    }

    public void updateDSPhong(){
        fp_displayPhong.getChildren().clear();

        for (Phong phong : listPhong){
            Phong_controller phong_controller = new Phong_controller(phong, listPhongDuocChon.contains(phong));

            AnchorPane root = phong_controller.getRoot();
            phong_controller.setThuePhong_controller(this);

            fp_displayPhong.getChildren().add(root);
        }
    }

    public boolean taoHoaDon(){
        int phiBoSung = 0;
        String ghiChu = "";
        boolean trangThai = false;

        HoaDon hoaDon = new HoaDon(maHoaDon, caLamViec, khachHang, LocalDate.now().atStartOfDay(), dp_ngayTraPhong.getValue().atStartOfDay(), phiBoSung, trangThai, ghiChu);
        hoaDon.setListPhongThue(listPhongThue);
        QuanLyHoaDon_Business.taoHoaDon(hoaDon, isCoHoaDon);
        QuanLyPhong_Bussiness.updateTrangThai(listPhongThue, "DANGTHUE");
        reloadThuePhongPage(); 
        return true;
    }

    public void tinhTongThanhTien(){
        double thanhTien = 0;
        for (PhongThue phongThue : listPhongThue){
            thanhTien += phongThue.getThanhTien();
        }

        tf_thanhTien.setText(NumberFormat.currencyFormat(thanhTien));
    }

    public void reloadThuePhongPage(){
        tf_timKH.setText("");
        tf_tenKH.setText("");
        tf_cccd.setText("");
        tf_ngaysinh.setText("");
        tf_sdt.setText("");
        tf_thanhTien.setText("0.0 VND");
        tf_soNguoiThue.setText("0");
        tf_tang.setText("1");
        dp_ngayTraPhong.setValue(LocalDate.now());
        listPhong.clear();
        listPhong = QuanLyPhong_Bussiness.getAllPhongConTrongTheoTang(tang, dp_ngayTraPhong.getValue());
        updateDSPhong();
        listPhongThue.clear();
        set_khachHang.clear();
        fl_displayPhongThue.getChildren().clear();
        dp_ngayTraPhong.setDisable(false);
    }
}
