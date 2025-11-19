package gui.controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import dao.DichVu_DAO;
import dto.ChiTietDichVu;
import dto.DichVu;
import dto.enum_class.LoaiDichVu;
import dto.enum_class.TrangThaiDichVu;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class QuanLiDichVu_Controller implements Initializable {
	@FXML
	private Button btnCapNhat;

	@FXML
	private Button btnLamMoi;

	@FXML
	private Button btnThem;

	@FXML
	private Button btnTim;

	@FXML
	private TextField txtDonVi;

	@FXML
	private JFXComboBox<String> cboLoai;

	@FXML
	private JFXComboBox<String> cboTrangThai;

	@FXML
	private TableColumn<DichVu, String> donViCol;

	@FXML
	private TableColumn<DichVu, Double> giaCol;

	@FXML
	private TableColumn<DichVu, String> loaiDVCol;

	@FXML
	private TableColumn<DichVu, String> maDVCol;

	@FXML
	private TableColumn<DichVu, String> moTaCol;

	@FXML
	private TableColumn<DichVu, Integer> soLuongCol;

	@FXML
	private StackPane stackPane;

	@FXML
	private TableView<DichVu> table;

	@FXML
	private TableColumn<DichVu, String> tenDVCol;

	@FXML
	private TableColumn<DichVu, String> trangThaiCol;

	@FXML
	private TextField txtGia;

	@FXML
	private TextField txtMaDV;

	@FXML
	private TextArea txtMoTa;

	@FXML
	private TextField txtSoLuong;

	@FXML
	private TextField txtTenDV;

	@FXML
	private TextField txtTimKiem;

	@FXML
	public VBox VboxChiTietDV;

	private DichVu_DAO dichVuDAO = new DichVu_DAO();
	private ObservableList<DichVu> dichVuList;
	// List<DichVu> list;
	private List<DichVu> dsDichVu = new ArrayList<>();
	private List<ChiTietDichVu> dsChiTietDichVuDuocChon = new ArrayList<>();
	private List<ChiTietDichVu> dsChiTietDV = new ArrayList<>();
	public DichVu dichVu = new DichVu();
	private String cssFile = Objects.requireNonNull(getClass().getResource("/css/HuyDatPhong.css")).toExternalForm();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtMaDV.setDisable(true);

		// Khởi tạo giá trị cho ComboBox
		cboLoai.setItems(FXCollections.observableArrayList("Bán", "Cho Thuê"));
		cboTrangThai.setItems(FXCollections.observableArrayList("Hoạt Động", "Ngừng", "Bảo Trì"));

		maDVCol.setCellValueFactory(new PropertyValueFactory<>("maDichVu"));
		tenDVCol.setCellValueFactory(new PropertyValueFactory<>("tenDichVu"));
		donViCol.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
		loaiDVCol.setCellValueFactory(celldata -> {
			DichVu dichVu = (DichVu) celldata.getValue();
			String loai = null;
			switch (dichVu.getLoaiDichVu().toString()) {
			case "DICHVUBAN": {
				loai = "Bán";
				break;
			}
			case "DICHVUCHOTHUE": {
				loai = "Cho Thuê";
				break;
			}
			}
			return new SimpleObjectProperty<>(loai);
		});
		giaCol.setCellValueFactory(new PropertyValueFactory<>("donGia"));
		soLuongCol.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
		moTaCol.setCellValueFactory(new PropertyValueFactory<>("moTa"));
		trangThaiCol.setCellValueFactory(celldata -> {
			DichVu dichVu = (DichVu) celldata.getValue();
			String trangThai = null;
			switch (dichVu.getTrangThaiDichVu().toString()) {
			case "DANGHOATDONG": {
				trangThai = "Hoạt Động";
				break;
			}
			case "NGUNGCUNGCAP": {
				trangThai = "Ngừng";
				break;
			}
			case "BAOTRI": {
				trangThai = "Bảo Trì";
				break;
			}
			}
			return new SimpleObjectProperty<>(trangThai);
		});

		// Đọc dữ liệu vào table
		DocDuLieuDatabaseVaoTable();

		// sự kiện click vào 1 dòng trong table
		table.setOnMouseClicked(mouseEvent -> {
			DichVu chonDichVu = table.getSelectionModel().getSelectedItem();
			if (chonDichVu != null) {
				// Đổ dữ liệu vào các TextField
				txtMaDV.setText(chonDichVu.getMaDichVu());
				txtTenDV.setText(chonDichVu.getTenDichVu());
				txtDonVi.setText(chonDichVu.getDonViTinh());

				// Chuyển đổi giá trị loại dịch vụ
				String loai = null;
				switch (chonDichVu.getLoaiDichVu().toString()) {
				case "DICHVUBAN":
					loai = "Bán";
					break;
				case "DICHVUCHOTHUE":
					loai = "Cho Thuê";
					break;
				}
				cboLoai.setValue(loai); // Hiển thị loại dịch vụ trên ComboBox

				txtGia.setText(String.valueOf(chonDichVu.getDonGia()));
				txtSoLuong.setText(String.valueOf(chonDichVu.getSoLuong()));
				txtMoTa.setText(chonDichVu.getMoTa());

				// Chuyển đổi giá trị trạng thái dịch vụ
				String trangThai = null;
				switch (chonDichVu.getTrangThaiDichVu().toString()) {
				case "DANGHOATDONG":
					trangThai = "Hoạt Động";
					break;
				case "NGUNGCUNGCAP":
					trangThai = "Ngừng";
					break;
				case "BAOTRI":
					trangThai = "Bảo Trì";
					break;
				}
				cboTrangThai.setValue(trangThai); // Hiển thị trạng thái trên ComboBox

				// sự kiên click hiện VBox
				DocDuLieuDatabaseVaoTable();
				updateDSChiTietDichVu(chonDichVu);
				dsChiTietDichVuDuocChon.clear();
				updataVBChiTietDichVu();
			}
		});

		btnLamMoi.setOnAction(actionEvent -> {
			// Sự kiên làm mới
			DocDuLieuDatabaseVaoTable();
			txtTimKiem.clear();
			clearFields();
		});
		btnThem.setOnAction(action -> {
			try {
				String ten = txtTenDV.getText();

				DichVu dichVu = valid();
				dichVuDAO.create(dichVu);
				showDialog(stackPane, "Thông báo", "Thêm dịch vụ thành công", "OK");
				DocDuLieuDatabaseVaoTable();
			} catch (Exception e) {
				e.printStackTrace();
				showDialog(stackPane, "Lỗi", "Lỗi khi thêm dịch vụ", "OK");
			}
			clearFields();
		});

		btnCapNhat.setOnAction(actionEvent -> {
			// Kiểm tra khách hàng đã được chọn từ bảng chưa
			DichVu chonDV = table.getSelectionModel().getSelectedItem();

			if (chonDV != null) {
				// Lấy thông tin mới từ các trường TextField
				DichVu dichVuNew = valid();

				if (dichVuNew != null) { // Kiểm tra thông tin hợp lệ
					// Gán lại mã khách hàng để đảm bảo đúng thông tin khách hàng được cập nhật
					dichVuNew.setMaDichVu(chonDV.getMaDichVu());
					try {
						boolean capNhatThanhCong = dichVuDAO.update(dichVuNew);

						if (capNhatThanhCong) {
							showDialog(stackPane, "Thông báo", "Cập nhật dịch vụ thành công", "OK");
							DocDuLieuDatabaseVaoTable();
						} else {
							showDialog(stackPane, "Thông báo", "Cập nhật dịch vụ thất bại", "OK");
						}
					} catch (Exception e) {
						e.printStackTrace();
						showDialog(stackPane, "Lỗi", "Lỗi khi cập nhật dịch vụ", "OK");
					}
				}
			} else {
				showDialog(stackPane, "Lỗi", "Vui lòng chọn một dịch vụ từ bảng để cập nhật", "OK");
			}
			clearFields();
		});
		txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
			String keyWord = txtTimKiem.getText().trim();

			// Nếu ô tìm kiếm trống thì hiển thị lại toàn bộ danh sách dịch vụ
			if (keyWord.isEmpty()) {
				table.setItems(FXCollections.observableArrayList(dsDichVu));
			} else {
				ArrayList<DichVu> temp = dichVuDAO.timDichVuTheoTen(keyWord);
				// Chuyển đổi kết quả thành ObservableList
				ObservableList<DichVu> list = FXCollections.observableArrayList(temp);
				table.setItems(list);
			}
		});

	}

	public void clearFields() {
		// Xóa dữ liệu trong các TextField
		txtMaDV.clear();
		txtTenDV.clear();
		txtDonVi.clear();
		txtGia.clear();
		txtSoLuong.clear();
		// Xóa dữ liệu trong TextArea
		txtMoTa.clear();
	}

	public DichVu valid() {
		String tenDV = txtTenDV.getText();
		String donVi = txtDonVi.getText();
		String loaiDVStr = cboLoai.getValue();
		String trangThaiStr = cboTrangThai.getValue();
		double gia = Double.parseDouble(txtGia.getText());
		int soLuong = Integer.parseInt(txtSoLuong.getText());
		String moTa = txtMoTa.getText();

		// Kiểm tra các trường rỗng
		if (tenDV == null || tenDV.trim().isEmpty() || donVi == null || donVi.trim().isEmpty() || loaiDVStr == null
				|| loaiDVStr.trim().isEmpty() || trangThaiStr == null || trangThaiStr.trim().isEmpty() || gia == 0
				|| txtGia.getText().trim().isEmpty() || soLuong == 0 || txtSoLuong.getText().trim().isEmpty()) {
			showDialog(stackPane, "Lỗi", "Vui lòng nhập đầy đủ thông tin", "OK");
			return null;
		}
		// Kiểm tra tên dịch vụ
		if (tenDV.trim().split("\\s+").length < 1) {
			showDialog(stackPane, "Lỗi", "Tên dịch vụ phải có ít nhất 1 từ", "OK");
			return null;
		}

		// Kiểm tra giá tiền
		if (gia <= 0) {
			showDialog(stackPane, "Lỗi", "Giá dịch vụ phải lớn hơn 0", "OK");
			return null;
		}
		// Kiểm tra số lượng
		if (soLuong < 0) {
			showDialog(stackPane, "Lỗi", "Số lượng phải là số nguyên không âm", "OK");
			return null;
		}
		// Kiểm tra và chuyển đổi loaiDV và trangThaiStr từ String sang Enum
		LoaiDichVu loaiDV = null;
		switch (loaiDVStr) {
		case "Bán":
			loaiDV = LoaiDichVu.DICHVUBAN;
			break;
		case "Cho Thuê":
			loaiDV = LoaiDichVu.DICHVUCHOTHUE;
			break;
		}
		TrangThaiDichVu trangThai = null;
		switch (trangThaiStr) {
		case "Bảo Trì":
			trangThai = TrangThaiDichVu.BAOTRI;
			break;
		case "Hoạt Động":
			trangThai = TrangThaiDichVu.DANGHOATDONG;
			break;
		case "Ngừng":
			trangThai = TrangThaiDichVu.NGUNGCUNGCAP;
			break;
		}

		// Tạo đối tượng DichVu nếu tất cả đều hợp lệ
		DichVu dichVu = new DichVu(taoMaDichVu(tenDV), tenDV, moTa, donVi, gia, soLuong, loaiDV, trangThai);
		return dichVu;
	}

	public String taoMaDichVu(String tenDichVu) {
		if (tenDichVu == null || tenDichVu.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên dịch vụ không được để trống");
		}
		// Chuyển thành chữ hoa sau khi có cắt chuỗi
		String[] words = tenDichVu.trim().toUpperCase().split("\\s+");
		StringBuilder maDichVu = new StringBuilder("DV");

		for (String word : words) {
			if (!word.isEmpty()) {
				maDichVu.append(word.charAt(0));
			}
		}
		// Nếu k đủ 6 kí tự tự động thêm chữ X
		while (maDichVu.length() < 6) {
			maDichVu.append("X");
		}

		return maDichVu.substring(0, 6);
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

	// Hàm đoc từ Database
	public void DocDuLieuDatabaseVaoTable() {
		dsDichVu = dichVuDAO.getDSDichVu();
		dichVuList = FXCollections.observableArrayList(dsDichVu);
		table.setItems(dichVuList);
	}

	public void updateDSChiTietDichVu(DichVu dv) {
		dsChiTietDV.clear();
		dsChiTietDV = dichVuDAO.getChiTietDichVuTheoMa(dv);
	}

	public void updataVBChiTietDichVu() {
		VboxChiTietDV.getChildren().clear();
		if (dsChiTietDV.size() > 0) {
			for (ChiTietDichVu ctdv : dsChiTietDV) {
				VboxChiTietDV.getChildren().add(taoChiTietDichVu(ctdv));
			}
		}
	}

	public AnchorPane taoChiTietDichVu(ChiTietDichVu chiTietDichVu) {
		AnchorPane pane = new AnchorPane();
		VboxChiTietDV.setSpacing(10);
		pane.setPrefSize(370, 100);
		pane.getStylesheets().add(cssFile);

		// Đặt kiểu giao diện tùy thuộc vào việc phòng này có được chọn không
		if (dsChiTietDichVuDuocChon.contains(chiTietDichVu)) {
			pane.getStyleClass().add("chiTietDichVuDat");
		} else {
			pane.getStyleClass().add("chiTietDichVuDat");
		}
		// Phần chứa mã phòng
		AnchorPane anMaPhong = new AnchorPane();
		anMaPhong.setPrefSize(110, 100);
		anMaPhong.getStyleClass().add("rectangle");

		Label maPhong = new Label(chiTietDichVu.getPhong().getMaPhong());
		maPhong.setFont(new javafx.scene.text.Font("Tahoma Bold", 36));
		maPhong.setTextFill(Color.WHITE);

		Label soLuong = new Label(String.valueOf("Số lượng: " +chiTietDichVu.getSoLuong()));
		soLuong.setFont(new javafx.scene.text.Font("Tahoma", 14));
		soLuong.setTextFill(Color.DARKSLATEGRAY);

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		double gia = chiTietDichVu.getSoLuong() * chiTietDichVu.getDonGia();
		Label donGia = new Label(String.valueOf("Giá: " + currencyFormat.format(gia)));
		donGia.setFont(new javafx.scene.text.Font("Tahoma", 14));
		donGia.setTextFill(Color.DARKSLATEGRAY);


		ImageView imageView = new ImageView(getClass().getResource("/image/hinhDichVu/" +chiTietDichVu.getDichVu().getMaDichVu() + ".jpg").toExternalForm());
		imageView.setFitWidth(120);
		imageView.setFitHeight(100);
	 	imageView.getStyleClass().add("img");
		// Thêm các thành phần vào giao diện
		anMaPhong.getChildren().add(maPhong);
		pane.getChildren().addAll(anMaPhong, soLuong, donGia, imageView);

		// Đặt vị trí các thành phần
		AnchorPane.setLeftAnchor(anMaPhong, 0.0);
		AnchorPane.setTopAnchor(maPhong, 25.0);
		AnchorPane.setLeftAnchor(maPhong, 10.0);
		AnchorPane.setLeftAnchor(soLuong, 140.0);
		AnchorPane.setTopAnchor(soLuong, 20.0);
		AnchorPane.setLeftAnchor(donGia, 140.0);
		AnchorPane.setTopAnchor(donGia, 60.0);
		AnchorPane.setLeftAnchor(imageView, 470.0);

		return pane;
	}
}
