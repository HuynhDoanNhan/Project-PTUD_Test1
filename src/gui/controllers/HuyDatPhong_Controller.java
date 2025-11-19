package gui.controllers;

import bus.KhachHang_Business;
import bus.QuanLyDichVu_Business;
import bus.QuanLyPhieuDat_Business;
import dao.PhieuDatPhong_DAO;
import dao.PhongDat_DAO;
import dao.Phong_DAO;
import dto.*;
import dto.enum_class.TrangThaiPhieuDat;
import dto.enum_class.TrangThaiPhongDat;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

public class HuyDatPhong_Controller implements Initializable {
	public TableColumn<PhieuDatPhong, String> maPhieuCol;
	public TableColumn<PhieuDatPhong, Date> ngayDatCol;
	public TableColumn<PhieuDatPhong, String> tongTienCol;
	public TableColumn<PhieuDatPhong, String> tongTienCocCol;
	public TableColumn<PhieuDatPhong, String> sdtCol;
	public TableColumn<PhieuDatPhong, String> hoTenCol;
	public TableColumn<PhieuDatPhong, String> trangThaiCol;
	public VBox VB_chiTietPhieuDatPhong;

	public TextField txtTenKH;
	public TextField txtCCCD;
	public TextField txtNgaySinh;
	public TextField txtSDT;

	@FXML
	private Button btnLamMoi;
	@FXML
	private Button btnTim;
	@FXML
	private TableView<PhieuDatPhong> table;
	@FXML
	private TextField txtTimKiem;
	@FXML
	private StackPane stackPane;

	private List<PhongDat> dsChiTietPhongDat = new ArrayList<>();
	private List<PhongDat> dsChiTietDuocChon = new ArrayList<>();
	private List<PhieuDatPhong> dsPhieuDat = new ArrayList<>();

	private QuanLyPhieuDat_Business quanLyPhieuDat_business = new QuanLyPhieuDat_Business();
	private KhachHang_Business quanLyKhachHang_business = new KhachHang_Business();
	private KhachHang khachHang = new KhachHang();
	private HuyDatPhong_Controller huyDatPhong_Controller;
	private PhieuDatPhong phieuDatPhong_selected;
	private CaLamViec caLamViec;
	private KhungGiaoDien_controller khungGiaoDienController;

	PhieuDatPhong_DAO daoPhieuDat = new PhieuDatPhong_DAO();
	PhongDat_DAO daoPhongDat = new PhongDat_DAO();
	Phong_DAO daoPhong = new Phong_DAO();
	private String cssFile = Objects.requireNonNull(getClass().getResource("/css/HuyDatPhong.css")).toExternalForm();

	public PhieuDatPhong getPhieuDatPhong_selected() {
		return phieuDatPhong_selected;
	}

	public void setCaLamViec(CaLamViec caLamViec) {
		this.caLamViec = caLamViec;
	}

