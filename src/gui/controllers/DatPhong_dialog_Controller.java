package gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import dao.CaLamViec_DAO;
import dao.PhieuDatPhong_DAO;
import dao.PhongDat_DAO;
import dao.Phong_DAO;
import dto.*;
import dto.enum_class.TrangThaiPhieuDat;
import dto.enum_class.TrangThaiPhongDat;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DatPhong_dialog_Controller implements Initializable {
    public TextField tf_hoTen;
    public TextField tf_soDienThoai;
    public TextField tf_soCCCD;
    public TextField tf_soPhongThue;
    public TextField tf_ngayTraPhong;
    public TextField tf_ngayNhanPhong;
    public TextField tf_tienCoc;
    public TextField tf_tongTien;

    public TextField tf_tienKhachDua;
    public TextField tf_tienThoi;
    
    public DatPhong_Controller datPhong_Controller;
    
    public TableView<Phong> TB_phongDat;
    public TableColumn<Phong, String> col_maPhong;
    public TableColumn<Phong, String> col_loaiPhong;
    public TableColumn<Phong, Integer> col_soLuongNguoi;
    public TableColumn<Phong, String> col_loaiGiuong;
    public TableColumn<Phong, Double> col_thanhTien;
    public TableColumn<Phong, Double> col_giaPhong;
    public TableColumn<Phong, Double> col_tienCoc;
    public Button btnDatPhong;
    public Button btnThoat;
    
    @FXML
    private StackPane stackPane;
    
    @FXML
    private JFXButton btnGoiYTien1;

    @FXML
    private JFXButton btnGoiYTien2;

    @FXML
    private JFXButton btnGoiYTien3;

    private NhanVien nhanVien = new NhanVien();
    private CaLamViec caLamViec;
    private KhachHang khachHang = new KhachHang();
    private List<Phong> dsPhongDat = new ArrayList<>();
    private Phong_DAO phongDao = new Phong_DAO();
    private PhongDat_DAO chiTietPhongDao = new PhongDat_DAO();
    private PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
    private CaLamViec_DAO caLamViecDao = new CaLamViec_DAO();

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public void setDsPhongThue(List<Phong> dsPhongDat) {
        this.dsPhongDat = dsPhongDat;
    }

    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }
    
	public void setDatPhong_Controller(DatPhong_Controller datPhong_Controller) {
		this.datPhong_Controller = datPhong_Controller;
	}

	public ObservableList<Integer> soLuongKhachList;
	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	 //Định dạng tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    	
        col_maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        col_loaiGiuong.setCellValueFactory(cellData -> {
            Phong phong = cellData.getValue();
            return new SimpleStringProperty(phong.getLoaiGiuongSuDung().getMaLoaiGiuong());
        });
        col_loaiPhong.setCellValueFactory(celldata -> {
            Phong phong = (Phong) celldata.getValue();
            switch (phong.getLoaiPhong().getMaLoaiPhong()) {
                case "STD":
                    return new SimpleStringProperty("Standard");
                case "SUP":
                    return new SimpleStringProperty("Superior");
                case "DLX":
                    return new SimpleStringProperty("Deluxe");
                case "SUT":
                    return new SimpleStringProperty("Suite");
            }
            return null;
        });
        col_soLuongNguoi.setCellValueFactory(cellData -> {
        	Phong phong = cellData.getValue();
        	 soLuongKhachList = datPhong_Controller.getSoLuongKhachList();
        	 
        	 if (soLuongKhachList.isEmpty() || phong == null) {
        	        return new SimpleObjectProperty(0); 
        	 }
        	 int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
        	 if (index >= 0 && index < soLuongKhachList.size()) {
        	        int soLuongKhach = soLuongKhachList.get(index);  // Lấy số lượng khách tại dòng tương ứng
        	        return new SimpleObjectProperty(soLuongKhach);
        	    }
        	return new SimpleObjectProperty(0);
        });
        
        col_giaPhong.setCellValueFactory(cellData -> {
        	Phong phong = (Phong) cellData.getValue();
        	double giaCoBan = 0;
            switch (phong.getLoaiPhong().getMaLoaiPhong().toString()) {
            case "STD":
            	giaCoBan = 200000;
                break;
            case "SUP":
            	giaCoBan = 500000;
            case "DLX":
            	giaCoBan = 1000000;
                break;
            case "SUT":
            	giaCoBan = 2000000;
                break;
            }
    		
            double heSoGia = 1;
            switch (phong.getLoaiGiuongSuDung().getMaLoaiGiuong().toString()) {
            case "DOUBLE":
            	heSoGia = 1.2;
                break;
            case "KING":
            	heSoGia = 1.2;
            case "QUEEN":
            	heSoGia = 1;
                break;
            case "SINGLE":
            	heSoGia = 1;
                break;
            }
            double giaPhong = giaCoBan * heSoGia;
            return new SimpleObjectProperty<>(giaPhong); 
        });
        
        
        col_thanhTien.setCellValueFactory(cellData -> {
            Phong phong = (Phong) cellData.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ngayNhan = LocalDate.parse(tf_ngayNhanPhong.getText(), formatter);
            LocalDate ngayTra = LocalDate.parse(tf_ngayTraPhong.getText(), formatter);
            long soNgayO = ChronoUnit.DAYS.between(ngayNhan, ngayTra);
            if (soNgayO == 0) {
                soNgayO = 1; // Nếu ngày nhận và ngày trả giống nhau, tính là 1 ngày
            }
            double giaPhong = col_giaPhong.getCellObservableValue(phong).getValue();
            double thanhTien = soNgayO * giaPhong;
            return new SimpleObjectProperty<>(thanhTien);
        });
        col_tienCoc.setCellValueFactory(cellData -> {
            Phong phong = (Phong) cellData.getValue();
            double phanTramCoc = 0.3;  // mặc định STD
            switch (phong.getLoaiPhong().getMaLoaiPhong()) {
                case "SUP":
                    phanTramCoc = 0.3;
                    break;
                case "DLX":
                    phanTramCoc = 0.35;
                    break;
                case "SUT":
                    phanTramCoc = 0.4;
                    break;
                case "STD":
                default:
                    phanTramCoc = 0.3;
            }
            double thanhTien = col_thanhTien.getCellObservableValue(phong).getValue();
            double tienCoc = thanhTien * phanTramCoc;
            return new SimpleObjectProperty<>(tienCoc);
        });


        // Định dạng số tiền cho cột giaPhong
        col_giaPhong.setCellFactory(column -> new TableCell<Phong, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Định dạng số tiền cho cột thanhTien
        col_thanhTien.setCellFactory(column -> new TableCell<Phong, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Định dạng số tiền cho cột tienCoc
        col_tienCoc.setCellFactory(column -> new TableCell<Phong, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });


        Platform.runLater(() -> {
            //Hiển thị trên các textfield
            tf_hoTen.setText(khachHang.getTenKhachHang());
            tf_soCCCD.setText(khachHang.getCCCD());
            tf_soDienThoai.setText(khachHang.getSoDienThoai());

            ObservableList<Phong> listDatPhong = FXCollections.observableArrayList(dsPhongDat);
            TB_phongDat.setItems(listDatPhong);

            tf_soPhongThue.setText(String.valueOf(listDatPhong.size()));

            tinhTongTienVaTienCoc();

        });
        //Xử lí sự kiện thoát
        btnThoat.setOnAction(actionEvent -> {
        	soLuongKhachList.clear();
            Stage stage = (Stage) btnThoat.getScene().getWindow();
            datPhong_Controller.reload();
            stage.close();
        });
        //Xử lí sự kiện đặt phòng
        btnDatPhong.setOnAction(actionEvent -> {
            if (tf_ngayNhanPhong.getText().trim().isEmpty() || tf_ngayTraPhong.getText().trim().isEmpty()) {
                showDialog(stackPane, "Lỗi", "Vui lòng nhập ngày nhận và ngày trả phòng.", "OK");
            } else {
                //tạo chi tiết phòng đặt
                List<PhongDat> dsPhongDat = new ArrayList<>();
                for (Phong p : TB_phongDat.getItems()) {
                    PhongDat chiTietPhongDat = new PhongDat();
                    chiTietPhongDat.setPhong(p);
                    chiTietPhongDat.setDonGia(col_giaPhong.getCellObservableValue(p).getValue());
                    chiTietPhongDat.setSoNguoi(col_soLuongNguoi.getCellObservableValue(p).getValue());
                    chiTietPhongDat.setTrangThaiPhongDat(TrangThaiPhongDat.CHONHAN);
                    dsPhongDat.add(chiTietPhongDat);
                }
                //tạo phiếu đặt
                PhieuDatPhong phieuDatPhong = new PhieuDatPhong();
                phieuDatPhong.setMaPhieu(taoMaPhieu());
                phieuDatPhong.setKhachHang(khachHang);
                phieuDatPhong.setCaLamViec(caLamViec);
                phieuDatPhong.setNgayDatPhong(LocalDate.now());
                phieuDatPhong.setNgayNhanPhong(LocalDate.parse(tf_ngayNhanPhong.getText()));
                phieuDatPhong.setNgayTraPhong(LocalDate.parse(tf_ngayTraPhong.getText()));
                phieuDatPhong.setTrangThaiPhieuDat(TrangThaiPhieuDat.CHONHAN);

                //Tính tổng tiền cọc dựa theo tiền cọc và thành tiền trong Chi tiết phiếu đặt
                double tongTienCoc = 0;
				try {
					tongTienCoc = currencyFormat.parse(tf_tienCoc.getText()).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                phieuDatPhong.setTienCoc(tongTienCoc);

                //lưu phiếu đặt và phòng đặt vào database
                if (phieuDatPhongDao.themPhieuDatPhong(phieuDatPhong)) {
                    for (PhongDat ct : dsPhongDat) {
                        ct.setPhieuDatPhong(phieuDatPhong);
                        chiTietPhongDao.themPhongDat(ct);
                    }
                    // Hiển thị thông báo thành công
                    showDialog(stackPane, "Thành công", "Đặt phòng thành công!", "OK");
                } else {
                    showDialog(stackPane, "Lỗi", "Đặt phòng không thành công!", "OK");
                }
            }
        });
        
        tf_tienKhachDua.setOnAction(e -> {
        	double tongTienCoc = 0;
    		try {
    			tongTienCoc = currencyFormat.parse(tf_tienCoc.getText()).doubleValue();
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        	double tienKhachDua = Double.parseDouble(tf_tienKhachDua.getText());
        	if(tienKhachDua < tongTienCoc) {
        		showDialog(stackPane, "Thông báo", "Số tiền khách trả phải lớn hơn tổng tiền cọc", "OK");
        	}else {
        		tf_tienThoi.setText(currencyFormat.format(tienKhachDua - tongTienCoc));
        	}
        });
    }
	
    //Lấy ngày nhận phòng ngày thuê phòng từ ThuePhong_Controller
    public void setNgayNhanTraPhong(String ngayNhanPhong, String ngayTraPhong) {
        tf_ngayNhanPhong.setText(ngayNhanPhong);
        tf_ngayTraPhong.setText(ngayTraPhong);
    }

    private void tinhTongTienVaTienCoc() {
    	//Định dạng tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        double tongTien = 0;
        double tongTienCoc = 0;
        for (Phong p : TB_phongDat.getItems()) {
            double thanhTien = col_thanhTien.getCellObservableValue(p).getValue();
            tongTien += thanhTien;

            double tienCoc = col_tienCoc.getCellObservableValue(p).getValue();
            tongTienCoc += tienCoc;
        }
        //Hiển thị trên txt tổng tiền và tiền cọc
        tf_tongTien.setText(currencyFormat.format(tongTien));
        tf_tienCoc.setText(currencyFormat.format(tongTienCoc));
        
        //Gợi ý tiền
		goiYTien(tongTienCoc);
    }

    private String taoMaPhieu() { 
        // ký tự tiếp theo là 2 chữ số cuối của năm và tháng ngày mà đặt phòng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dateString = LocalDate.now().format(formatter);
        //4 ký tự tiếp theo là 4 ký tự ngẫu nhiên tăng dần
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        return "PD" + dateString + randomNumber;
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
    
 // Hàm gợi ý số tiền
    public void goiYTien(double soTien) {
    	 NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    	 
        double soTienGoiY1 = Math.round(soTien / 1000) * 1000;
        btnGoiYTien1.setText(currencyFormat(soTienGoiY1));
        btnGoiYTien1.setOnAction(event -> {
            tf_tienKhachDua.setText(formatGoiYTien(soTienGoiY1));
            
            double tienCoc = 0;
            try {
   				tienCoc = currencyFormat.parse(tf_tienCoc.getText()).doubleValue();
   			} catch (ParseException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
            tf_tienThoi.setText(currencyFormat(Double.parseDouble(formatGoiYTien(soTienGoiY1)) - tienCoc));
        });
        double soTienGoiY2 = Math.floor(soTien / 100000) * 100000 + 100000;
        btnGoiYTien2.setText(currencyFormat(soTienGoiY2));
        btnGoiYTien2.setOnAction(event -> {
        	tf_tienKhachDua.setText(formatGoiYTien(soTienGoiY2));
        	
        	double tienCoc = 0;
            try {
   				tienCoc = currencyFormat.parse(tf_tienCoc.getText()).doubleValue();
   			} catch (ParseException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
            tf_tienThoi.setText(currencyFormat(Double.parseDouble(formatGoiYTien(soTienGoiY2)) - tienCoc));
        });
        double soTienGoiY3 = Math.floor(soTien / 500000) * 500000 + 500000;
        btnGoiYTien3.setText(currencyFormat(soTienGoiY3));
        btnGoiYTien3.setOnAction(event -> {
        	tf_tienKhachDua.setText(formatGoiYTien(soTienGoiY3));
        	
        	double tienCoc = 0;
            try {
   				tienCoc = currencyFormat.parse(tf_tienCoc.getText()).doubleValue();
   			} catch (ParseException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
            tf_tienThoi.setText(currencyFormat(Double.parseDouble(formatGoiYTien(soTienGoiY3)) - tienCoc));
        });
    }

    public String formatGoiYTien(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("####");
        String formattedAmount = decimalFormat.format(amount);
        return formattedAmount;
    }
    
    public static double currencyUnFormat(String currency) throws Exception {
        String numberString = currency.replace(",", "").replace("đ", "");
        double amount = 0;
        try {
        	amount = Double.parseDouble(numberString);
		} catch (Exception e) {
			throw new Exception();
		}
        
        return amount;
    }
    public static String currencyFormat(double amount) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0");
		String formattedAmount = decimalFormat.format(amount);
		String currency = formattedAmount + " đ";
		return currency;
	}
}

