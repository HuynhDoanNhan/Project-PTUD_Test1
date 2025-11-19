package gui.controllers;

import com.jfoenix.controls.JFXButton;
import dto.DichVu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXComboBox;

import bus.ThongKeVaBaoCao_Business;
import dto.ChiTietDichVu;
import dto.HoaDon;
import dto.PhongThue;
import javafx.scene.layout.StackPane;
import utils.ExportFile;
import utils.NumberFormat;
import utils.ShowDialog;

import javax.swing.*;

public class ThongKe_Controller implements Initializable {

    @FXML
    private LineChart<String, Number> bieudo;


    @FXML
    private JFXButton exportExcel;

    @FXML
    private JFXComboBox<String> ccboxnam;
    @FXML
    private TableColumn<HoaDon, String> columGhiChu;

    @FXML
    private TableColumn<HoaDon, String> columMaHoaDon;

    @FXML
    private TableColumn<HoaDon, String> columNgayNhanPhong;

    @FXML
    private TableColumn<HoaDon, String> columNgayTraPhong;

    @FXML
    private TableColumn<HoaDon, String> columTongTien;

    private ObservableList<HoaDon> list;
    @FXML
    private TableView<HoaDon> tableView = new TableView<HoaDon>();
    private List<HoaDon> data = new ArrayList<HoaDon>();
    @FXML
    private JFXComboBox<String> ccboxthang;
    private ThongKeVaBaoCao_Business thongke = new ThongKeVaBaoCao_Business();
    @FXML
    private TextField txtGiaPhong;

    @FXML
    private TextField txthgiadichvu;

    @FXML
    private TextField txtmahoaDon;

    @FXML
    private TextField txtsophong;

    @FXML
    private TextField txttensanPham;

    @FXML
    private TextField txttongdoanhthu;

    @FXML
    private StackPane stackPane;
    @FXML
    private TextField txtphiphatsinh;
    @FXML
    private PieChart bieudotron;

    @FXML
    private JFXComboBox<String> luachonbieudo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        luachonbieudo.getItems().addAll("Line Chart", "Pie Chart");
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        // Thêm các tháng vào ComboBox tháng
        ccboxthang.getItems().clear();
        for (int i = 1; i <= 12; i++) {
            ccboxthang.getItems().add(String.valueOf(i));  // Thêm các tháng từ 1 đến 12
        }

        // Đặt giá trị tháng hiện tại
        ccboxthang.setValue(String.valueOf(currentMonth));
        // Đặt giá trị mặc định
        if (luachonbieudo.getValue() == null) {
            luachonbieudo.setValue("Line Chart");
        }
        DocDuLieuDatabaseVaoTable(currentMonth, currentYear);
        // Xử lý sự kiện thay đổi biểu đồ
        luachonbieudo.setOnAction(event -> {
            String selectedChart = luachonbieudo.getValue();
            if ("Line Chart".equals(selectedChart)) {
                bieudo.setVisible(true);
                bieudotron.setVisible(false);
            } else if ("Pie Chart".equals(selectedChart)) {
                bieudo.setVisible(false);
                bieudotron.setVisible(true);
                updatePieChart(Integer.parseInt(ccboxnam.getValue()));// Cập nhật biểu đồ tròn với dữ liệu năm 2024
            }
        });

        // Hiển thị Line Chart ban đầu
        getThongKeData(1, 2024);
        bieudo.setVisible(true);
        bieudotron.setVisible(false);
        Thuchienlaynam();