	public void setKhungGiaoDien_controller(KhungGiaoDien_controller khungGiaoDienController) {
		this.khungGiaoDienController = khungGiaoDienController;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		// Thiết lập ánh xạ dữ liệu cho các cột cho table phiếu đặt
		maPhieuCol.setCellValueFactory(new PropertyValueFactory<>("maPhieu"));
		ngayDatCol.setCellValueFactory(new PropertyValueFactory<>("ngayDatPhong"));
		tongTienCocCol.setCellValueFactory(cellData -> {
			PhieuDatPhong phieuDatPhong = cellData.getValue();
			return new SimpleObjectProperty<>(currencyFormat.format(phieuDatPhong.getTienCoc()));
		});
		trangThaiCol.setCellValueFactory(cellData -> {
			PhieuDatPhong phieuDatPhong = cellData.getValue();
			String trangThai = phieuDatPhong.getTrangThaiPhieuDat().toString();
			String trangThaiHienThi = null;
			switch (trangThai) {
			case "DANHAN":
				trangThaiHienThi = "Đã nhận";
				break;
			case "DAHUY":
				trangThaiHienThi = "Đã hủy";
				break;
			case "CHONHAN":
				trangThaiHienThi = "Cho nhận";
				break;
			}
			return new SimpleObjectProperty<>(trangThaiHienThi);
		});

		table.setOnMouseClicked(mouseEvent -> {
			PhieuDatPhong phieuDatPhong = table.getSelectionModel().getSelectedItem();
			if (phieuDatPhong != null) {
				DocDuLieuDatabaseVaoTable();
				phieuDatPhong_selected = phieuDatPhong;
				updateDSChiTietPhongDat(phieuDatPhong);
				updataVBChiTietPhongDat();
				//kiemTraVaCapNhatTrangThaiPhieuDat(phieuDatPhong);
			}
		});

		btnTim.setOnAction(e -> {
			String keyWord = txtTimKiem.getText();

			khachHang = quanLyKhachHang_business.timKhachHangNhiu("", keyWord, "");

			if (khachHang != null) {
				txtTenKH.setText(khachHang.getTenKhachHang());
				txtCCCD.setText(khachHang.getCCCD());
				txtSDT.setText(khachHang.getSoDienThoai());
				txtNgaySinh.setText(khachHang.getNgaySinh().toString());
				updateDSPhieuDat();
				// Đọc dữ liệu từ database vào TableView
				DocDuLieuDatabaseVaoTable();
				dsChiTietDuocChon.clear();
				updataVBChiTietPhongDat();
			} else {
				txtTenKH.setText("");
				txtCCCD.setText("");
				txtSDT.setText("");
				txtNgaySinh.setText("");
			}
		});
		btnLamMoi.setOnAction(actionEvent -> {
			reloadTrang();
		});

	}

	public void updateDSPhieuDat() {
		dsPhieuDat = quanLyPhieuDat_business.getPhieuDatPhongTheoKhachHang(khachHang);
	}

	public void updateDSChiTietPhongDat(PhieuDatPhong phieuDatPhong) {
		dsChiTietPhongDat = quanLyPhieuDat_business.getChiTietPhongDatTheoPhieuDat(phieuDatPhong);

		System.out.println(dsChiTietPhongDat);

		updataVBChiTietPhongDat();
	}

	public void updataVBChiTietPhongDat() {
		VB_chiTietPhieuDatPhong.getChildren().clear();
		if (dsChiTietPhongDat.size() > 0) {
			for (PhongDat phongDat : dsChiTietPhongDat) {
				VB_chiTietPhieuDatPhong.getChildren().add(taoChiTietPhongDat(phongDat));
			}
		}
	}

