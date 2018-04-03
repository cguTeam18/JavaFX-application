/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.EventModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import ip3ver2.IP3ver2;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author jidev
 */
public class ViewEventController implements Initializable {
    GetMethods get;
    PutMethods put;
    private EventModel event;
    
    @FXML
    Label eventTitleLbl;
    
    @FXML
    TextArea dateArea;
    
    @FXML
    TextArea descArea;
    
    @FXML
    TextArea locationArea;
    
    public ViewEventController() {
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.event = ViewTimelineController.selectedEvent;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTitle();
        setDesc();
        setDateTime();
        setLocation();
    }
    
    @FXML
    private void setTitle() {
        eventTitleLbl.setText(event.getEventTitle());
    }
    
    @FXML
    private void setDesc() {
        descArea.setText(event.getEventDescription());
    }
    
    @FXML
    private void setDateTime() {
        dateArea.setText(event.getDateString());
    }
    
    @FXML
    private void setLocation() {
        locationArea.setText(event.getLocation());
    }
    
    @FXML 
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
        this.event = null;
        ViewTimelineController.selectedEvent = null;
    }
}
