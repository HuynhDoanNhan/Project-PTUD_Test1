package gui.controllers;

import bus.QuanLyPhong_Bussiness;
import dto.LoaiGiuongSuDung;
import dto.LoaiPhong;
import dto.Phong;
import dto.enum_class.TrangThaiPhong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class ThemPhong_Controller implements Initializable {

	@FXML
	private TextField txtgiaphong;

	@FXML
	private ComboBox<String> comBoBoxLoaiPhong;
	@FXML
	private ComboBox<String> comBoboxloaigiuong;

	@FXML
	private TextField txtsonguoi;

	@FXML
	private TextField txtMaPhong;

	@FXML
	private ImageView imgphong;


	private QuanLyPhong_Controller phongController;
	private boolean phongMoiDuocThem = false;

	private double giaPhong = 1000000;
	public double heSogia = 1;
	private int songuoi = 0;
	private QuanLyPhong_Bussiness phongBusiness = new QuanLyPhong_Bussiness();
	private File selectedFile;
	private String destinationPath;

	public void setPhongController(QuanLyPhong_Controller phongController) {
		this.phongController = phongController;
	}

	public void setPhongBusiness(QuanLyPhong_Bussiness phongBusiness) {
		this.phongBusiness = phongBusiness;
	}

	public boolean isPhongMoiDuocThem() {
		return phongMoiDuocThem;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		comBoBoxLoaiPhong.getItems().clear();
			List<LoaiPhong> dsLoaiPhong = phongBusiness.ReadLoaiPhong();
			if (dsLoaiPhong != null && !dsLoaiPhong.isEmpty()) {
				for (LoaiPhong loaiPhong : dsLoaiPhong) {
					comBoBoxLoaiPhong.getItems().add(loaiPhong.getTenLoaiPhong());
				}
				if (!comBoBoxLoaiPhong.getItems().isEmpty()) {
					comBoBoxLoaiPhong.setValue(comBoBoxLoaiPhong.getItems().get(0));
				}
			} 
		 
		txtgiaphong.setEditable(false);
		txtsonguoi.setEditable(false);
		comBoBoxLoaiPhong.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			Map<String, Integer> soKhachToiDaTheoLoaiPhong = new HashMap<>();
			soKhachToiDaTheoLoaiPhong.put("Standard", 2);
			soKhachToiDaTheoLoaiPhong.put("Superior", 2);
			soKhachToiDaTheoLoaiPhong.put("Deluxe", 3);
			soKhachToiDaTheoLoaiPhong.put("Suite", 3);

			int soKhachToiDa = soKhachToiDaTheoLoaiPhong.get(newValue);
			txtsonguoi.setText(String.valueOf(soKhachToiDaTheoLoaiPhong.get(newValue)));
			songuoi = soKhachToiDa;

			Map<String, String> giaTien = new HashMap<>();
			giaTien.put("Standard", "200000");
			giaTien.put("Superior", "500000");
			giaTien.put("Deluxe", "1000000");
			giaTien.put("Suite", "2000000");

			giaPhong = Double.parseDouble(giaTien.get(newValue));

			updateGiaPhong(giaPhong, heSogia);
		});
		comBoboxloaigiuong.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				Double hesogia_x = null;

				switch (newValue) {
				case "SINGLE":
					hesogia_x = phongBusiness.getHesogia("Single");
					break;
				case "DOUBLE":
					hesogia_x = phongBusiness.getHesogia("Double");
					break;
				case "KING":
					hesogia_x = phongBusiness.getHesogia("King");
					break;
				case "QUEEN":
					hesogia_x = phongBusiness.getHesogia("Queen");
					break;
				default:
					System.out.println("Loại giường không hợp lệ: " + newValue);
					break;
				}

				if (hesogia_x != null) {
					heSogia = hesogia_x;
					updateGiaPhong(giaPhong, heSogia);
				}
			}
		});
		comBoboxloaigiuong.getItems().clear();

		List<LoaiGiuongSuDung> sdloaigiuong = phongBusiness.ReadLoaiGiuongSuDung();

		for (LoaiGiuongSuDung loaigiuong : sdloaigiuong) {
			comBoboxloaigiuong.getItems().add(loaigiuong.getMaLoaiGiuong());
		}
		if (!comBoboxloaigiuong.getItems().isEmpty()) {
			comBoboxloaigiuong.setValue(comBoboxloaigiuong.getItems().get(0));
		}

	}

	public void updateGiaPhong(double giaPhong, double heSogia) {
		String maphong = txtMaPhong.getText();
		Map<String, String> loaiPhongSuDung = new HashMap<>();
		loaiPhongSuDung.put("Deluxe", "DLX");
		loaiPhongSuDung.put("Standard", "STD");
		loaiPhongSuDung.put("Superior", "SUP");
		loaiPhongSuDung.put("Suite", "SUT");
		String maloaiphong = loaiPhongSuDung.get(comBoBoxLoaiPhong.getValue());
		TrangThaiPhong trangThaiPhong = TrangThaiPhong.valueOf("CONTRONG");
		LoaiPhong loaiPhong = new LoaiPhong(maloaiphong, giaPhong);
		LoaiGiuongSuDung loaiGiuongSuDung = new LoaiGiuongSuDung(comBoboxloaigiuong.getValue(), heSogia);
		Phong p = new Phong(maphong, loaiPhong, loaiGiuongSuDung, songuoi, trangThaiPhong);
		txtgiaphong.setText(p.getGiaPhong() + "");
	}

	@FXML
	public void handleXacNhan() {

		try {
			String maphong = txtMaPhong.getText();
			if (maphong == null || maphong.isEmpty() || !maphong.matches("P\\d{3}")) {
				JOptionPane.showMessageDialog(null, "Mã phòng không hợp lệ");
				return;
			}
			Map<String, String> loaiPhongSuDung = new HashMap<>();
			loaiPhongSuDung.put("Deluxe", "DLX");
			loaiPhongSuDung.put("Standard", "STD");
			loaiPhongSuDung.put("Superior", "SUP");
			loaiPhongSuDung.put("Suite", "SUT");
			String maloaiphong = loaiPhongSuDung.get(comBoBoxLoaiPhong.getValue());
			String maloaigiuong = comBoboxloaigiuong.getValue();
			int songuoi = txtsonguoi.getText().isEmpty() ? 0 : Integer.parseInt(txtsonguoi.getText());
			double giaPhong = txtgiaphong.getText().isEmpty() ? 0 : Double.parseDouble(txtgiaphong.getText());
			TrangThaiPhong trangThaiPhong = TrangThaiPhong.valueOf("CONTRONG");
			LoaiPhong loaiPhong = new LoaiPhong(maloaiphong, giaPhong);
			LoaiGiuongSuDung loaiGiuongSuDung = new LoaiGiuongSuDung(comBoboxloaigiuong.getValue(), heSogia);
			Phong p = new Phong(maphong, loaiPhong, loaiGiuongSuDung, songuoi, trangThaiPhong);
			phongController.themphong_Dao(p);
			phongMoiDuocThem = true;
			Stage stage = (Stage) txtMaPhong.getScene().getWindow();
			stage.close();

		} catch (NumberFormatException e) {
			System.out.println("Số phòng không hợp lệ. Vui lòng nhập lại.");
		}
	}

	@FXML
	void handleChonAnh(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg",
				"*.jpeg");
		fileChooser.getExtensionFilters().add(imageFilter);
		Stage stage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			String imagePath = selectedFile.toURI().toString();
			Image image = new Image(imagePath);
			imgphong.setImage(image);
			imgphong.setLayoutX(450.0);
			imgphong.setLayoutY(80.0);
			imgphong.setFitWidth(430);
			imgphong.setFitHeight(280);
			imgphong.setPreserveRatio(false);
			String userDir = System.getProperty("user.dir");
			String destinationPath = userDir + "/src/main/resources/image/hinhPhong/" + txtMaPhong.getText() + ".jpg";
			File destination = new File(destinationPath);
			try {
				Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Ảnh đã được sao chép thành công vào thư mục: " + destination.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Không có file nào được chọn.");
		}
	}

	@FXML
	void btnhuy(ActionEvent event) {
		Stage stage = (Stage) txtMaPhong.getScene().getWindow();
		stage.close();
	}
}
