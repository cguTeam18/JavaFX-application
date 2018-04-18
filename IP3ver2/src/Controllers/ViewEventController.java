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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jidev
 */
public class ViewEventController implements Initializable {
    GetMethods get;
    PutMethods put;
    public static EventModel selectedEvent;
    public EventModel constEvent;
    private EventModel event;
    private final ArrayList<EventModel> linkedEvents;
    private static int bookmark = -1;
    
    @FXML
    Label eventTitleLbl;
    
    @FXML
    TextArea dateArea;
    
    @FXML
    TextArea descArea;
    
    @FXML
    WebView locationArea;
    
    @FXML
    Button nextEventBtn;
    
    @FXML
    Button prevEventBtn;
    private WebEngine webEngine;
    
    public ViewEventController() {
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.constEvent = ViewTimelineController.selectedEvent;
        this.linkedEvents = constEvent.getLinkedEvents();
        if(!linkedEvents.isEmpty() && bookmark != -1) {
            this.event = linkedEvents.get(bookmark);
        }
        else {
            this.event = ViewTimelineController.selectedEvent;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.webEngine = locationArea.getEngine();
        final URL urlGoogleMaps = getClass().getResource("/HTML/GoogleMapsNew.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setUserStyleSheetLocation(getClass().getResource("/HTML/mapsStyleSheet.css").toExternalForm());
        String enterLocation = "document.getElementById('address').value='" + event.getLocation()+"';";
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
        if ( newState == Worker.State.SUCCEEDED ) {
                webEngine.executeScript(enterLocation);
                findLocation();
                }
        });
        setTitle();
        setDesc();
        setDateTime();
        //setLocation();
        if(bookmark < linkedEvents.size() && !linkedEvents.isEmpty()) {
            nextEventBtn.setVisible(true);
            prevEventBtn.setVisible(true);
        }
        if(bookmark == -1 && !linkedEvents.isEmpty()) {
            nextEventBtn.setVisible(true);
            prevEventBtn.setVisible(false);
        }
        if(bookmark == linkedEvents.size()-1 && !linkedEvents.isEmpty()){
            nextEventBtn.setVisible(false);
            prevEventBtn.setVisible(true);
        }
        if(linkedEvents.isEmpty()) {
            nextEventBtn.setVisible(false);
            prevEventBtn.setVisible(false);
        }
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
    public void findLocation() {
        this.webEngine.executeScript("codeAddress()");
    }
    
    @FXML
    private void editEvent(ActionEvent event) throws IOException {
        selectedEvent = this.event;
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/EditEventFromEvent.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
    
    @FXML 
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
        this.event = null;
        ViewTimelineController.selectedEvent = null;
    }
    
    @FXML
    private void nextLinkedEvent(ActionEvent event) throws IOException {
        //ViewTimelineController.selectedEvent = linkedEvents.get(bookmark);
        bookmark++;
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewEvent.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
    
    @FXML
    private void prevLinkedEvent(ActionEvent event) throws IOException {
        //if(bookmark == linkedEvents.size()) {
          //  bookmark--;
            //ViewTimelineController.selectedEvent = constEvent;
        //}
        //else {
          //  ViewTimelineController.selectedEvent = linkedEvents.get(bookmark);
            bookmark--;
        //}
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewEvent.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
        
    }
}
