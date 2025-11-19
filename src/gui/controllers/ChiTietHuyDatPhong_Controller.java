package gui.controllers;

import bus.KhachHang_Business;
import dao.*;
import dto.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;

import com.aspose.words.Document;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import utils.ExportFile;

import java.io.IOException;

public class ChiTietHuyDatPhong_Controller implements Initializable {

	@FXML
	public Label ngayDat;
	@FXML
	public Label ngayNhan;
	@FXML
	public Label ngayTra;
	@FXML
	public Label maPhong;
	@FXML
	public Label tenDichVu;
	@FXML
	private Label ten;
	@FXML
	private Label phiHuy;
	@FXML
	private Label giaPhong;
	@FXML
	private Label tienCoc;
	@FXML
	private Label sdt;
	@FXML
	private Button btnHuy;
	@FXML
	private StackPane stackPane;

	private Phong_DAO daoPhong = new Phong_DAO();
	private HoaDonTra_DAO daoHoaDonTra = new HoaDonTra_DAO();
	private KhachHang_Business khachHang_Business = new KhachHang_Business();
	private PhieuDatPhong_DAO daoPhieuDat = new PhieuDatPhong_DAO();
	private PhongDat_DAO daoPhongDat = new PhongDat_DAO();
	private KhachHang_DAO daokhachHang = new KhachHang_DAO();
	private LoaiPhong_DAO daoLoaiPhong = new LoaiPhong_DAO();
	private HuyDatPhong_Controller parentController;
	//private CaLamViec caLamViec;

	private KhungGiaoDien_controller khungGiaoDien;

	public void setKhungGiaoDien_controller(KhungGiaoDien_controller khungGiaoDien) {
		this.khungGiaoDien = khungGiaoDien;
	}

	public ChiTietHuyDatPhong_Controller() {
	}