        ccboxthang.setOnAction(e -> {
            int year = Integer.parseInt(ccboxnam.getValue());
            int month = Integer.parseInt(ccboxthang.getValue());

            DocDuLieuDatabaseVaoTable(month, year);

            getThongKeData(month, year);
            getThongKeData(1, year);
            if ("Pie Chart".equals(luachonbieudo.getValue())) {
                updatePieChart(year);
            }
        });
    }

    private void updatePieChart(int year) {
        // Lấy dữ liệu từ hệ thống
        double totalPhong = thongke.getDoanhThuTheoThang(year).stream()
                .mapToDouble(Double::doubleValue).sum();
        double totalDichVu = thongke.getDoanhThuDichVuTheoThang(year).values().stream()
                .mapToDouble(Double::doubleValue).sum();
        double total = totalPhong + totalDichVu;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(String.format("Doanh thu phòng (%.1f%%)", (totalPhong / total) * 100), totalPhong),
                new PieChart.Data(String.format("Doanh thu dịch vụ (%.1f%%)", (totalDichVu / total) * 100), totalDichVu)
        );

        bieudotron.setData(pieChartData);
        bieudotron.setTitle("Tổng doanh thu năm " + year);

        for (PieChart.Data data : bieudotron.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                Tooltip tooltip = new Tooltip(
                        String.format("%s: %.2f", data.getName(), data.getPieValue())
                );
                Tooltip.install(data.getNode(), tooltip);
            });
        }
    }


    private void initializeTableColumns() {
        columMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
        columNgayNhanPhong.setCellValueFactory(new PropertyValueFactory<>("ngayNhanPhong"));
        columNgayTraPhong.setCellValueFactory(new PropertyValueFactory<>("ngayTraPhong"));
        columTongTien.setCellValueFactory(cellData -> {
            HoaDon hoaDon = cellData.getValue();
            String maHoaDon = hoaDon.getMaHoaDon();
            List<PhongThue> phongThues = thongke.getPhongThueByMaHoaDon(maHoaDon);
            ArrayList<ChiTietDichVu> chitietDichVu = thongke.getChiTietDichVuByMaHoaDon(maHoaDon);
            double hoadontien = thongke.getTienHoaDon(phongThues, hoaDon, chitietDichVu);
            return new SimpleStringProperty(String.valueOf(NumberFormat.currencyFormat(hoadontien)));
        });
        columGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !tableView.getSelectionModel().isEmpty()) {
                HoaDon selectedHoaDon = tableView.getSelectionModel().getSelectedItem();
                if (selectedHoaDon != null) {
                    txtmahoaDon.setText(selectedHoaDon.getMaHoaDon());
                    List<PhongThue> phongThues = thongke.getPhongThueByMaHoaDon(selectedHoaDon.getMaHoaDon());
                    String tenPhong = "";
                    for (PhongThue pt : phongThues) {
                        tenPhong += pt.getPhong().getMaPhong() + " ";
                    }
                    txtsophong.setText(tenPhong);
                    List<DichVu> chitietDichVu = thongke.TenDichVuByMaHoaDon(selectedHoaDon.getMaHoaDon());
                    Map<String, Integer> dichVuMap = new HashMap<>();

                    for (DichVu dv : chitietDichVu) {
                        String tenDichVu = dv.getTenDichVu();
                        dichVuMap.put(tenDichVu, dichVuMap.getOrDefault(tenDichVu, 0) + 1);
                    }
                    StringBuilder tenDichVuHienThi = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : dichVuMap.entrySet()) {
                        if (entry.getValue() > 1) {
                            tenDichVuHienThi.append(entry.getKey()).append(" (x").append(entry.getValue()).append(") ");
                        } else {
                            tenDichVuHienThi.append(entry.getKey()).append(" ");
                        }
                    }
                    txttensanPham.setText(tenDichVuHienThi.toString().trim());
                    txttensanPham.setEditable(false);
                    txtGiaPhong.setEditable(false);
                    txthgiadichvu.setEditable(false);
                    txttongdoanhthu.setEditable(false);
                    txtsophong.setEditable(false);
                    txtmahoaDon.setEditable(false);
                    txtphiphatsinh.setEditable(false);
                    List<PhongThue> data = thongke.getPhongThueByMaHoaDon(selectedHoaDon.getMaHoaDon());
                    double tienPhong = thongke.getTienPhongThang(data);
                    txtGiaPhong.setText(String.valueOf(NumberFormat.currencyFormat(tienPhong)));
                    ArrayList<ChiTietDichVu> chitietDichVu_x = thongke.getChiTietDichVuByMaHoaDon(selectedHoaDon.getMaHoaDon());
                    double tienDichVu = 0;
                    for (ChiTietDichVu dv : chitietDichVu_x) {
                        tienDichVu += dv.getDonGia();
                    }
                    txthgiadichvu.setText(String.valueOf(NumberFormat.currencyFormat(tienDichVu)));
                    double hoadontien = thongke.getTienHoaDon(phongThues, selectedHoaDon, chitietDichVu_x);
                    txttongdoanhthu.setText(String.valueOf(NumberFormat.currencyFormat(hoadontien)));
                    txtphiphatsinh.setText(String.valueOf(NumberFormat.currencyFormat(selectedHoaDon.getPhiBoSung())));
                }
            }
        });
    }

    private void Thuchienlaynam() {
        ccboxnam.getItems().clear();
        List<Integer> years = thongke.getAllYears();
        years.sort(Integer::compareTo);
        List<String> items = years.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        ccboxnam.getItems().addAll(items);

        // Đặt mục đầu tiên là năm cuối cùng (năm lớn nhất)
        if (!items.isEmpty()) {
            ccboxnam.getSelectionModel().select(items.size() - 1); // Chọn mục cuối cùng
        }

        ccboxnam.setOnAction(this::onYearChanged);
    }

    private void onYearChanged(ActionEvent event) {
        int year = Integer.parseInt(ccboxnam.getValue());
        List<Integer> months = thongke.getMonthsForYear(year);
        months.sort(Integer::compareTo);
        ccboxthang.getItems().clear();
        ccboxthang.getItems().addAll(months.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    public void DocDuLieuDatabaseVaoTable(int month, int year) {
        data = thongke.getDataByYearAndMonth(year, month);
        list = FXCollections.observableArrayList(data);
        tableView.setItems(list);
        exportExcel.setOnAction(event -> {
            ExportFile.ExportExcelHoaDon(list);
            ShowDialog.showDialog(stackPane,
                    "Xuất file thành công",
                    "Danh sách phiếu đặt đã được xuất ra file excel",
                    "OK");
        });
    }

    public void onThongKeAction(ActionEvent event) {
        String selectedYear = ccboxnam.getValue();
        String selectedMonth = ccboxthang.getValue();

        if (selectedYear != null && selectedMonth != null) {
            int year = Integer.parseInt(selectedYear);
            int month = Integer.parseInt(selectedMonth);
            getThongKeData(month, year);
        } else {
//            System.out.println("Vui lòng chọn năm và tháng để thống kê.");
        }
    }

    @FXML
    private void getThongKeData(int month, int year) {
        List<Double> doanhThuTheoThang = thongke.getDoanhThuTheoThang(year); // Doanh thu phòng
        Map<Integer, Double> doanhThuDichVuThang = thongke.getDoanhThuDichVuTheoThang(year); // Doanh thu dịch vụ theo tháng (Map)

        XYChart.Series<String, Number> seriesPhong = new XYChart.Series<>();
        seriesPhong.setName("Doanh thu phòng");

        XYChart.Series<String, Number> seriesDichVu = new XYChart.Series<>();
        seriesDichVu.setName("Doanh thu dịch vụ");

        CategoryAxis xAxis = (CategoryAxis) bieudo.getXAxis();
        xAxis.setLabel("Doanh thu theo tháng");
        xAxis.setTickLabelsVisible(true);
        xAxis.setTickMarkVisible(true);
        xAxis.setCategories(FXCollections.observableArrayList());

        for (int i = 1; i <= 12; i++) {
            String monthLabel = "Tháng " + i;

            double doanhThuPhong = (i <= doanhThuTheoThang.size()) ? doanhThuTheoThang.get(i - 1) : 0.0;
            seriesPhong.getData().add(new XYChart.Data<>(monthLabel, doanhThuPhong));

            double doanhThuDichVu = doanhThuDichVuThang.getOrDefault(i, 0.0);
            seriesDichVu.getData().add(new XYChart.Data<>(monthLabel, doanhThuDichVu));
            xAxis.getCategories().add(monthLabel);
        }
        bieudo.getData().clear();
        bieudo.getData().addAll(seriesPhong, seriesDichVu);
    }


}
