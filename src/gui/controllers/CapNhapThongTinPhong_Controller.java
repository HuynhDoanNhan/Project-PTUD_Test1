package gui.controllers;

import dao.LoaiGiuongSuDung_DAO;
import dao.LoaiPhong_DAO;
import dao.Phong_DAO;
import dto.LoaiGiuongSuDung;
import dto.LoaiPhong;
import dto.Phong;
import dto.enum_class.TrangThaiPhong;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import bus.QuanLyPhong_Bussiness;

public class CapNhapThongTinPhong_Controller implements Initializable {
    private QuanLyPhong_Bussiness phongBussines;

    @FXML
    private TextField txtdientich;

    @FXML
    private TextField txtgiaphong;

    @FXML
    private TextField txtmaphong;

    @FXML
    private TextField txtsonguoi;
    @FXML
    private TextField txttrangthai;

    @FXML
    private ComboBox<String> comBoBoxLoaiPhong;

    @FXML
    private ComboBox<String> comBoboxloaigiuong;

    @FXML
    private ComboBox<String> trangthaiPhong;


    @FXML
    private Button btnluu;
    @FXML
    private Button btnhuy;
    private double giaPhong_x = 1000000;
    private double heSogia = 1;
    private Phong_DAO phongDAO;
    private Stage parentStage;

	private QuanLyPhong_Bussiness phongBusiness = new QuanLyPhong_Bussiness();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnluu.setOnAction(event -> {
            if (capnhapthongtinphong()) { 
                closeWindow(); 
                if (parentStage != null) {
                    parentStage.close();
                }
            }
        });

