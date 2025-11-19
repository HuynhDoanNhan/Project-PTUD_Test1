package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dto.ChiTietDichVu;
import dto.Phong;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class ButtonPhongDatDichVuView_Controller implements Initializable {
	 
	private ArrayList<ChiTietDichVu> listChiTietDichVus;
	private Phong phong;
	
	private AnchorPane root;
	@FXML
	private JFXButton button;
	
	private Boolean isSelected;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public ButtonPhongDatDichVuView_Controller(Phong phong) {
		this.phong = phong;
		loadFXML(phong);
		listChiTietDichVus = new ArrayList<ChiTietDichVu>();
	}
	
	 private void loadFXML(Phong phong) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/ButtonPhong_DatDichVu_View.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
            root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/DatDichVu_CSS.css")).toExternalForm());
            button.setText(phong.getMaPhong());
            button.getStyleClass().addAll("lbMain", "lbFontBold", "btnPhongUnSelected");
            isSelected = false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load ButtonPhong_DatDichVu_View.fxml");
        }
    }
	 
	 private void updateSelected() {
		 button.getStyleClass().removeAll("btnPhongUnSelected", "btnPhongSelected");
		 if (isSelected) {
			 button.getStyleClass().add("btnPhongSelected");
		 } else {
			 button.getStyleClass().add("btnPhongUnSelected");
		 }
	 }
	 
	 public void setSelected(boolean bool) {
		 isSelected = bool;
		 updateSelected();
	 }
	 
	 public boolean isSelected() {
		 return isSelected;
	 }
	
	public JFXButton getButton() {
		return button;
	}
	
	public Phong getPhong() {
		return phong;
	}
	
	public ArrayList<ChiTietDichVu> getListChiTietDichVu() {
		return listChiTietDichVus;
	}
	
	public void setListChiTietDichVu(ArrayList<ChiTietDichVu> liChiTietDichVus) {
		this.listChiTietDichVus = liChiTietDichVus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(phong);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ButtonPhongDatDichVuView_Controller other = (ButtonPhongDatDichVuView_Controller) obj;
		return Objects.equals(phong, other.phong);
	}
}
