package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;

import com.jfoenix.controls.JFXButton;

import bus.QuanLyPhong_Bussiness;
import dto.HoaDon;
import dto.Phong;
import dto.PhongThue;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class From_GiaHanPhong  {
	@FXML
	private DatePicker ngaygiahan;

	@FXML
	private DatePicker DP_ngaytraphong;

	@FXML
	private Button btngiahan;

	@FXML
	private Button btnhuy;
    @FXML
    private JFXButton handchuyenphong;

	private QuanLyPhong_Bussiness quanLyPhong_bussiness = new QuanLyPhong_Bussiness();

	private LocalDateTime ngaygiahantr;


	public void setRoomDetails(Phong p, String sdt, LocalDateTime ngaynhanphong, LocalDateTime ngaytraphong, String mahoadon) {
		ngaygiahan.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);

				if (date.isBefore(DP_ngaytraphong.getValue())) {
					setDisable(true);
					setStyle("-fx-background-color: #D3D3D3;");
				}

				if (date.equals(DP_ngaytraphong.getValue())) {
					setDisable(true);
					setStyle("-fx-background-color: #FF6347;");
				}
			}
		});
		this.DP_ngaytraphong.setDisable(true);
		this.DP_ngaytraphong.setValue(ngaytraphong.toLocalDate());
	    btngiahan.setOnAction(event -> {
			List<PhongThue> dsPhongThue = quanLyPhong_bussiness.getDSPhongThue(mahoadon);
			for (PhongThue pt : dsPhongThue) {
				if(pt.getNgayBatDauO().equals(ngaytraphong)){
					JOptionPane.showMessageDialog(null, "Phòng đã được gia hạn cho tới ngày "+pt.getNgayKetThucO());
					return;
				}
			}
	        if (ngaygiahan.getValue() == null) {
	            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày gia hạn!");
	            return;
	        }

	        if (ngaygiahan.getValue().isBefore(ngaytraphong.toLocalDate())) {
	            JOptionPane.showMessageDialog(null, "Ngày gia hạn phải sau ngày trả phòng!");
	            return;
	        }
	         ngaygiahantr = ngaygiahan.getValue().atTime(0, 0);
			// kiem tra từ ngày trả phòng tới ngày gia hạn chỉ có 30 ngày
			// lấy hóa đơn phòng ngày của phòng này
			String maphoadonphong=quanLyPhong_bussiness.getMaHoaDonPhong(p.getMaPhong());
	        boolean hopLe = quanLyPhong_bussiness.kiemTraNgay(ngaynhanphong, ngaygiahantr, p.getMaPhong(),maphoadonphong);
	        if(hopLe) {
	            HoaDon hd = new HoaDon(mahoadon);
		        PhongThue pt = new PhongThue(hd, p, quanLyPhong_bussiness.getSoNguoiHD(mahoadon),
		        		ngaynhanphong, ngaygiahantr, 
		            p.getGiaPhong());
				HoaDon hd_x = new HoaDon(mahoadon,ngaynhanphong, ngaygiahantr);
				quanLyPhong_bussiness.getupdateHD_giahan(hd_x);
		        quanLyPhong_bussiness.updatePhongThue(pt);
		        JOptionPane.showMessageDialog(null, "Gia hạn phòng thành công!");
		        Stage stage = (Stage) btngiahan.getScene().getWindow();
		        stage.close();
	        }else {
	        	JOptionPane.showMessageDialog(null, "Thời gian gia hạn trùng với lịnh đặt phòng của khách hàng khác bạn hãy chuyển sang 1 phòng khác");
	        }
	    });
        btnhuy.setOnAction(event -> {
            Stage stage = (Stage) btnhuy.getScene().getWindow();
            stage.close();
        });
        handchuyenphong.setOnAction(event ->{
			List<PhongThue> dsPhongThue = quanLyPhong_bussiness.getDSPhongThue(mahoadon);
			for (PhongThue pt : dsPhongThue) {
				if(pt.getNgayBatDauO().equals(ngaytraphong)){
					JOptionPane.showMessageDialog(null, "Phòng đã được gia hạn rồi");
					return;
				}
			}
        	if(ngaygiahan.getValue()!=null) {
        		ngaygiahantr = ngaygiahan.getValue().atTime(0, 0);
    	        try {
    	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ThongTinChuyenPhong_view.fxml"));
    	            Stage stage = new Stage();
    	            stage.setScene(new Scene(loader.load()));
    	            stage.setTitle("Cập nhật thông tin phòng");
    	            int giahan =0;
    	                    ThongTinChuyenPhong_Controller thongtinchuyenphong_Controller = loader.getController();
    	                    thongtinchuyenphong_Controller.setRoomDetails(
    	                    		p,
    	                    		sdt,
    	                            ngaynhanphong,
    	                            ngaygiahantr,
    	                            mahoadon,
    	                            giahan
    	                    );

    	            stage.initModality(Modality.APPLICATION_MODAL);
    	            stage.showAndWait();
					closeWindow();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        } catch (DateTimeParseException e) {
    	            System.out.println("Định dạng ngày không hợp lệ: " + e.getMessage());
    	        }
        	}else {
        	   	JOptionPane.showMessageDialog(null, "Thời gian gia hạn chưa được nhập");
        	}
    	    });
    }
	private void closeWindow() {
		Stage stage = (Stage) handchuyenphong.getScene().getWindow();
		stage.close();
	}
}