        btnhuy.setOnAction(event -> {
            closeWindow(); 
        });
    }
    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
    public void setRoomDetails(String maPhong, String maLoaiPhong, String maLoaiGiuong, int songuoi,  double giaPhong, String trangThai) {
        phongBussines = new QuanLyPhong_Bussiness();
        txtmaphong.setText(maPhong);
        txtsonguoi.setText(String.valueOf(songuoi));
        txtgiaphong.setText(String.valueOf(giaPhong));
        Phong_DAO phongDAO = new Phong_DAO();

        LoaiPhong_DAO loaiPhong_dao = new LoaiPhong_DAO();
        comBoBoxLoaiPhong.getItems().clear();
        try {
            List<LoaiPhong> dsLoaiPhong = phongBussines.ReadLoaiPhong();
            if (dsLoaiPhong != null && !dsLoaiPhong.isEmpty()) {
                for (LoaiPhong loaiPhong : dsLoaiPhong) {
                    comBoBoxLoaiPhong.getItems().add(loaiPhong.getTenLoaiPhong());
                }
                if (!comBoBoxLoaiPhong.getItems().isEmpty()) {
                    comBoBoxLoaiPhong.setValue(comBoBoxLoaiPhong.getItems().get(0));
                }
            } else {
                System.out.println("Không có loại phòng nào để hiển thị.");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            System.out.println("Có lỗi xảy ra khi nạp loại phòng vào ComboBox.");
        }
        LoaiGiuongSuDung_DAO loaigiuong_dao = new LoaiGiuongSuDung_DAO();

        comBoboxloaigiuong.getItems().clear();

        try {
            List<LoaiGiuongSuDung> sdloaigiuong = phongBussines.ReadLoaiGiuongSuDung();

            if (sdloaigiuong != null && !sdloaigiuong.isEmpty()) {
                for (LoaiGiuongSuDung loaigiuong : sdloaigiuong) {
                    comBoboxloaigiuong.getItems().add(loaigiuong.getMaLoaiGiuong());
                }
                if (!comBoboxloaigiuong.getItems().isEmpty()) {
                    comBoboxloaigiuong.setValue(comBoboxloaigiuong.getItems().get(0));
                }
            } else {
                System.out.println("Không có loại giường nào để hiển thị.");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            System.out.println("Có lỗi xảy ra khi nạp loại giường vào ComboBox.");
        }
        Map<String, String> trangThaiMap_x = new HashMap<>();
        trangThaiMap_x.put("Phòng trống", "CONTRONG");
        trangThaiMap_x.put("Phòng đang dọn", "DANGDON");
        trangThaiMap_x.put("Phòng được thuê", "DANGTHUE");
        trangThaiMap_x.put("Phòng đang bảo trì", "DANGBAOTRI");
        trangThaiMap_x.put("Ngừng hoạt động", "NGUNGHOATDONG");
        trangthaiPhong.getItems().clear();  
        trangthaiPhong.getItems().addAll("Phòng trống", "Phòng đang dọn", "Phòng được thuê", "Phòng đang bảo trì","Ngừng hoạt động");

        trangthaiPhong.setValue(trangThai); 

        if (!trangthaiPhong.getItems().contains(trangThai)) {
            trangthaiPhong.getItems().add(0, trangThai);  
        }
        String trangThaiCode = trangThaiMap_x.get(trangthaiPhong.getValue());
        comBoBoxLoaiPhong.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            Map<String, Integer> soKhachToiDaTheoLoaiPhong = new HashMap<>();
            soKhachToiDaTheoLoaiPhong.put("Standard", 2);  
            soKhachToiDaTheoLoaiPhong.put("Superior", 2);  
            soKhachToiDaTheoLoaiPhong.put("Deluxe", 3);    
            soKhachToiDaTheoLoaiPhong.put("Suite", 3);    

            int soKhachToiDa = soKhachToiDaTheoLoaiPhong.getOrDefault(newValue, 0);
            txtsonguoi.setText(String.valueOf(soKhachToiDa));


            Map<String, String> giaTien = new HashMap<>();
            giaTien.put("Standard", "200000");
            giaTien.put("Superior", "500000");
            giaTien.put("Deluxe", "1000000");
            giaTien.put("Suite", "2000000");


            giaPhong_x = Double.parseDouble(giaTien.getOrDefault(newValue, "0"));
            updateGiaPhong(giaPhong_x, heSogia);
        });


        comBoboxloaigiuong.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                Double hesogia_x = null;

                switch (newValue) {
                    case "SINGLE":
                        hesogia_x = phongBusiness .getHesogia("SINGLE");
                        break;
                    case "DOUBLE":
                        hesogia_x = phongBusiness.getHesogia("DOUBLE");
                        break;	
                    case "KING":
                        hesogia_x = phongBusiness.getHesogia("KING");
                        break;
                    case "QUEEN":
                        hesogia_x = phongBusiness.getHesogia("QUEEN");
                        break;
                    default:
                        System.out.println("Loại giường không hợp lệ: " + newValue);
                        break;
                }

                if (hesogia_x != null) {
                    heSogia = hesogia_x; 
                    updateGiaPhong(giaPhong_x, heSogia);
                }
            }
        });
    }

    // Method to update room price
    public void updateGiaPhong(double giaPhong, double heSogia) {
    	  String maphong = txtmaphong.getText();
          Map<String, String> loaiPhongSuDung = new HashMap<>();
          loaiPhongSuDung.put("Deluxe", "DLX");
          loaiPhongSuDung.put("Standard", "STD");
          loaiPhongSuDung.put("Superior", "SUP");
          loaiPhongSuDung.put("Suite", "SUT");
          String maloaiphong = loaiPhongSuDung.get(comBoBoxLoaiPhong.getValue());
      	  TrangThaiPhong trangThaiPhong = TrangThaiPhong.valueOf("CONTRONG");
  		  LoaiPhong loaiPhong = new LoaiPhong(maloaiphong,giaPhong);
  		  LoaiGiuongSuDung loaiGiuongSuDung = new LoaiGiuongSuDung(comBoboxloaigiuong.getValue(),heSogia);
          Phong p =new Phong(maphong, loaiPhong, loaiGiuongSuDung, Integer.parseInt(txtsonguoi.getText()), trangThaiPhong);
          txtgiaphong.setText(p.getGiaPhong()+"");
    }
    public boolean capnhapthongtinphong() {
        Map<String, String> trangThaiPhong = new HashMap<>();
        trangThaiPhong.put("Deluxe", "DLX");
        trangThaiPhong.put("Standard", "STD");
        trangThaiPhong.put("Superior", "SUP");
        trangThaiPhong.put("Suite", "SUT");
        String tenLoaiPhong = trangThaiPhong.get(comBoBoxLoaiPhong.getValue());

        String maPhong = txtmaphong.getText();
        int soNguoi = Integer.parseInt(txtsonguoi.getText());
        float giaPhong = Float.parseFloat(txtgiaphong.getText());
        Map<String, String> trangThaiMap_x = new HashMap<>();
        trangThaiMap_x.put("Phòng trống", "CONTRONG");
        trangThaiMap_x.put("Phòng đang dọn", "DANGDON");
        trangThaiMap_x.put("Phòng được thuê", "DANGTHUE");
        trangThaiMap_x.put("Phòng đang bảo trì", "DANGBAOTRI");
        trangThaiMap_x.put("Ngừng hoạt động", "NGUNGHOATDONG");
        String trangThaiCode = trangThaiMap_x.get(trangthaiPhong.getValue());
        if(trangThaiCode.equals("DANGTHUE")) {
        	JOptionPane.showMessageDialog(null, "Không được cập nhập phòng thuê khi không có thông tin khách hàng");
        	return false;
        }
        String maloaigiuong = comBoboxloaigiuong.getValue();
        TrangThaiPhong trangThaiPhong_x = TrangThaiPhong.valueOf(trangThaiCode);
        LoaiPhong loaiPhong = new LoaiPhong(tenLoaiPhong,giaPhong);
		LoaiGiuongSuDung loaiGiuongSuDung = new LoaiGiuongSuDung(maloaigiuong,heSogia);
        Phong p =new Phong(maPhong, loaiPhong, loaiGiuongSuDung, soNguoi, trangThaiPhong_x);
        try {
            boolean result = phongBussines.capnhatphong(p);
            if (result) {
    			JOptionPane.showMessageDialog(null, "Cập nhập thông tin phòng thành công");
                return true;
            } else {
            	JOptionPane.showMessageDialog(null, "Cập nhập thông tin phòng thất bại");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) btnluu.getScene().getWindow();
        stage.close();
    }
}
