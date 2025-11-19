package gui.controllers;

import bus.*;
import com.jfoenix.controls.JFXButton;
import dto.*;
import dto.enum_class.TrangThaiPhieuDat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import utils.ExportFile;
import utils.NumberFormat;
import utils.ShowDialog;

public class TrangQuanLyPhieuDat_Controller implements Initializable {
    @FXML
    private Button btnAll;

    @FXML
    private Button btnInuse;

    @FXML
    private Button btnNearlyReceive;

    @FXML
    private Button btnPaid;

    @FXML
    private Button btnUpcoming;

    @FXML
    private Label currWeekDS;
    @FXML
    private Button rightBtnDS, leftBtnDS;
    @FXML
    private Label lbCountPD;
    @FXML
    private JFXButton refreshBtn, searchBtn, exportExcel;
    @FXML
    private TextField searchBarMaPD;
    @FXML
    private StackPane stackPane;
    @FXML
    private FlowPane pnListPhieuDPView;
    @FXML
    private ScrollPane scrllPane;

    private AtomicReference<LocalDate> currentDate = new AtomicReference<>(LocalDate.now());

    private ArrayList<PhieuDatPhong> listPhieuDat = new ArrayList<>();
    private ArrayList<PhieuDatPhongView_Controller> listPhieuDatView = new ArrayList<>();
    private String selectedTrangThai = "NearlyReceive";

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Tao tua de ngay thang nam
        updateLabelWeekDS(currentDate.get());
        updateListPhieuDat(currentDate.get());
        btnNearlyReceive.getStyleClass().add("selected");
        taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon(selectedTrangThai, listPhieuDat));


        // Nút để di chuyển tới tuần kế trước
        leftBtnDS.setOnAction(event -> {
            LocalDate newCurrentDate = currentDate.get().minusDays(7);
            currentDate.set(newCurrentDate);
            updateLabelWeekDS(currentDate.get());
            updateListPhieuDat(currentDate.get());
            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon(selectedTrangThai, listPhieuDat));
        });

        // Nút để di chuyển tới tuần kế
        rightBtnDS.setOnAction(event -> {
            LocalDate newCurrentDate = currentDate.get().plusDays(7);
            currentDate.set(newCurrentDate);
            updateLabelWeekDS(currentDate.get());
            updateListPhieuDat(currentDate.get());
            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon(selectedTrangThai, listPhieuDat));
        });

        btnAll.setOnAction(event -> {
            selectedTrangThai = "All";
            removeSelected();
            btnAll.getStyleClass().add("selected");

            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon("All", listPhieuDat));
        });

        btnInuse.setOnAction(event -> {
            selectedTrangThai = "Inuse";
            removeSelected();
            btnInuse.getStyleClass().add("selected");

            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon("Inuse", listPhieuDat));
        });

        btnNearlyReceive.setOnAction(event -> {
            selectedTrangThai = "NearlyReceive";
            removeSelected();
            btnNearlyReceive.getStyleClass().add("selected");

            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon("NearlyReceive", listPhieuDat));
        });

        btnPaid.setOnAction(event -> {
            selectedTrangThai = "Paid";
            removeSelected();
            btnPaid.getStyleClass().add("selected");
            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon("Paid", listPhieuDat));
        });

        btnUpcoming.setOnAction(event -> {
            selectedTrangThai = "Upcoming";
            removeSelected();
            btnUpcoming.getStyleClass().add("selected");

            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon("Upcoming", listPhieuDat));
        });

        // Làm mới dữ liệu trong tab danh sách
        refreshBtn.setOnAction(event -> {
            currentDate.set(LocalDate.now());
            updateListPhieuDat(currentDate.get());
            updateLabelWeekDS(currentDate.get());
            removeSelected();
            selectedTrangThai = "NearlyReceive";
            btnNearlyReceive.getStyleClass().add("selected");
            searchBarMaPD.clear();
            taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon(selectedTrangThai, listPhieuDat));
        });

        // Tìm kiếm phiếu đặt phòng theo mã phiếu
        searchBtn.setOnAction(event -> {
            ArrayList<PhieuDatPhong> listPhieuDatSearch;
            listPhieuDatSearch = listPhieuDat.stream().filter(phieuDatPhong -> phieuDatPhong.getKhachHang().getSoDienThoai().equals(searchBarMaPD.getText())).collect(Collectors.toCollection(ArrayList::new));
            if (listPhieuDatSearch.isEmpty() || listPhieuDatSearch == null) {
                ShowDialog.showDialog(stackPane, "Không tìm thấy phiếu đặt", "Không tìm thấy phiếu đặt phòng với mã phiếu đã nhập", "OK");
            } else {
                taoPhieuDatPhongViewVaAddVaoGiaoDien(getListTheoTrangThaiDaChon(selectedTrangThai, listPhieuDatSearch));
            }
        });

        // Xuất danh sách phiếu đặt ra file excel
        exportExcel.setOnAction(event -> {
            ExportFile.ExportExcelPhieuDat(listPhieuDat);
            ShowDialog.showDialog(stackPane, "Xuất file thành công", "Danh sách phiếu đặt đã được xuất ra file excel", "OK");
        });
    }

    // Cập nhật lại danh sách chi tiết hóa đơn và chi tiết phòng đặt
    public void updateListPhieuDat(LocalDate date) {
        LocalDate monDayOfCurr = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunDayOfCurr = monDayOfCurr.plusDays(6);
        listPhieuDat = QuanLyPhieuDat_Business.getPhieuDatPhongTheoThoiGian(monDayOfCurr, sunDayOfCurr);
    }


    // cập nhật lại label tuần trong tuần khi thay đổi ngày trong tab ds
    public void updateLabelWeekDS(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currWeekDS.setText(NumberFormat.dateFormat(monday) + " - " + NumberFormat.dateFormat(sunday));
    }

    public void taoPhieuDatPhongViewVaAddVaoGiaoDien(ArrayList<PhieuDatPhong> listPhieuDat) {
        pnListPhieuDPView.getChildren().clear();
        listPhieuDatView.clear();
        for (PhieuDatPhong phieuDatPhong : listPhieuDat) {
            PhieuDatPhongView_Controller phieuDatPhongView = new PhieuDatPhongView_Controller(phieuDatPhong);
            listPhieuDatView.add(phieuDatPhongView);
        }
        pnListPhieuDPView.getChildren().addAll(listPhieuDatView.stream().map(PhieuDatPhongView_Controller::getRoot).toArray(AnchorPane[]::new));
        lbCountPD.setText(Integer.toString(listPhieuDatView.size()));
    }

    public ArrayList<PhieuDatPhong> getListTheoTrangThaiDaChon(String trangThai, ArrayList<PhieuDatPhong> listPhieuDat) {
        switch (trangThai) {
            case "Inuse":
                return listPhieuDat.stream().filter(phieuDatPhong -> phieuDatPhong.getTrangThaiPhieuDat().equals(TrangThaiPhieuDat.DANHAN)).collect(Collectors.toCollection(ArrayList::new));
            case "NearlyReceive":
                return listPhieuDat.stream().filter(phieuDatPhong -> phieuDatPhong.getTrangThaiPhieuDat().equals(TrangThaiPhieuDat.CHONHAN) && phieuDatPhong.getNgayNhanPhong().equals(LocalDate.now())).collect(Collectors.toCollection(ArrayList::new));
            case "Paid":
                return listPhieuDat.stream().filter(phieuDatPhong -> phieuDatPhong.getTrangThaiPhieuDat().equals(TrangThaiPhieuDat.DAHUY)).collect(Collectors.toCollection(ArrayList::new));
            case "Upcoming":
                return listPhieuDat.stream().filter(phieuDatPhong -> phieuDatPhong.getTrangThaiPhieuDat().equals(TrangThaiPhieuDat.CHONHAN) && phieuDatPhong.getNgayNhanPhong().isAfter(LocalDate.now())).collect(Collectors.toCollection(ArrayList::new));
            default:
                return listPhieuDat;
        }
    }

    public void removeSelected() {
        btnInuse.getStyleClass().remove("selected");
        btnNearlyReceive.getStyleClass().remove("selected");
        btnPaid.getStyleClass().remove("selected");
        btnUpcoming.getStyleClass().remove("selected");
        btnAll.getStyleClass().remove("selected");
    }
}
