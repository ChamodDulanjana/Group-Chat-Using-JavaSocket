package com.lk.ijse.chatApp.client.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class LoggingFormController{
    public JFXTextField txtName;

    public void btnEnterOnAction(ActionEvent actionEvent) {
        if (txtName.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Name is empty !", ButtonType.OK).show();
            txtName.setFocusColor(Paint.valueOf("Red"));
            txtName.requestFocus();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/ChatWindow.fxml"));
        Parent load = null;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatWindowController controller = fxmlLoader.getController();
        controller.setUserName(txtName.getText());

        Stage stage = new Stage();
        stage.setTitle("Chat App");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.show();
        txtName.clear();
    }
}