	public AnchorPane taoChiTietPhongDat(PhongDat phongDat) {
		AnchorPane pane = new AnchorPane();
		pane.setPrefSize(370, 100);
		pane.getStylesheets().add(cssFile);

		// Đặt kiểu giao diện tùy thuộc vào việc phòng này có được chọn không
		if (dsChiTietDuocChon.contains(phongDat)) {
			pane.getStyleClass().add("chiTietPhongDat_isChosed");
		} else {
			pane.getStyleClass().add("chiTietPhongDat");
		}

		// Phần chứa mã phòng
		AnchorPane anMaPhong = new AnchorPane();
		anMaPhong.setPrefSize(110, 100);
		// Kiểm tra trạng thái và áp dụng lớp CSS
		String trangThai = phongDat.getTrangThaiPhongDat().toString();
		switch (trangThai) {
		case "CHONHAN":
			anMaPhong.getStyleClass().add("rectangle");
			break;
		case "DAHUY":
			anMaPhong.getStyleClass().add("rectangle_dahuy");
			break;
		case "DANHAN":
			anMaPhong.getStyleClass().add("rectangle_danhan");
			break;
		}

		Label maPhong = new Label(phongDat.getPhong().getMaPhong());
		maPhong.setFont(new javafx.scene.text.Font("Tahoma Bold", 36));
		maPhong.setTextFill(Color.WHITE);

		// Ngày nhận phòng
		Label ngayNhanPhong = new Label(
				"Ngày Nhận phòng: " + phongDat.getPhieuDatPhong().getNgayNhanPhong().toString());
		ngayNhanPhong.setFont(new javafx.scene.text.Font("Tahoma", 14));
		ngayNhanPhong.setTextFill(Color.DARKSLATEGRAY);

		// Ngày trả phòng
		Label ngayTraPhong = new Label("Ngày Trả phòng: " + phongDat.getPhieuDatPhong().getNgayTraPhong().toString());
		ngayTraPhong.setFont(new javafx.scene.text.Font("Tahoma", 14));
		ngayTraPhong.setTextFill(Color.DARKSLATEGRAY);

		// Thêm các thành phần vào giao diện
		anMaPhong.getChildren().add(maPhong);
		pane.getChildren().addAll(anMaPhong, ngayNhanPhong, ngayTraPhong);

		// Đặt vị trí các thành phần
		AnchorPane.setLeftAnchor(anMaPhong, 0.0);
		AnchorPane.setTopAnchor(maPhong, 25.0);
		AnchorPane.setLeftAnchor(maPhong, 10.0);
		AnchorPane.setLeftAnchor(ngayNhanPhong, 140.0);
		AnchorPane.setTopAnchor(ngayNhanPhong, 20.0);
		AnchorPane.setLeftAnchor(ngayTraPhong, 140.0);
		AnchorPane.setTopAnchor(ngayTraPhong, 60.0);

		// Xử lý sự kiện khi người dùng nhấn vào chi tiết phòng đặt
		pane.setOnMouseClicked(event -> {
			if (phongDat.getTrangThaiPhongDat() == TrangThaiPhongDat.CHONHAN) {

				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/ChiTietHuyDatPhong.fxml"));
				Parent root;
				try {
					root = loader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				// Truyền dữ liệu vào ChiTietHuyDatPhong_Controller
				ChiTietHuyDatPhong_Controller controller = loader.getController();
				controller.setPhongDat(phongDat); // Truyền phongDat vào controller
				controller.setKhungGiaoDien_controller(khungGiaoDienController);
				controller.setParentController(this);

				// Hiển thị cửa sổ chi tiết
				Stage detailStage = new Stage();
				detailStage.setTitle("Chi Tiết Hủy Đặt Phòng");
				detailStage.setScene(new Scene(root));
				detailStage.initModality(Modality.APPLICATION_MODAL); // Chế độ modal
				detailStage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow()); // Đặt cửa sổ cha
				detailStage.showAndWait();

			}
		});
		return pane;
	}

	public void DocDuLieuDatabaseVaoTable() {
		ObservableList<PhieuDatPhong> list = FXCollections.observableArrayList(dsPhieuDat); 
		table.setItems(list);
		// sắp xếp cột ngày đặt
		table.getSortOrder().add(ngayDatCol);
		ngayDatCol.setSortType(TableColumn.SortType.DESCENDING);
	}

	public void kiemTraVaCapNhatTrangThaiPhieuDat(PhieuDatPhong phieuDatPhong) {
		// Kiểm tra tất cả các phòng trong phiếu đặt
		List<PhongDat> phongDatList = daoPhongDat.getListPhongDatTheoMaPD(phieuDatPhong.getMaPhieu());
		boolean tatCaPhongDaHuy = true;
		for (PhongDat phongDat : phongDatList) {
			if (phongDat.getTrangThaiPhongDat() != TrangThaiPhongDat.DAHUY) {
				tatCaPhongDaHuy = false;
				break;
			}
		}

		// Nếu tất cả các phòng đã hủy, cập nhật trạng thái phiếu đặt thành DAHUY
		if (tatCaPhongDaHuy) {
			phieuDatPhong.setTrangThaiPhieuDat(TrangThaiPhieuDat.DAHUY);
			daoPhieuDat.capNhatPhieuDat(phieuDatPhong.getMaPhieu());
		}
	}

	public void reloadTrang() {
		// Làm mới dữ liệu bảng PhieuDatPhong
		table.getItems().clear();
		VB_chiTietPhieuDatPhong.getChildren().clear();

		// Làm mới các trường thông tin khách hàng
		txtTenKH.setText("");
		txtCCCD.setText("");
		txtSDT.setText("");
		txtNgaySinh.setText("");
		// Cập nhật lại danh sách chi tiết phòng đặt
		dsChiTietDuocChon.clear();
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

}
