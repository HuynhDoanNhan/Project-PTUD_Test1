package gui.controllers;

import bus.QuanLyPhong_Bussiness;
import connectDB.ConnectDB;
import dto.LoaiPhong;
import dto.Phong;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

import javax.swing.JOptionPane;

public class QuanLyPhong_Controller implements Initializable {

	@FXML
	private VBox dsPhong;

	@FXML
	private ScrollPane scrollPanePhong;
	private QuanLyPhong_Bussiness phongBusiness = new QuanLyPhong_Bussiness();

	@FXML
	private Button btncapnhap;
	@FXML
	private Label soLuongPhong;

	@FXML
	private Label soluongphongdondep;

	@FXML
	private Label soluongphongduocthue;
    @FXML
    private Label soluongphongbaotri;

	@FXML
	private Label soluongphongtrong;
	private List<Phong> danhSachPhong = new ArrayList<>();

	@FXML
	private ComboBox<String> LocThongTin;
	@FXML
	private Button btnTimKiem;
	@FXML
	private TextField txttimkiem;
	@FXML
	private ComboBox<String> comboboxloaiphong;
    @FXML
    private HBox PhongTheoTang;
    private String maphong_x=null;

    @FXML
    private Label sophongngunghd;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			System.out.println("Kết nối thành công");
		} catch (Exception e) {
			e.printStackTrace();
		}

		QuanLyPhong_Bussiness.updateTrangThaiPhongTheoNgay();

		phongBusiness = new QuanLyPhong_Bussiness();
		danhsachphongsize();
		dsPhong.setSpacing(20); 
		phongBusiness = new QuanLyPhong_Bussiness(dsPhong);
		updateDSPhong();
		scrollPanePhong.setContent(dsPhong);
		scrollPanePhong.setFitToWidth(true);
		btnTimKiem.setOnAction(event -> {
			timphongtheomaphong();
		});
		btncapnhap.setOnAction(event ->{
			dsPhong.getChildren().clear(); 
			phongBusiness.resetDemTrangThai();
			comboboxloaiphong.getItems().clear();
			LocThongTin.getItems().clear();
			updateDSPhong();
		});
	}
	public void updateDSPhong() {
	    danhSachPhong = phongBusiness.docdulieuDatabase();
	    dsPhong.getChildren().clear(); 
	    Map<Integer, FlowPane> tangToPaneMap = new HashMap<>();
	    for (Phong phong : danhSachPhong) {
	    	phongBusiness.kiemtratrangthai(phong.getTrangThaiPhong().name());
	    	maphong_x=phong.getMaPhong();
	        String tang = phongBusiness.tinhtang(phong.getMaPhong());
	        int tangInt = Integer.parseInt(tang);
	        FlowPane tangPane = tangToPaneMap.get(tangInt);
	        if (tangPane == null) {
	            tangPane = taoTang(tangInt); 
	            tangToPaneMap.put(tangInt, tangPane);
	        }
	        PhongQL_Controller phongQLController = new PhongQL_Controller(phong);
	        AnchorPane phongNode = phongQLController.getRoot();
	        tangPane.getChildren().add(phongNode);
	    }
	    tangToPaneMap.keySet().stream().sorted().forEachOrdered(tang -> {
	        VBox floorBox = taoVBoxTang(tang, tangToPaneMap.get(tang));
	        dsPhong.getChildren().add(floorBox);
	    });
	    soluongphongtrong.setText(phongBusiness.getPhongtrong()+"");
	    soluongphongduocthue.setText(phongBusiness.getPhongdangthue()+"");
	    soluongphongdondep.setText(phongBusiness.getPhongdangdondep()+"");
	    soluongphongbaotri.setText(phongBusiness.getPhongdangbaotri()+"");
	    sophongngunghd.setText(phongBusiness.getPhongngunghoatdong()+"");
	    soLuongPhong.setText(phongBusiness.getSoPhong()+"");
	    ArrayList<Phong> listtrangthaiphong = phongBusiness.trangthaiphong();
	    Map<String, String> trangThaiMap = new HashMap<>();
	    trangThaiMap.put("CONTRONG", "Phòng trống");
	    trangThaiMap.put("DANGDON", "Phòng đang dọn");
	    trangThaiMap.put("DANGTHUE", "Phòng được thuê");
	    trangThaiMap.put("DANGBAOTRI", "Phòng đang bảo trì");
	    trangThaiMap.put("NGUNGHOATDONG", "Ngừng hoạt động");

	    LocThongTin.getItems().add("Tất cả trạng thái phòng");
	    for (Phong trangthai : listtrangthaiphong) {
	        String tentrangthai = trangThaiMap.get(trangthai.getTrangThaiPhong().toString());
	        if (tentrangthai != null) {
	            LocThongTin.getItems().add(tentrangthai);
	        }
	    }
	    LocThongTin.getSelectionModel().selectFirst();

	    ArrayList<LoaiPhong> listloaiphong = phongBusiness.loaiphong_bus();
	    Map<String, String> loaiPhongMap = new HashMap<>();
	    loaiPhongMap.put("DLX", "Deluxe");
	    loaiPhongMap.put("STD", "Standard");
	    loaiPhongMap.put("SUP", "Superior");
	    loaiPhongMap.put("SUT", "Suite");
	    comboboxloaiphong.getItems().add("Tất cả loại phòng");
	    for (LoaiPhong loaiPhong : listloaiphong) {
	        String tenLoaiPhong = loaiPhongMap.get(loaiPhong.getMaLoaiPhong());
	        if (tenLoaiPhong != null) {
	            comboboxloaiphong.getItems().add(tenLoaiPhong);
	        }
	    }
	    comboboxloaiphong.getSelectionModel().selectFirst();
	    Map<String, String> trangThaiReverseMap = new HashMap<>();
	    trangThaiReverseMap.put("Tất cả trạng thái phòng", "Tất cả trạng thái phòng");
	    trangThaiReverseMap.put("Phòng trống", "CONTRONG");
	    trangThaiReverseMap.put("Phòng đang dọn", "DANGDON");
	    trangThaiReverseMap.put("Phòng được thuê", "DANGTHUE");
	    trangThaiReverseMap.put("Phòng đang bảo trì", "DANGBAOTRI");
	    trangThaiReverseMap.put("Ngừng hoạt động", "NGUNGHOATDONG");

	    Map<String, String> loaiPhongReverseMap = new HashMap<>();
	    loaiPhongReverseMap.put("Tất cả loại phòng", "Tất cả loại phòng");
	    loaiPhongReverseMap.put("Deluxe", "DLX");
	    loaiPhongReverseMap.put("Standard", "STD");
	    loaiPhongReverseMap.put("Superior", "SUP");
	    loaiPhongReverseMap.put("Suite", "SUT");

	    LocThongTin.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
	        if (newValue != null) {
	            String currentLoaiPhong = comboboxloaiphong.getSelectionModel().getSelectedItem();
	            locPhongTheoDieuKien(
	                trangThaiReverseMap.get(newValue), 
	                loaiPhongReverseMap.getOrDefault(currentLoaiPhong, "Tất cả loại phòng")
	            );
	        }
	    });

	    comboboxloaiphong.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
	        if (newValue != null) {
	            String currentTrangThai = LocThongTin.getSelectionModel().getSelectedItem();
	            locPhongTheoDieuKien(
	                trangThaiReverseMap.getOrDefault(currentTrangThai, "Tất cả trạng thái phòng"), 
	                loaiPhongReverseMap.get(newValue)
	            );
	        }
	    });

	}

	private FlowPane taoTang(int tang) {
	    FlowPane tangPane = new FlowPane();
	    tangPane.setPrefWidth(854.0);
	    tangPane.setHgap(20);
	    tangPane.setVgap(30);
	    tangPane.setPrefWrapLength(500);
	    return tangPane;
	}
	private VBox taoVBoxTang(int tang, FlowPane tangPane) {
	    Label tangLabel = new Label("Tầng " + tang);
	    tangLabel.setPrefHeight(50.0);
	    tangLabel.setPrefWidth(77.0);
	    tangLabel.setStyle("-fx-font-size: 14;");

	    Label sophongthuoctangLabel = new Label("(" + phongBusiness.soluongphongtungtang(tang) + " Phòng)");
	    sophongthuoctangLabel.setPrefHeight(30.0);
	    sophongthuoctangLabel.setPrefWidth(77.0);
	    sophongthuoctangLabel.setStyle("-fx-font-size: 14;");

	    HBox tangInfoBox = new HBox(-30);
	    tangInfoBox.getChildren().addAll(tangLabel, sophongthuoctangLabel);
	    tangInfoBox.setAlignment(Pos.CENTER_LEFT);

	    VBox floorBox = new VBox();
	    floorBox.getChildren().addAll(tangInfoBox, tangPane);
	    floorBox.setSpacing(10);

	    return floorBox;
	}
	public void danhsachphongsize() {
		soLuongPhong.setText(String.valueOf(danhSachPhong.size()));
	}
	public void themphong_Dao(Phong p) {
		for (Phong existingPhong : danhSachPhong) {
			if (existingPhong.getMaPhong().equals(p.getMaPhong())) {
				System.out.println("Phòng đã tồn tại: " + p.getMaPhong());
				JOptionPane.showMessageDialog(null, "Phòng đã tồn tại hãy thêm 1 phòng khác");
				return;
			}
		}
		phongBusiness.themPhong(p);
		dsPhong.getChildren().clear(); 
		phongBusiness.resetDemTrangThai();
		comboboxloaiphong.getItems().clear();
		LocThongTin.getItems().clear();
		updateDSPhong();
	}
	
	

	@FXML
	public void handleThemPhong() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ThemPhong_view.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(loader.load()));
			stage.setTitle("Thêm Phòng");

			ThemPhong_Controller themPhongController = loader.getController();
			themPhongController.setPhongController(this); 
			themPhongController.setPhongBusiness(phongBusiness);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void timphongtheomaphong() {
	    String maphong_x = txttimkiem.getText().trim();
	    dsPhong.getChildren().clear();
	    boolean found = false;
	    for (Phong phong : danhSachPhong) {
	        if (phong.getMaPhong().equalsIgnoreCase(maphong_x)) {
	            PhongQL_Controller phongQLController = new PhongQL_Controller(phong);
	            AnchorPane phongNode = phongQLController.getRoot();
	            dsPhong.getChildren().add(phongNode); 
	            found = true;
	            break;
	        }
	    }
	    if (!found) {
	        Label notFoundLabel = new Label("Không tìm thấy phòng với mã: " + maphong_x);
	        notFoundLabel.setStyle("-fx-font-size: 16; -fx-text-fill: red;");
	        dsPhong.getChildren().add(notFoundLabel);
	    }
	}

	private void locPhongTheoDieuKien(String trangThai_input, String loaiphong) {
	    dsPhong.getChildren().clear(); // Xóa giao diện hiện tại
	    Map<Integer, FlowPane> tangToPaneMap = new HashMap<>(); // Bản đồ tầng - FlowPane

	    for (Phong phong : danhSachPhong) {
	        boolean isMatchTrangThai = trangThai_input.equalsIgnoreCase("Tất cả trạng thái phòng") || 
	                                   phong.getTrangThaiPhong().name().equalsIgnoreCase(trangThai_input);

	        boolean isMatchLoaiPhong = loaiphong.equalsIgnoreCase("Tất cả loại phòng") || 
	                                   phong.getLoaiPhong().getMaLoaiPhong().equalsIgnoreCase(loaiphong);

	        // Chỉ thêm phòng nếu thỏa cả hai điều kiện
	        if (isMatchTrangThai && isMatchLoaiPhong) {
	            String tang = phongBusiness.tinhtang(phong.getMaPhong());
	            int tangInt = Integer.parseInt(tang);

	            FlowPane tangPane = tangToPaneMap.get(tangInt);
	            if (tangPane == null) { // Nếu tầng chưa tồn tại, tạo mới
	                tangPane = taoTang(tangInt);
	                tangToPaneMap.put(tangInt, tangPane);
	            }

	            // Thêm phòng vào tầng
	            PhongQL_Controller phongQLController = new PhongQL_Controller(phong);
	            AnchorPane phongNode = phongQLController.getRoot();
	            tangPane.getChildren().add(phongNode);
	        }
	    }

	    // Hiển thị các tầng theo thứ tự
	    tangToPaneMap.keySet().stream().sorted().forEachOrdered(tang -> {
	        VBox floorBox = taoVBoxTang(tang, tangToPaneMap.get(tang));
	        dsPhong.getChildren().add(floorBox);
	    });
	}

}