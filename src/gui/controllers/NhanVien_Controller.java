package gui.controllers;

import bus.QuanLyNhanVien_Business;
import com.jfoenix.controls.JFXComboBox;
import dao.KhachHang_DAO;
import dao.NhanVien_DAO;
import dto.KhachHang;
import dto.NhanVien;
import dto.enum_class.CapBac;
import dto.enum_class.TrangThaiNhanVien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NhanVien_Controller implements Initializable {
    @FXML
    private DatePicker txtNgayketthuc;
    @FXML
    private DatePicker txtngaybatdaulam;

    @FXML
    private JFXComboBox<String> ccbcapbac;
    @FXML
    private JFXComboBox<String> cbbtrangthai;
    @FXML
    private Button btnCapNhat;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnThem;

    @FXML
    private TableView<NhanVien> table;

    @FXML
    private TextField txtCCCD;
    @FXML
    private TextField txtMaNV;

    @FXML
    private DatePicker txtNgaySinh;
    @FXML
    private TextField txtsodienthoai;

    @FXML
    private TextField txtTenNV;

    @FXML
    private TextField txtTimKiem;
    @FXML
    private TableColumn<NhanVien, String> trangthaiColumn;
    @FXML
    private TableColumn<NhanVien, String> capbacColumn;

    @FXML
    private TableColumn<NhanVien, String> cccdColumn;


    @FXML
    private TableColumn<NhanVien, String> maNVColumn;

    @FXML
    private TableColumn<NhanVien, String> ngaybatdauColumn;

    @FXML
    private TableColumn<NhanVien, String> ngayketthucColumn;

    @FXML
    private TableColumn<NhanVien, String> ngaysinhColumn;

    @FXML
    private TableColumn<NhanVien, String> SodienthoaiColumn;
    @FXML
    private TableColumn<NhanVien, String> tenNVColumn;

    private ObservableList<NhanVien> list;

    @FXML
    private TableColumn<NhanVien, String> EmailColumn;
    @FXML
    private TextField txtemaiil;

    private NhanVien_DAO daonv = new NhanVien_DAO();
    List<NhanVien> NhanvienList = new ArrayList<>();
    private QuanLyNhanVien_Business quanLyNhanVien_business = new QuanLyNhanVien_Business();

    private void clearFields() {
        txtMaNV.clear();
        txtTenNV.clear();
        txtsodienthoai.clear();
        txtCCCD.clear();
        txtemaiil.clear();
        txtNgaySinh.setValue(null);
        txtngaybatdaulam.setValue(null);
        txtNgayketthuc.setValue(null);
        cbbtrangthai.setValue(null);
        ccbcapbac.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<NhanVien> getdanhsachtrangthai = quanLyNhanVien_business.getdanhsachtrangthai();
        for (NhanVien nv:getdanhsachtrangthai){
            cbbtrangthai.getItems().add(nv.getTrangThaiNhanVien().name());
        }
        List<NhanVien> getdanhsachcapbac= quanLyNhanVien_business.getdscapbac();
        for (NhanVien nv:getdanhsachcapbac){
            ccbcapbac.getItems().add(nv.getCapBac().name());
        }
        maNVColumn.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        tenNVColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        cccdColumn.setCellValueFactory(new PropertyValueFactory<>("cCCD"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        SodienthoaiColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        ngaysinhColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        ngaybatdauColumn.setCellValueFactory(new PropertyValueFactory<>("ngayBatDauLamViec"));
        ngayketthucColumn.setCellValueFactory(new PropertyValueFactory<>("ngayKetThucLamViec"));
        trangthaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThaiNhanVien"));
        capbacColumn.setCellValueFactory(new PropertyValueFactory<>("capBac"));
        // Đọc dữ liệu vào TableView
        DocDuLieuDatabaseVaoTable();
        txtMaNV.setEditable(false);
        btnThem.setOnAction(actionEvent -> {
            //Lấy dữ liệu tưf textField
            NhanVien nhanvien = valid();
            if (nhanvien == null) {
                return;
            }
            List<NhanVien> kiemtra= quanLyNhanVien_business.kiemtra();
                for(NhanVien nv:kiemtra){
                    if(nv.getSoDienThoai().equals(nhanvien.getSoDienThoai())){
                        JOptionPane.showMessageDialog(null, "Nhân viên đã tồn tại");
                        return;
                    }
                }
            try {
                daonv.create(nhanvien);
                DocDuLieuDatabaseVaoTable();
                JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng");
            }
            clearFields();
        });
        btnCapNhat.setOnAction(actionEvent -> {
            // Kiểm tra khách hàng đã được chọn từ bảng chưa
            NhanVien chonNhanVien = table.getSelectionModel().getSelectedItem();

            if (chonNhanVien != null) {
                // Lấy thông tin mới từ các trường TextField
                NhanVien nhanvienMoi = valid();

                if (nhanvienMoi != null) {  
                    nhanvienMoi.setMaNhanVien(chonNhanVien.getMaNhanVien());
                    try {
                        boolean capNhatThanhCong = daonv.update(nhanvienMoi);

                        if (capNhatThanhCong) {
                            JOptionPane.showMessageDialog(null, "Cập nhật Nhân viên thành công");
                            DocDuLieuDatabaseVaoTable();
                        } else {
                            JOptionPane.showMessageDialog(null, "Cập nhật Nhân viên thất bại");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật nhân viên: " + e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên từ bảng để cập nhật");
            }
            clearFields();
        });

        btnLamMoi.setOnAction(actionEvent -> {
            //Sự kiên làm mới
            DocDuLieuDatabaseVaoTable();
            clearFields();
            LocalDate ngayhientai=LocalDate.now();
            txtngaybatdaulam.setValue(ngayhientai);
            txtTimKiem.clear();
        });
        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            String keyWord = txtTimKiem.getText().trim();
            if (keyWord.isEmpty()) {
                table.setItems(FXCollections.observableArrayList(NhanvienList));
            } else {
                ArrayList<NhanVien> temp = daonv.timKhachHangSDT(keyWord);
                ObservableList<NhanVien> list = FXCollections.observableArrayList(temp);
                table.setItems(list);
            }
        });

        table.setOnMouseClicked(mouseEvent -> {
            NhanVien chonNhanVien = table.getSelectionModel().getSelectedItem();
            if (chonNhanVien != null) {
                txtMaNV.setText(chonNhanVien.getMaNhanVien());
                txtTenNV.setText(chonNhanVien.getHoTen());
                txtCCCD.setText(chonNhanVien.getCCCD());
                txtemaiil.setText(chonNhanVien.getEmail());
                txtsodienthoai.setText(chonNhanVien.getSoDienThoai());
                ccbcapbac.setValue(chonNhanVien.getCapBac().toString());
                cbbtrangthai.setValue(chonNhanVien.getTrangThaiNhanVien().toString());
                txtngaybatdaulam.setValue(chonNhanVien.getNgayBatDauLamViec());
                if (chonNhanVien.getNgayKetThucLamViec() != null) {
                    txtNgayketthuc.setValue(chonNhanVien.getNgayKetThucLamViec());
                } else {
                    txtNgayketthuc.setValue(null);
                }
                txtNgaySinh.setValue(chonNhanVien.getNgaySinh());
            }
        });
    }

    public  NhanVien valid() {
    	    String tenNV = txtTenNV.getText();
    	    String cccd = txtCCCD.getText();
    	    String sdt = txtsodienthoai.getText();
    	    String email = txtemaiil.getText();
    	    LocalDate ngaySinh = txtNgaySinh.getValue();
    	    LocalDate ngaybatdau = txtngaybatdaulam.getValue();
    	    LocalDate ngayketthuc = txtNgayketthuc.getValue();
    	    String trangthai = cbbtrangthai.getValue();
    	    String capbac = ccbcapbac.getValue();

    	    if (tenNV == null || tenNV.trim().isEmpty() ||
    	            sdt == null || sdt.trim().isEmpty() ||
    	            cccd == null || cccd.trim().isEmpty() ||
    	            ngaySinh == null || ngaybatdau == null || trangthai == null || trangthai.trim().isEmpty() || capbac == null || capbac.trim().isEmpty()) {
    	        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin");
    	        return null;
    	    }

    	    // Kiểm tra họ tên
    	    if (tenNV.trim().split("\\s+").length < 2) {
    	        JOptionPane.showMessageDialog(null, "Họ và tên khách hàng phải có ít nhất 2 từ");
    	        return null;
    	    }

    	    // Kiểm tra số CCCD
    	    if (cccd.length() != 12 || !cccd.matches("\\d{12}")) {
    	        JOptionPane.showMessageDialog(null, "Số CCCD của khách hàng phải gồm 12 ký tự số");
    	        return null;
    	    }

    	    // Kiểm tra ngày sinh
    	    if (ngaySinh.isAfter(LocalDate.now())) {
    	        JOptionPane.showMessageDialog(null, "Ngày sinh phải trước ngày hiện tại");
    	        return null;
    	    } else {
    	        int tuoi = Period.between(ngaySinh, LocalDate.now()).getYears();
    	        if (tuoi < 18) {
    	            JOptionPane.showMessageDialog(null, "Tuổi không được nhỏ hơn 18");
    	            return null;
    	        }
    	    }

    	    if (ngayketthuc != null && ngaybatdau.isAfter(ngayketthuc)) {
    	        JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc");
    	        return null;
    	    }

    	    // Kiểm tra số điện thoại
    	    if (sdt.length() != 10 || !sdt.matches("\\d{10}")) {
    	        JOptionPane.showMessageDialog(null, "Số điện thoại phải gồm 10 chữ số");
    	        return null;
    	    }

    	    if (ngayketthuc == null) {
    	        ngayketthuc = null;
    	    }
    	    String maNV = generateMaNV(ngaySinh, ngaybatdau);
    	    TrangThaiNhanVien trangthainv = TrangThaiNhanVien.valueOf(trangthai);
    	    CapBac capbacnv = CapBac.valueOf(capbac);

    	    NhanVien nhanvien = new NhanVien(maNV, tenNV, cccd, sdt, email, ngaySinh, ngaybatdau, ngayketthuc, trangthainv, capbacnv);
    	    return nhanvien;
    	}

    private String generateMaNV(LocalDate ngaySinh, LocalDate ngaybatdau) {
        String prefix = "NV";
        String yearOfBirth = String.valueOf(ngaySinh.getYear()).substring(2); // Lấy 2 chữ số cuối của năm sinh
        String yearOfStart = String.valueOf(ngaybatdau.getYear()).substring(2);  

        int soThuTu = daonv.getSoThuTuTiepTheo(); 

        String soThuTuFormatted = String.format("%03d", soThuTu);

        return prefix + yearOfBirth + yearOfStart + soThuTuFormatted;
    }



    //Hàm đoc từ Database
    public void DocDuLieuDatabaseVaoTable() {
        NhanvienList = daonv.getNhanVien();
        list = FXCollections.observableArrayList(NhanvienList);
        table.setItems(list);
        table.refresh();
    }

}
