package gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import dto.Phong;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.TextField;

import java.awt.Button;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class PhongDat_controller {

	@FXML
	private AnchorPane root;

	@FXML
	private AnchorPane an_loaiPhong;

	@FXML
	private JFXButton btn_chon;

	@FXML
	private ImageView imgView_trangThaiPhong;

	@FXML
	private Label lb_giaPhong;

	@FXML
	private Label lb_loaiGiuong;

	@FXML
	private Label lb_trangThai;

	@FXML
	private Label lb_loaiPhong;

	@FXML
	private Label lb_maPhong;

	@FXML
	private Label lb_soNguoi;

	private StackPane stackPane = new StackPane();

	private DatPhong_Controller datPhong_Controller;
	private Phong phong;
	private boolean isSelected = false;
	private Stage primaryStage;

	public void setThuePhong_controller(DatPhong_Controller datPhong_Controller) {
		this.datPhong_Controller = datPhong_Controller;
	}

	public void isSelected() {
		this.isSelected = !this.isSelected;
	}

	public AnchorPane getRoot() {
		return root;
	}

	public Phong getPhong() {
		return phong;
	}

	public void updateChoose(boolean isSelected) {
		if (isSelected) {
			root.setBackground(
					new Background(new BackgroundFill(Color.web("lightgreen"), new CornerRadii(10), Insets.EMPTY)));
			btn_chon.setVisible(false);
		} else {
			root.setBackground(
					new Background(new BackgroundFill(Color.web("#b8b8b8"), new CornerRadii(10), Insets.EMPTY)));
			btn_chon.setVisible(true);
		}
	}

	public PhongDat_controller() {
		// Kiểm tra nếu datPhong_Controller chưa được khởi tạo
		if (datPhong_Controller == null) {
			datPhong_Controller = new DatPhong_Controller();
		}
	}

	public PhongDat_controller(Phong phong) {
		this.phong = phong;
		createNode();
	}

	public PhongDat_controller(Phong phong, boolean isSelected) {
		this.phong = phong;
		this.isSelected = isSelected;

		createNode();
	}

	public PhongDat_controller(Phong phong, boolean isSelected, Stage primaryStage) {
		this.phong = phong;
		this.isSelected = isSelected;
		this.primaryStage = primaryStage;
		createNode();
	}

	// Ensure that datPhong_Controller is passed in the constructor
	public PhongDat_controller(Phong phong, DatPhong_Controller datPhong_Controller) {
		this.phong = phong;
		this.datPhong_Controller = datPhong_Controller; // Ensure the controller is set
		createNode();
	}

	public PhongDat_controller(Phong phong, boolean isSelected, DatPhong_Controller datPhong_Controller) {
		this.phong = phong;
		this.isSelected = isSelected;
		this.datPhong_Controller = datPhong_Controller; // Ensure the controller is set

		createNode();
	}

	public void createNode() {
		loadFXML();

		this.phong = phong;

		lb_maPhong.setText(phong.getMaPhong());

		lb_loaiPhong.setText(phong.getLoaiPhong().getMaLoaiPhong());
		switch (phong.getLoaiPhong().getMaLoaiPhong()) {
		case "STD": {
			an_loaiPhong.setStyle("-fx-background-color: #11DD03; -fx-background-radius: 5;");
			break;
		}
		case "SUP": {
			an_loaiPhong.setStyle("-fx-background-color: #FFA500; -fx-background-radius: 5;");
			break;
		}
		case "DLX": {
			an_loaiPhong.setStyle("-fx-background-color: #00A1FF; -fx-background-radius: 5;");
			break;
		}
		case "SUT": {
			an_loaiPhong.setStyle("-fx-background-color: #FF4500; -fx-background-radius: 5;");
			break;
		}
		}
		lb_loaiGiuong.setText("Loại giường: " + phong.getLoaiGiuongSuDung().getMaLoaiGiuong());
		lb_soNguoi.setText("Số người: " + phong.getSoNguoi());

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
		lb_giaPhong.setText(formatCurrency(giaPhong));

		switch (phong.getTrangThaiPhong().toString()) {
		case "CONTRONG": {
			lb_trangThai.setText("Còn trống");
			break;
		}
		case "DANGTHUE": {
			lb_trangThai.setText("Đang thuê");
			break;
		}
		case "DANGDON": {
			lb_trangThai.setText("Đang dọn");
			break;
		}
		case "DANGBAOTRI": {
			lb_trangThai.setText("Đang bảo trì");
			break;
		}
		case "NGUNGHOATDONG": {
			lb_trangThai.setText("Ngừng hoạt động");
			break;
		}
		}

		if (isSelected) {
			root.setBackground(
					new Background(new BackgroundFill(Color.web("lightgreen"), new CornerRadii(10), Insets.EMPTY)));
			btn_chon.setVisible(false);
		} else if (!phong.getTrangThaiPhong().toString().equalsIgnoreCase("CONTRONG")) {
			btn_chon.setVisible(false);
			root.setBackground(
					new Background(new BackgroundFill(Color.web("#4c4c50"), new CornerRadii(10), Insets.EMPTY)));
			lb_loaiGiuong.setStyle("-fx-text-fill: white;");
			lb_soNguoi.setStyle("-fx-text-fill: white;");
			lb_giaPhong.setStyle("-fx-text-fill: white;");
		} else {
			root.setBackground(
					new Background(new BackgroundFill(Color.web("#b8b8b8"), new CornerRadii(10), Insets.EMPTY)));
		}

		imgView_trangThaiPhong.setImage(new Image(
				getClass().getResource("/image/" + phong.getTrangThaiPhong().toString() + ".png").toExternalForm()));

		btn_chon.setOnAction(event -> {
			ImageView imageView = new ImageView(getClass().getResource("/image/check_icon.png").toExternalForm());
			imageView.setFitHeight(50);
			imageView.setFitWidth(50);

			boolean isSelected = datPhong_Controller.getDsPhongThue().contains(phong);
			if (isSelected) {
				// Ẩn hình ảnh (biểu tượng đã chọn) và loại bỏ phòng khỏi danh sách
				imageView.setVisible(false);
				datPhong_Controller.getDsPhongThue().remove(phong); // Loại bỏ phòng khỏi danh sách
				datPhong_Controller.capNhatBangThuePhong(datPhong_Controller.getDsPhongThue()); // Cập nhật bảng thuê phòng
			} else {
				datPhong_Controller.Taodialog("Nhập số lượng khách lưu trú", "Xác nhận", soKhach -> {
					if (soKhach > phong.getSoNguoi()) {
						datPhong_Controller.callDialog("Thông báo", "Số lượng khách vượt quá sức chứa phòng!", "OK");
					} else {
						// Hiển thị lại hình ảnh (biểu tượng chọn) và thêm phòng vào danh sách
						imageView.setVisible(true);
						if (!datPhong_Controller.getDsPhongThue().contains(phong)) {
							datPhong_Controller.getDsPhongThue().add(phong); // Thêm phòng vào danh sách
							datPhong_Controller.capNhatBangThuePhong(datPhong_Controller.getDsPhongThue()); // Cập nhật bảng thuê phòng
						}
					}
				});

			}
		});
	}

	public static String formatCurrency(double giaTien) {
		Locale locale = new Locale("vi", "VN"); // Định dạng Việt Nam
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
		String formattedAmount = currencyFormatter.format(giaTien);
		formattedAmount = formattedAmount.replace("₫", "VND").trim();

		return formattedAmount;
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/Phong_view.fxml")));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load ButtonPhong_DatDichVu_View.fxml");
		}
	}
}