	public void setParentController(HuyDatPhong_Controller parentController) {
		this.parentController = parentController;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	public void setPhongDat(PhongDat phongDat) {
		// Định dạng phí hủy với đơn vị tiền tệ
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

		ngayDat.setText(phongDat.getPhieuDatPhong().getNgayDatPhong().toString());
		ngayNhan.setText(phongDat.getPhieuDatPhong().getNgayNhanPhong().toString());
		ngayTra.setText(phongDat.getPhieuDatPhong().getNgayTraPhong().toString());
		maPhong.setText(phongDat.getPhong().getMaPhong());
		ten.setText(phongDat.phieuDatPhong.getKhachHang().getTenKhachHang());
		sdt.setText(phongDat.phieuDatPhong.getKhachHang().getSoDienThoai());

		// Tính giá phòng
		double gia = phongDat.getThanhTien();
		giaPhong.setText(currencyFormat.format(gia));

		// Tính phí hủy đặt phòng
		LocalDate ngayNhanPhong = phongDat.getPhieuDatPhong().getNgayNhanPhong();
		double tienCocPhong = 0;

		Phong phong = daoPhong.getMaLoaiPhongTinhCoc(phongDat.getPhong().getMaPhong().toString());
		switch (phong.getLoaiPhong().getMaLoaiPhong()) {
		case "DLX" -> {
			tienCocPhong = 0.35 * gia;
		}
		case "SUT" -> {
			tienCocPhong = 0.4 * gia;
		}
		default -> {
			tienCocPhong = 0.3 * gia;
		}
		}
		int soNgay = Period.between(LocalDate.now(), ngayNhanPhong).getDays();
		if (soNgay == 0) {
			soNgay = 1; // Nếu ngày nhận và ngày trả giống nhau, tính là 1 ngày
        }
		double tienPhiHuy = 0.0;
		// Tính phí hủy theo quy định
		if (soNgay >= 15) {
			tienPhiHuy = 0.3 * tienCocPhong;
		} else if (soNgay >= 3) {
			tienPhiHuy = 0.5 * tienCocPhong;
		} else {
			tienPhiHuy = tienCocPhong;
		}
		// hiển thị trên lable phí hủy
		phiHuy.setText(currencyFormat.format(tienPhiHuy));
		// hiển thị lable tiền cọc
		tienCoc.setText(currencyFormat.format(tienCocPhong));

		btnHuy.setOnAction(event -> {
			String maPhong = phongDat.getPhong().getMaPhong();
			// Xóa chi tiết phòng đặt khỏi cơ sở dữ liệu
			boolean isDeleted = daoPhongDat.HuyPhongDatTheoMa(maPhong);

			if (isDeleted) {
				// Tạo hóa đơn trả
				if (taoHoaDon()) {
					showDialog(stackPane, "Thành công", "Hủy đặt phòng và tạo hóa đơn trả thành công ", "OK");
				} else {
					showDialog(stackPane, "Thông báo", "Tạo hóa đơn trả không thành công", "OK");
				}
//				//cập nhật lại tiền đặt phòng
//				double tienDatCoc = 0;
//                try {
//                    tienDatCoc = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(tienCoc.getText()).doubleValue();
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                parentController.getPhieuDatPhong_selected().setTienCoc(parentController.getPhieuDatPhong_selected().getTienCoc() - tienDatCoc);
//				//update tiền cọc trong phiếu đặt
//				daoPhieuDat.updateTienCoc(parentController.getPhieuDatPhong_selected());

				parentController.updateDSChiTietPhongDat(parentController.getPhieuDatPhong_selected());
				parentController.updataVBChiTietPhongDat();
				parentController.kiemTraVaCapNhatTrangThaiPhieuDat(parentController.getPhieuDatPhong_selected());
				// Đóng cửa sổ hiện tại
				Stage stage = (Stage) btnHuy.getScene().getWindow();
				stage.close();
			} else {
				showDialog(stackPane, "Lỗi", "Hủy đặt phòng thất bại", "OK");
			}
		});
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

	public String taoMaHoaDon() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		Date date = java.sql.Date.valueOf(LocalDate.now());
		String datePart = dateFormat.format(date);

		// Tạo 4 số ngẫu nhiên
		Random random = new Random();
		int randomPart = 1000 + random.nextInt(9000);
		return "HDT" + datePart + randomPart;
	}

	public boolean taoHoaDon() {
		double lePhi = 0;
		double tienCocPhong = 0;

		try {
			// Parse phí hủy và tiền cọc
			lePhi = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(phiHuy.getText()).doubleValue();
			tienCocPhong = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(tienCoc.getText()).doubleValue();
			//tienCocPhieu = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(tienCoc.getText()).doubleValue();
			
		} catch (ParseException e) {
			e.printStackTrace();
			showDialog(stackPane, "Lỗi", "Không thể đọc giá trị tiền tệ. Vui lòng kiểm tra dữ liệu đầu vào.", "OK");
			return false;
		}
		double soTienHoanTra = tienCocPhong - lePhi;
		// Tạo hóa đơn trả
		HoaDonTra hoaDonTra = new HoaDonTra(taoMaHoaDon(), khungGiaoDien.getCaLamViec(), parentController.getPhieuDatPhong_selected(),
				lePhi, soTienHoanTra, LocalDate.now());

		try {
			// Lưu hóa đơn trả vào cơ sở dữ liệu
			daoHoaDonTra.insertHoaDonTra(hoaDonTra);
			createPDF(hoaDonTra);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			showDialog(stackPane, "Lỗi", "Có lỗi khi tạo hóa đơn trả PDF.", "OK");
			return false;
		}
	}

	public boolean createPDF(HoaDonTra hoaDonTra) {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		InputStream file = Objects.requireNonNull(ExportFile.class.getResourceAsStream("/data/HoaDonTra/HoaDonTra_Mau.docx"));
//		String fileWord = "D:\\DoAn1_PTUD\\data\\HoaDonTra\\HoaDonTra_" + hoaDonTra.getMaHoaDon() + ".docx";
//		String filePDF = "D:\\DoAn1_PTUD\\data\\HoaDonTra\\HoaDonTra_" + hoaDonTra.getMaHoaDon() + ".pdf";

		String fileWord = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "HoaDonTra_" + hoaDonTra.getMaHoaDon() + ".docx";
		String filePDF = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "HoaDonTra_" + hoaDonTra.getMaHoaDon() + ".pdf";

		try {
			// Đoc file mẫu
//			FileInputStream file = new FileInputStream(fileMau);
			XWPFDocument document = new XWPFDocument(file);

			// thay nôi dung
			double tienCocPhong = 0;
			double thanhTien = 0;
			tienCocPhong = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(tienCoc.getText()).doubleValue();
			thanhTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(giaPhong.getText()).doubleValue();
			
			Map<String, String> noiDung = new HashMap<>();
			noiDung.put("{{NgaySinhh}}", LocalDate.now().toString());
			noiDung.put("{{TenKhachHang}}", hoaDonTra.phieuDatPhong.getKhachHang().getTenKhachHang());
			noiDung.put("{{SoDienThoaii}}", hoaDonTra.phieuDatPhong.getKhachHang().getCCCD());
			noiDung.put("{{NgaySinh}}", hoaDonTra.phieuDatPhong.getKhachHang().getNgaySinh().toString());
			noiDung.put("{{SoDienThoai}}", hoaDonTra.phieuDatPhong.getKhachHang().getSoDienThoai());
			noiDung.put("{{MaHoaDon}}", hoaDonTra.getMaHoaDon());
			noiDung.put("{{TienCoc}}", currencyFormat.format(tienCocPhong));
			noiDung.put("{{PhiHuy}}", currencyFormat.format(hoaDonTra.getLePhi()));
			noiDung.put("{{SoTienHoanTra}}", currencyFormat.format(hoaDonTra.getSoTienHoanTra()));
			
			
			int soNgay = Period.between(parentController.getPhieuDatPhong_selected().getNgayNhanPhong(), parentController.getPhieuDatPhong_selected().getNgayTraPhong()).getDays();
			if (soNgay == 0) {
				soNgay = 1; // Nếu ngày nhận và ngày trả giống nhau, tính là 1 ngày
	        }
			noiDung.put("{{Ngay}}", String.valueOf(soNgay));
			noiDung.put("{{Hang}}", maPhong.getText());
			noiDung.put("{{Ma}}", currencyFormat.format(thanhTien));
		
			thayTheNoiDung(document, noiDung);

			// Lưu Word mới
			FileOutputStream fos = new FileOutputStream(fileWord);
			document.write(fos);
			
			Document doc = new Document(fileWord);
			// Lưu tài liệu PDF
			doc.save(filePDF);
			//Mở tài liệu
			openFile(filePDF);
			
			fos.close();
			document.close();
			
	        return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return true;
	}

	private void thayTheNoiDung(XWPFDocument document, Map<String, String> replacements) {
		for (XWPFParagraph paragraph : document.getParagraphs()) {
			for (XWPFRun run : paragraph.getRuns()) {
				String text = run.getText(0);
				if (text != null) {
					for (Map.Entry<String, String> entry : replacements.entrySet()) {
						text = text.replace(entry.getKey(), entry.getValue());
					}
					run.setText(text, 0);
				}
			}
		}
		
		// Duyệt qua từng bảng
	    for (XWPFTable table : document.getTables()) {
	        for (XWPFTableRow row : table.getRows()) {
	            for (XWPFTableCell cell : row.getTableCells()) {
	                for (XWPFParagraph paragraph : cell.getParagraphs()) {
	                	for (XWPFRun run : paragraph.getRuns()) {
	                        String text = run.getText(0);
	                        if (text != null) {
	                            for (Map.Entry<String, String> entry : replacements.entrySet()) {
	                                text = text.replace(entry.getKey(), entry.getValue());
	                            }
	                            run.setText(text, 0);
	                        }
	                    }
	                }
	            }
	        }
	    }
	}
	
	public void openFile(String filePath) {
		try {
			File pdfFile = new File(filePath);
			if (!pdfFile.exists()) {
				return;
			}
			Desktop destop = Desktop.getDesktop();
			if (destop.isSupported(Desktop.Action.OPEN)) {
				destop.open(pdfFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showDialog(stackPane,"Lỗi", "Không thể mở tập tin PDF!", "OK");
		}
	}
}
