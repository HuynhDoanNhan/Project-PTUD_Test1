package gui.controllers;

import bus.QuanLyPhong_Bussiness;
import dao.Phong_DAO;
import dto.CaLamViec;
import dto.ChiTietPhongThue;
import dto.HoaDon;
import dto.KhachHang;
import dto.LoaiGiuongSuDung;
import dto.LoaiPhong;
import dto.Phong;
import dto.PhongThue;
import dto.enum_class.TrangThaiPhong;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.NumberFormat;

import javax.swing.*;

import com.jfoenix.controls.JFXButton;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class ThongTinChuyenPhong_Controller {
	@FXML
	private VBox dsPhong;
	@FXML
	private ImageView imgPhong;

	@FXML
	private ScrollPane scrollPanePhong;
	private int maPhongINT;
	private List<Phong> danhSachPhong = new ArrayList<>();
	private ArrayList<Phong> sortedList = new ArrayList<>();
	private QuanLyPhong_Bussiness phongBusiness;
	private Phong_DAO phong_dao = new Phong_DAO();
	@FXML
	private FlowPane tangPane_1;
	@FXML
	private ScrollPane scrollPanePhong_1;
	@FXML
	private VBox dsPhong_1;
	@FXML
	private Button btnhuy;

	@FXML
	private Label giaphongcu;

	@FXML
	private Label giaphongmoi;
	@FXML
	private Button btnLuu;
	@FXML
	private Label phichuyenphong;
	private int kiemtra_x;
	
	private ArrayList<JFXButton> dsPhongBT =new ArrayList<JFXButton>();
	private ArrayList<JFXButton> dsPhongBT_2 =new ArrayList<JFXButton>();
	private Map<JFXButton, Label> buttonToLoaiPhongLabel = new HashMap<>();
	private Map<JFXButton, Label> buttonToGiaPhongLabel = new HashMap<>();
	private Map<JFXButton, Label> buttonToLoaiPhong_2Label= new HashMap<>();
	private Map<JFXButton, Label> buttongiaphongPhongLabel= new HashMap<>();
	private Map<JFXButton, Label> buttonmaphong_xLabel= new HashMap<>();



	public void setRoomDetails(Phong p, String sodienthoai, LocalDateTime ngaynhanphong, LocalDateTime ngaytraphong,
			String mahoadon, int kiemtra) {
		kiemtra_x=kiemtra;
		phongBusiness = new QuanLyPhong_Bussiness();
		dsPhong.setSpacing(20);
		LocalDateTime today = LocalDateTime.now();
		giaphongcu.setText(NumberFormat.currencyFormat(phongBusiness.tinhtienPhongCu(p, ngaynhanphong, today, mahoadon)));
		phongBusiness = new QuanLyPhong_Bussiness(dsPhong);
		scrollPanePhong.setContent(dsPhong);
		scrollPanePhong.setFitToWidth(true);
		phongBusiness = new QuanLyPhong_Bussiness(dsPhong_1);
		scrollPanePhong_1.setContent(dsPhong_1);
		scrollPanePhong_1.setFitToWidth(true);
		scrollPanePhong.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		scrollPanePhong_1.setStyle("-fx-background-color: transparent; -fx-border-color: #1f7845;");

		dsPhong.getChildren().clear();
		danhSachPhong = phongBusiness.docdulieuDatabase();
		Set<String> addedRoomCriteria = new HashSet<>();
		dsPhongBT.clear();
		for (Phong phong : danhSachPhong) {
			String maPhong = phong.getMaPhong().toString();
			String maLoaiPhong = phong.getLoaiPhong().getMaLoaiPhong();
			String maLoaiGiuong = phong.getLoaiGiuongSuDung().getMaLoaiGiuong();
			String trangThai = phong.getTrangThaiPhong().toString();
			int soNguoi = phong.getSoNguoi();
			double giaPhong = phong.getGiaPhong();

			String roomCriteria = maLoaiPhong + "-" + maLoaiGiuong;

			boolean isValid = false;
			if ((maLoaiPhong.equals("STD") || maLoaiPhong.equals("SUP"))
					&& (maLoaiGiuong.equals("SINGLE") || maLoaiGiuong.equals("DOUBLE"))) {
				isValid = true;
			} else if ((maLoaiPhong.equals("DLX") || maLoaiPhong.equals("SUT"))
					&& (maLoaiGiuong.equals("QUEEN") || maLoaiGiuong.equals("KING"))) {
				isValid = true;
			}
			if (isValid && !addedRoomCriteria.contains(roomCriteria) && (kiemtra_x==1)&&giaPhong >= p.getGiaPhong()) {
				LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(maLoaiGiuong, phong_dao.getHesgia(maLoaiGiuong));
				LoaiPhong lp = new LoaiPhong(maLoaiPhong, phong_dao.getgiacoban(maLoaiPhong));
				TrangThaiPhong trangthai = TrangThaiPhong.valueOf(trangThai);
				Phong p_x = new Phong(maPhong, lp, lgsd, soNguoi, trangthai);
				themPhongVaoGiaoDien(p_x, ngaynhanphong, ngaytraphong, sodienthoai, p, mahoadon);
				addedRoomCriteria.add(roomCriteria);
			}else if(isValid && !addedRoomCriteria.contains(roomCriteria) && (kiemtra_x==0)) {
				LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(maLoaiGiuong, phong_dao.getHesgia(maLoaiGiuong));
				LoaiPhong lp = new LoaiPhong(maLoaiPhong, phong_dao.getgiacoban(maLoaiPhong));
				TrangThaiPhong trangthai = TrangThaiPhong.valueOf(trangThai);
				Phong p_x = new Phong(maPhong, lp, lgsd, soNguoi, trangthai);
				themPhongVaoGiaoDien(p_x, ngaynhanphong, ngaytraphong, sodienthoai, p, mahoadon);
				addedRoomCriteria.add(roomCriteria);
			}
		}
		btnhuy.setOnAction(event -> {
			Stage stage = (Stage) btnhuy.getScene().getWindow();
			stage.close();
		});
	}

	public void themPhongVaoGiaoDien(Phong p_x, LocalDateTime ngaynhanphong, LocalDateTime ngaytraphong,
			String sodienthoai, Phong p_cu, String mahoadon) {
		phongBusiness = new QuanLyPhong_Bussiness();
		String tang = phongBusiness.tinhtang(p_x.getMaPhong());
		int tangInt = Integer.valueOf(tang);
		FlowPane tangPane = timHoacTaoTang(tangInt);
		JFXButton phongButton = new JFXButton();
		phongButton.setPrefHeight(50.0);
		phongButton.setPrefWidth(520.0);
		phongButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff;");
		FlowPane.setMargin(phongButton, new Insets(0, 0, 0, 20));
		Pane pane = new Pane();
		pane.setPrefHeight(50.0);
		pane.setPrefWidth(520.0);

		Map<String, String> loaiPhongKS = new HashMap<>();
		loaiPhongKS.put("DLX", "Deluxe");
		loaiPhongKS.put("STD", "Standard");
		loaiPhongKS.put("SUP", "Superior");
		loaiPhongKS.put("SUT", "Suite");
		String tenLoaiPhong = loaiPhongKS.get(p_x.getLoaiPhong().getMaLoaiPhong());

		Label loaiphong_x = new Label("Phòng " + tenLoaiPhong + " giường " + p_x.getLoaiGiuongSuDung().getMaLoaiGiuong()
				+ " cho " + p_x.getSoNguoi() + " người");
		loaiphong_x.setLayoutX(10.0);
		loaiphong_x.setLayoutY(10);
		loaiphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

		Label giaphong_x = new Label("Giá: " + NumberFormat.currencyFormat(p_x.getGiaPhong()));
		giaphong_x.setLayoutX(350.0);
		giaphong_x.setLayoutY(10);
		giaphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
		pane.getChildren().addAll(loaiphong_x, giaphong_x);
		phongButton.setGraphic(pane);

		tangPane.getChildren().add(phongButton);
		dsPhongBT.add(phongButton);

		
		buttonToLoaiPhongLabel.put(phongButton, loaiphong_x);
		buttonToGiaPhongLabel.put(phongButton, giaphong_x);
		phongButton.setOnAction(event -> {
		    dsPhong_1.getChildren().clear();

		    for (JFXButton bt : dsPhongBT) {
		    	Label loaiPhongLabel = buttonToLoaiPhongLabel.get(bt);
		    	Label giaPhongLabel = buttonToGiaPhongLabel.get(bt);
		        if (loaiPhongLabel != null) {
		            loaiPhongLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #000000;"); 
		        }
		        if (giaPhongLabel != null) {
		            giaPhongLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #000000;");
		        }
		        bt.setStyle("-fx-background-color: #ffffff;"); 
		    }
		    phongButton.setStyle("-fx-background-color: #1f7845;"); 
		    loaiphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #FFFFFF;"); 
		    giaphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #FFFFFF;"); 
		    themPhongVaoGiaoDien_Phong(p_x, ngaynhanphong, ngaytraphong, sodienthoai, p_cu, mahoadon);
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
							System.out.println("Found existing floor: " + tang);
							break;
						}
					}
				}
			}
		}
		if (tangPane == null) {
			tangPane = new FlowPane();
			tangPane.setPrefWidth(854.0);
			HBox tangInfoBox = new HBox();
			Label tangLabel = new Label();
			tangInfoBox.getChildren().add(tangLabel);
			VBox floorBox = new VBox();
			floorBox.getChildren().addAll(tangInfoBox, tangPane);
			floorBox.setSpacing(0);
			floorBox.setPadding(new Insets(-10));
			tangInfoBox.setPadding(new Insets(0));

			dsPhong.getChildren().add(floorBox);
		}
		return tangPane;
	}

	public void themPhongVaoGiaoDien_Phong(Phong p_x, LocalDateTime ngaynhanphong, LocalDateTime ngaytraphonga,
			String sodienthoai,Phong p_cu, String mahoadon) {
		phongBusiness = new QuanLyPhong_Bussiness();
		List<Phong> danhSachPhong = phongBusiness.laydanhSachPhong(p_x.getLoaiPhong().getMaLoaiPhong(),
				p_x.getLoaiGiuongSuDung().getMaLoaiGiuong());

		for (Phong phong : danhSachPhong) {
			String maPhong = phong.getMaPhong();
			double giaPhong = phong.getGiaPhong();
			String loaiPhong = phong.getLoaiPhong().getMaLoaiPhong();
			String loaiGiuong = phong.getLoaiGiuongSuDung().getMaLoaiGiuong();
			String trangthai = phong.getTrangThaiPhong().toString();
			int soNguoi = phong.getSoNguoi();
			LocalDateTime ngaychuyen = LocalDateTime.now();
			String maphoadonphong=phongBusiness.getMaHoaDonPhong(p_x.getMaPhong());
			boolean hopLe = phongBusiness.kiemTraNgay(ngaychuyen, ngaytraphonga, maPhong,maphoadonphong);
			if (hopLe && trangthai.equalsIgnoreCase("CONTRONG") && (giaPhong >= p_x.getGiaPhong())) {
				String tang = phongBusiness.tinhtang(maPhong);
				int tangInt = Integer.valueOf(tang);
				FlowPane tangPane_1 = timHoacTaoTang_1(tangInt);
				JFXButton phongButton = new JFXButton();
				phongButton.setPrefHeight(170.0);
				phongButton.setPrefWidth(580.0);
				phongButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff;");
				FlowPane.setMargin(phongButton, new Insets(0, 0, 0, 20));
				ImageView img = new ImageView(getClass().getResource("/image/hinhPhong/" +phong.getMaPhong() + ".jpg").toExternalForm());
				img.setLayoutX(10.0);
				img.setLayoutY(20.0);
				img.setFitWidth(170);
				img.setFitHeight(120);
				img.setPreserveRatio(false);
				img.getStyleClass().add("img");
				Pane pane = new Pane();
				pane.setPrefHeight(140.0);
				pane.setPrefWidth(580.0);

				Label giatienLabel = new Label("Giá phòng " + NumberFormat.currencyFormat(giaPhong) + " VND");
				giatienLabel.setLayoutX(230);
				giatienLabel.setLayoutY(70);
				giatienLabel.setPrefHeight(3.0);
				giatienLabel.setPrefWidth(200);

				Label maphong_x = new Label(maPhong);
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
				String tenLoaiPhong = loaiPhongKS.get(p_x.getLoaiPhong().getMaLoaiPhong());

				Label loaiphong_x = new Label("Phòng " + tenLoaiPhong + " giường "
						+ p_x.getLoaiGiuongSuDung().getMaLoaiGiuong() + " cho " + soNguoi + " người");
				loaiphong_x.setLayoutX(200.0);
				loaiphong_x.setLayoutY(40);
				loaiphong_x.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
				tangPane_1.getChildren().add(phongButton);
				pane.getChildren().addAll(loaiphong_x, giatienLabel, img);
				pane.getChildren().add(maphong_x);			
				phongButton.setGraphic(pane);

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
				    LoaiPhong lp = new LoaiPhong(loaiPhong, phong_dao.getgiacoban(loaiPhong));
					LoaiGiuongSuDung lgsd = new LoaiGiuongSuDung(loaiGiuong, phong_dao.getHesgia(loaiGiuong));
					TrangThaiPhong trangthai_x = TrangThaiPhong.valueOf(trangthai);
					Phong p = new Phong(maPhong, lp, lgsd, soNguoi, trangthai_x);
					LocalDateTime today = LocalDateTime.now();
					giaphongmoi.setText(
							NumberFormat.currencyFormat(phongBusiness.tinhtienPhongMoi(p, ngaytraphonga, mahoadon)));
					chuyenphong(p, sodienthoai, today, ngaynhanphong, ngaytraphonga, p_cu, mahoadon);
				});
			}
		}
	}
	

	private FlowPane timHoacTaoTang_1(int tang) {
		FlowPane tangPane = null;
		for (Node node : dsPhong_1.getChildren()) {
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
			tangLabel.setPrefWidth(77.0);
			HBox tangInfoBox = new HBox(-30);
			tangInfoBox.getChildren().addAll(tangLabel);
			tangInfoBox.setAlignment(Pos.CENTER_LEFT);

			VBox floorBox = new VBox();
			floorBox.getChildren().addAll(tangInfoBox, tangPane);
			floorBox.setSpacing(10);
			dsPhong_1.getChildren().add(floorBox);
		}
		return tangPane;
	}

	public void chuyenphong(Phong p, String sodienthoai, LocalDateTime today, LocalDateTime ngayNhanPhong,
			LocalDateTime ngaytraphong, Phong p_cu, String mahoadon) {
		btnLuu.setOnAction(event -> {
			if (giaphongmoi.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng chọn looại phòng cần chuyển");
			} else {
				HoaDon hd = new HoaDon(mahoadon);
				CaLamViec clv = new CaLamViec(phongBusiness.getCalamviec(mahoadon));
				KhachHang kh = new KhachHang(phongBusiness.getKhacHang(mahoadon));
				if (kiemtra_x == 1) {
					PhongThue pt = new PhongThue(hd, p, phongBusiness.getSoNguoiHD(mahoadon), today, ngaytraphong,
							p.getGiaPhong());
					boolean chuyenphong_x = phongBusiness.chuyenphong(pt);
					PhongThue pt_2=new PhongThue(hd, p_cu,phongBusiness.getSoNguoiHD(mahoadon), ngayNhanPhong, today, p_cu.getGiaPhong());
					boolean updatePhongThue= phongBusiness.updatePhongThue(pt_2);
					List<ChiTietPhongThue> danhsachkh=phongBusiness.getKhachHang(mahoadon, p_cu.getMaPhong());
					for (ChiTietPhongThue ctpt : danhsachkh) {
						boolean themChiTietPhongThue = phongBusiness.themChiTietPhongThue(mahoadon, p.getMaPhong(),ctpt.khachHang);
					}
					double phi = 50000;
					String ghichuHienTai = phongBusiness.getGhichu(mahoadon);
					double tongPhiBoXung = phongBusiness.getPhiBoXung(mahoadon) + phi;

					if (ghichuHienTai.contains("Phí chuyển Phòng:")) {
						String[] parts = ghichuHienTai.split("Phí chuyển Phòng:");
						if (parts.length > 1) {
							String phanSau = parts[1].trim();
							double phiHienTai = Double.parseDouble(phanSau.replaceAll("[^\\d.]", ""));
							tongPhiBoXung = phiHienTai + phi;
							ghichuHienTai = parts[0] + "Phí chuyển Phòng: " + NumberFormat.currencyFormat(tongPhiBoXung);
						}
					} else {
						ghichuHienTai += " Phí chuyển Phòng: " + NumberFormat.currencyFormat(phi);
					}

					HoaDon hd_x = new HoaDon(mahoadon, tongPhiBoXung,ghichuHienTai);
					phongBusiness.getupdateHDChuyenPhong(hd_x);
					phongBusiness.updatePhongdangthue(p.getMaPhong());
					phongBusiness.updatePhongdangtrong(p_cu.getMaPhong());
					JOptionPane.showMessageDialog(null, "Chuyển phòng thành công");
					closeWindow();
				}
				else if(kiemtra_x == 0) {
					LocalDateTime ngaytra=phongBusiness.getNgayTraPhong(mahoadon,p_cu.getMaPhong());
					PhongThue pt = new PhongThue(hd, p, phongBusiness.getSoNguoiHD(mahoadon), ngaytra, ngaytraphong,
							p.getGiaPhong());
					boolean chuyenphong_x = phongBusiness.chuyenphong(pt);
					List<ChiTietPhongThue> danhsachkh=phongBusiness.getKhachHang(mahoadon, p_cu.getMaPhong());
					for (ChiTietPhongThue ctpt : danhsachkh) {
						boolean themChiTietPhongThue = phongBusiness.themChiTietPhongThue(mahoadon, p.getMaPhong(),ctpt.khachHang);
					}
					HoaDon hd_x = new HoaDon(mahoadon, clv, kh, ngayNhanPhong, ngaytraphong,
							phongBusiness.getPhiBoXung(mahoadon), phongBusiness.getTrangThaiHD(mahoadon),
							phongBusiness.getGhichu(mahoadon));
					phongBusiness.getupdateHD(hd_x);
					JOptionPane.showMessageDialog(null, "Chuyển phòng thành công");
					closeWindow();
				}
			}
		});

	}
	private void closeWindow() {
		Stage stage = (Stage) btnLuu.getScene().getWindow();
		stage.close();
	}
}
