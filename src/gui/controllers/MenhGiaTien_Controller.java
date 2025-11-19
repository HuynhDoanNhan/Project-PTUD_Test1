package gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.ChiTietKiemTien;
import dto.PhieuKiemTien;
import dto.enum_class.MenhGia;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.NumberStringConverter;
import utils.NumberFormat;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class MenhGiaTien_Controller {
    @FXML
    private FontAwesomeIconView giamSL;

    @FXML
    private Label menhGia;

    @FXML
    private TextField sl;

    @FXML
    private Label stt;

    @FXML
    private FontAwesomeIconView tangSL;

    @FXML
    private Label tongTien;

    @FXML
    private AnchorPane root;

    private final IntegerProperty soLuongValue = new SimpleIntegerProperty(0);
    private final DoubleProperty tongTienValue = new SimpleDoubleProperty(0);
    private final StringProperty tongTienFormatted = new SimpleStringProperty("0 VND");

    public void initialize() {
        // TODO

    }

    public MenhGiaTien_Controller(double menhGia, int stt) {
        // TODO
        loadFXML();
        this.menhGia.setText(NumberFormat.currencyFormat(menhGia));
        this.stt.setText(String.valueOf(stt));


        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();
            if (text.matches("\\d*")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        sl.setTextFormatter(textFormatter);
        sl.textProperty().bindBidirectional(soLuongValue, new NumberStringConverter());

        tongTienValue.bind(soLuongValue.multiply(menhGia));
        tongTienFormatted.bind(Bindings.createStringBinding(() ->
                NumberFormat.currencyFormat(tongTienValue.get()), tongTienValue));
        tongTien.textProperty().bind(tongTienFormatted);

        giamSL.setOnMouseClicked(event -> {
            int value = Integer.parseInt(sl.getText());
            if (value > 0) {
                sl.setText(String.valueOf(value - 1));
            }
        });

        tangSL.setOnMouseClicked(event -> {
            int value = Integer.parseInt(sl.getText());
            sl.setText(String.valueOf(value + 1));
        });
    }

    public AnchorPane getRoot() {
        return root;
    }

    public double getTongTien() {
        return tongTienValue.get();
    }

    public ChiTietKiemTien getChiTietKiemTien(PhieuKiemTien phieuKiemTien) throws Exception {
        double menhGiaValue = NumberFormat.currencyUnFormat(menhGia.getText());
        int soLuong = soLuongValue.get();
        if (soLuong == 0) {
            return null;
        }
        for (MenhGia menhGiaEnum : MenhGia.values()) {
            if (menhGiaEnum.getGiaTri() == menhGiaValue) {
                return new ChiTietKiemTien(phieuKiemTien, soLuongValue.get(), menhGiaEnum);
            }
        }
        return null;
    }

    public DoubleProperty getTongTienProperty() {
        return tongTienValue;
    }
    
    public void resetSoLuong() {
    	sl.setText(String.valueOf(0));
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/MenhGia_view.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed");
        }
    }
}
