package gui.controllers;

import dao.Phong_DAO;
import dto.*;
import dto.enum_class.TrangThaiPhong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import javax.swing.*;

import com.jfoenix.controls.JFXButton;

import bus.QuanLyPhong_Bussiness;
import utils.NumberFormat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ChuyenPhong_Controller implements Initializable {
	@FXML
	private VBox dsPhong;
	@FXML
	private Button btntimkiem;
	@FXML
	private ImageView imgPhong;

	@FXML
	private ScrollPane scrollPanePhong;
	private int maPhongINT;
	@FXML
	private TextField txttimkiem;
	@FXML
	private Button btncapnhap;
	private List<Phong> danhSachPhong = new ArrayList<>();
	private ArrayList<Phong> sortedList = new ArrayList<>();
	private QuanLyPhong_Bussiness phongBusiness;
	@FXML
	private Label txt_NgayNhanPhong;

	@FXML
	private Label txt_Ngaytraphong;

	@FXML
	private Label txt_soPhong;

	@FXML
	private Label txt_sodienthoai;

	@FXML
	private Label txt_tenkhachhang;
	@FXML
	private Label khachluutru;

	@FXML
	private Label txt_thanhtien;
	private Phong_DAO phong_dao = new Phong_DAO();
	private String mahoadon;
	private int songuoi;
	private double dongia;

	@FXML
	private Label phichuyenphong;
	private String maloaigiuong;
	private String maloaiPhong;
	private String tenLoaiPhong;
	private Map<JFXButton, Label> buttonToLoaiPhong_2Label = new HashMap<>();
	private Map<JFXButton, Label> buttongiaphongPhongLabel = new HashMap<>();
	private Map<JFXButton, Label> buttonmaphong_xLabel = new HashMap<>();
	private ArrayList<JFXButton> dsPhongBT_2 = new ArrayList<JFXButton>();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		phongBusiness = new QuanLyPhong_Bussiness();
		capnhapthongtinPhong();
		dsPhong.setSpacing(20);
		phongBusiness = new QuanLyPhong_Bussiness(dsPhong);
		scrollPanePhong.setContent(dsPhong);
		scrollPanePhong.setFitToWidth(true);
	}

	private void capnhapthongtinPhong() {
		dsPhong.getChildren().clear();
		danhSachPhong = phongBusiness.docdulieuDatabase();
		for (Phong phong : danhSachPhong) {
			String maPhong = phong.getMaPhong().toString();
			String maLoaiPhong = phong.getLoaiPhong().getMaLoaiPhong().toString();
			String maLoaiGiuong = phong.getLoaiGiuongSuDung().getMaLoaiGiuong().toString();
			String trangThai = phong.getTrangThaiPhong().toString();
			int soNguoi = phong.getSoNguoi();
			if (trangThai.equalsIgnoreCase("DANGTHUE")) {
				LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(maLoaiGiuong, phong_dao.getHesgia(maLoaiGiuong));
				LoaiPhong lp = new LoaiPhong(maLoaiPhong, phong_dao.getgiacoban(maLoaiPhong));
				TrangThaiPhong trangthai = TrangThaiPhong.valueOf(trangThai);
				Phong p = new Phong(maPhong, lp, lgsd, soNguoi, trangthai);
				themPhongVaoGiaoDien(p);
			}
		}
	}

	public void themPhongVaoGiaoDien(Phong p) {
		phongBusiness = new QuanLyPhong_Bussiness();
		String tang = phongBusiness.tinhtang(p.getMaPhong());
		int tangInt = Integer.valueOf(tang);
		FlowPane tangPane = timHoacTaoTang(tangInt);

		JFXButton phongButton = new JFXButton();
		phongButton.setPrefHeight(200.0);
		phongButton.setPrefWidth(540.0);
		phongButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff;");
		FlowPane.setMargin(phongButton, new Insets(0, 0, 0, 20));
		ImageView img = new ImageView(getClass().getResource("/image/hinhPhong/" +p.getMaPhong() + ".jpg").toExternalForm());
			img.setLayoutX(10.0);
			img.setLayoutY(20.0);
			img.setFitWidth(170);
			img.setFitHeight(150);
			img.setPreserveRatio(false);
			img.getStyleClass().add("img");
		Pane pane = new Pane();
		pane.setPrefHeight(140.0);
		pane.setPrefWidth(540.0);

		Label giatienLabel = new Label("Giá phòng " + NumberFormat.currencyFormat(p.getGiaPhong()));
		giatienLabel.setLayoutX(230);
		giatienLabel.setLayoutY(70);
		giatienLabel.setPrefHeight(3.0);
		giatienLabel.setPrefWidth(200);

		Label maphong_x = new Label(p.getMaPhong());
		maphong_x.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		maphong_x.setLayoutX(200.0);
		maphong_x.setLayoutY(4.0);
		maphong_x.setPrefHeight(30.0);
		maphong_x.setPrefWidth(77.0);
		Map<String, String> loaiPhongKS = new HashMap<>();
		loaiPhongKS.put("DLX", "Deluxe");
		loaiPhongKS.put("STD", "Standard");
		loaiPhongKS.put("SUP", "Superior");
		loaiPhongKS.put("SUT", "Suite");
		tenLoaiPhong = loaiPhongKS.get(p.getLoaiPhong().getMaLoaiPhong());
		Label loaiphong_x = new Label("Phòng " + tenLoaiPhong + " giường " + p.getLoaiGiuongSuDung().getMaLoaiGiuong()
				+ " cho " + p.getSoNguoi() + " người");
		loaiphong_x.setLayoutX(200.0);
		loaiphong_x.setLayoutY(40);
		loaiphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

		pane.getChildren().addAll(loaiphong_x, giatienLabel, img);
		pane.getChildren().add(maphong_x);
		phongButton.setGraphic(pane);

		tangPane.getChildren().add(phongButton);
		dsPhongBT_2.add(phongButton);
		buttonToLoaiPhong_2Label.put(phongButton, loaiphong_x);
		buttongiaphongPhongLabel.put(phongButton, giatienLabel);
		buttonmaphong_xLabel.put(phongButton, maphong_x);
		phongButton.setOnAction(event -> {
			for (JFXButton bt : dsPhongBT_2) {
				Label loaiPhongLabel_x = buttonToLoaiPhong_2Label.get(bt);
				Label giaPhong_x = buttongiaphongPhongLabel.get(bt);
				Label maphong_x1 = buttonmaphong_xLabel.get(bt);
				bt.setStyle("-fx-background-color: #ffffff;");
				maphong_x1.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
				loaiPhongLabel_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
				giaPhong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
			}
			phongButton.setStyle("-fx-background-color: #1f7845;");
			maphong_x.setStyle("-fx-font-size: 20; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
			loaiphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;-fx-text-fill: #FFFFFF;");
			giatienLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;-fx-text-fill: #FFFFFF;");
			hienThiThongTinKhachHang(p);
			txttimkiem.setText(p.getMaPhong());
			hienanh(p.getMaPhong());
		});
		btntimkiem.setOnAction(event -> {
			if(!txttimkiem.getText().trim().isEmpty()) {
				timphongtheomaphong();
				txt_NgayNhanPhong.setText("");
				txt_Ngaytraphong.setText("");
				txt_thanhtien.setText("");
				khachluutru.setText("");
				txt_tenkhachhang.setText("");
				txt_soPhong.setText("");
				txt_sodienthoai.setText("");
				hienanh(null);
			}else {
			      JOptionPane.showMessageDialog(null, "Nhập số phòng cần tìm", "Thông báo", JOptionPane.WARNING_MESSAGE);
			}
		});
		btncapnhap.setOnAction(event -> {
			capnhapthongtinPhong();
		});
	}

	private FlowPane timHoacTaoTang(int tang) {
		FlowPane tangPane = null;
		for (Node node : dsPhong.getChildren()) {
			if (node instanceof VBox) {
				VBox currentBox = (VBox) node;
				HBox tangInfoBox = (HBox) currentBox.getChildren().get(0);
				Label tangLabel = (Label) tangInfoBox.getChildren().get(0);
				if (tangLabel.getText().equals("Tầng " + tang)) {
					for (Node child : currentBox.getChildren()) {
						if (child instanceof FlowPane) {
							tangPane = (FlowPane) child;
							break;
						}
					}
				}
			}
		}
		if (tangPane == null) {
			tangPane = new FlowPane();
			tangPane.setPrefWidth(854.0);
			tangPane.setHgap(10);
			tangPane.setVgap(10);
			tangPane.setPrefWrapLength(200);

			Label tangLabel = new Label("Tầng " + tang);
			tangLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
			tangLabel.setPrefHeight(30.0);
			tangLabel.setPrefWidth(80.0);

			HBox tangInfoBox = new HBox(-30);
			tangInfoBox.getChildren().addAll(tangLabel);
			tangInfoBox.setAlignment(Pos.CENTER_LEFT);

			VBox floorBox = new VBox();
			floorBox.getChildren().addAll(tangInfoBox, tangPane);
			floorBox.setSpacing(10);
			dsPhong.getChildren().add(floorBox);
		}
		return tangPane;
	}

	private void hienanh(String maphong_y) {
		ImageView imageView = new ImageView(getClass().getResource("/image/hinhPhong/" +maphong_y + ".jpg").toExternalForm());
		imageView.setLayoutX(10.0);
		imageView.setLayoutY(20.0);
		imageView.setFitWidth(170); // Set width
		imageView.setFitHeight(150); // Set height
		imageView.setPreserveRatio(false); // Allow non-proportional resizing
		imageView.getStyleClass().add("img");
		imgPhong.setImage(imageView.getImage());
		imgPhong.setPreserveRatio(false);

	}

	private void hienThiThongTinKhachHang(Phong p_x) {
		Map<String, String> thongTinKhachHang = phong_dao.layThongTinKhachHangTheoMaPhong(p_x.getMaPhong());
		txt_tenkhachhang.setText(thongTinKhachHang.get("hoTen"));
		txt_soPhong.setText(thongTinKhachHang.get("maPhong"));
		txt_sodienthoai.setText(thongTinKhachHang.get("soDienThoai"));

		mahoadon = thongTinKhachHang.get("maHoaDon");

		String ngayBatDauFormatted = dinhDangNgay(thongTinKhachHang.get("ngayBatDauO"));
		String ngayKetThucFormatted = dinhDangNgay(thongTinKhachHang.get("ngayKetThucO"));
		txt_NgayNhanPhong.setText(ngayBatDauFormatted);
		txt_Ngaytraphong.setText(ngayKetThucFormatted);

		songuoi = Integer.valueOf(thongTinKhachHang.get("soNguoi"));
		dongia = Double.valueOf(thongTinKhachHang.get("donGia"));

		LocalDateTime ngayBatDau = convertStringToLocalDateTime(thongTinKhachHang.get("ngayBatDauO"));
		LocalDateTime ngayKetThuc = convertStringToLocalDateTime(thongTinKhachHang.get("ngayKetThucO"));

		Phong p = new Phong(p_x.getMaPhong());
		HoaDon hd = new HoaDon(mahoadon);
		PhongThue pt = new PhongThue(hd, p, songuoi, ngayBatDau, ngayKetThuc, dongia);
		txt_thanhtien.setText(String.valueOf(NumberFormat.currencyFormat(pt.getThanhTien())));
		maloaigiuong = p_x.getLoaiGiuongSuDung().getMaLoaiGiuong();
		maloaiPhong = p_x.getLoaiPhong().getMaLoaiPhong();

		String kh = "";

		List<ChiTietPhongThue> danhsachkh=phongBusiness.getKhachHang(mahoadon, p_x.getMaPhong());
		for (ChiTietPhongThue ctpt : danhsachkh) {
			kh += phongBusiness.gettenkhachhang(ctpt.khachHang.getMaKhachHang()) + "\n";
		}


		// Nếu danh sách rỗng hoặc null, giá trị mặc định là chuỗi rỗng
		khachluutru.setText(kh.isEmpty() ? "Trống" : kh.trim());
	}

	private LocalDateTime convertStringToLocalDateTime(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);
		return localDate.atStartOfDay();
	}

	private String dinhDangNgay(String ngayGoc) {
		SimpleDateFormat dinhDangCu = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dinhDangMoi = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date date = dinhDangCu.parse(ngayGoc);
			return dinhDangMoi.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return ngayGoc;
		}
	}

	@FXML
	void handchuyenphong(ActionEvent event) {
		if (txt_soPhong.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng cần chuyển");
		} else {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ThongTinChuyenPhong_view.fxml"));
				Stage stage = new Stage();
				stage.setScene(new Scene(loader.load()));
				stage.setTitle("Cập nhật thông tin phòng");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

				if (txt_NgayNhanPhong.getText() != null && !txt_NgayNhanPhong.getText().trim().isEmpty()) {
					LocalDate ngayNhan = LocalDate.parse(txt_NgayNhanPhong.getText(), formatter);
					LocalDateTime ngaynhanphong = ngayNhan.atStartOfDay();
					if (txt_Ngaytraphong.getText() != null && !txt_Ngaytraphong.getText().trim().isEmpty()) {
						LocalDate ngayTra = LocalDate.parse(txt_Ngaytraphong.getText(), formatter);
						LocalDateTime ngaytraphong = ngayTra.atStartOfDay();
						System.out.println(ngaynhanphong);
						System.out.println(ngaytraphong);
						ThongTinChuyenPhong_Controller thongtinchuyenphong_Controller = loader.getController();
						LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(maloaigiuong, phong_dao.getHesgia(maloaigiuong));
						LoaiPhong lp = new LoaiPhong(maloaiPhong, phong_dao.getgiacoban(maloaiPhong));
						TrangThaiPhong trangthai = TrangThaiPhong.valueOf("DANGTHUE");
						Phong p = new Phong(txt_soPhong.getText(), lp, lgsd, songuoi, trangthai);
						int chuyenphong = 1;
						thongtinchuyenphong_Controller.setRoomDetails(p, txt_sodienthoai.getText(), ngaynhanphong,
								ngaytraphong, mahoadon,chuyenphong);
					} else {
						System.out.println("Ngày trả phòng không được để trống.");
					}
				} else {
					System.out.println("Ngày nhận phòng không được để trống.");
				}

				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DateTimeParseException e) {
				System.out.println("Định dạng ngày không hợp lệ: " + e.getMessage());
			}
		}
	}

	@FXML
	void giahanPhong(ActionEvent event) {
		if (txt_soPhong.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng cần gia hạn");
		} else {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/From_GiaHanPhong.fxml"));
				Stage stage = new Stage();
				stage.setScene(new Scene(loader.load()));
				stage.setTitle("Cập nhật thông tin phòng");

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

				if (txt_NgayNhanPhong.getText() != null && !txt_NgayNhanPhong.getText().trim().isEmpty()) {
					LocalDate ngayNhan = LocalDate.parse(txt_NgayNhanPhong.getText(), formatter);
					LocalDateTime ngaynhanphong = ngayNhan.atStartOfDay();
					if (txt_Ngaytraphong.getText() != null && !txt_Ngaytraphong.getText().trim().isEmpty()) {
						LocalDate ngayTra = LocalDate.parse(txt_Ngaytraphong.getText(), formatter);
						LocalDateTime ngaytraphong = ngayTra.atStartOfDay();
						LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(maloaigiuong, phong_dao.getHesgia(maloaigiuong));
						LoaiPhong lp = new LoaiPhong(maloaiPhong, phong_dao.getgiacoban(maloaiPhong));
						TrangThaiPhong trangthai = TrangThaiPhong.valueOf("DANGTHUE");
						Phong p = new Phong(txt_soPhong.getText(), lp, lgsd, songuoi, trangthai);
						From_GiaHanPhong giahan_Controller = loader.getController();
						giahan_Controller.setRoomDetails(p, txt_sodienthoai.getText(), ngaynhanphong, ngaytraphong,
								mahoadon);
					} else {
						System.out.println("Ngày trả phòng không được để trống.");
					}
				} else {
					System.out.println("Ngày nhận phòng không được để trống.");
				}

				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DateTimeParseException e) {
				System.out.println("Định dạng ngày không hợp lệ: " + e.getMessage());
			}
		}
	}

	private void timphongtheomaphong() {
		String maphong_x = txttimkiem.getText();

		dsPhong.getChildren().clear();
		for (Phong phong : danhSachPhong) {
			if (phong.getMaPhong().equals(maphong_x) && phong.getTrangThaiPhong().name().equals("DANGTHUE")) {
				String maPhong = phong.getMaPhong().toString();
				String maLoaiPhong = phong.getLoaiPhong().getMaLoaiPhong().toString();
				String maLoaiGiuong = phong.getLoaiGiuongSuDung().getMaLoaiGiuong().toString();
				String trangThai = phong.getTrangThaiPhong().toString();
				int soNguoi = phong.getSoNguoi();
				LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(maLoaiGiuong, phong_dao.getHesgia(maLoaiGiuong));
				LoaiPhong lp = new LoaiPhong(maLoaiPhong, phong_dao.getgiacoban(maLoaiPhong));
				TrangThaiPhong trangthai = TrangThaiPhong.valueOf(trangThai);
				Phong p = new Phong(maPhong, lp, lgsd, soNguoi, trangthai);
				themPhongVaoGiaoDien(p);
			}

		}

	}
}
