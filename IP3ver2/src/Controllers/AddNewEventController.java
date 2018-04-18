/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.EventModel;
import Models.TimelineModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import ip3ver2.IP3ver2;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jidev
 */
public class AddNewEventController implements Initializable {
    
    private EventModel event;
    private GetMethods get = new GetMethods();
    private PutMethods put = new PutMethods();
    private TimelineModel selectedTimeline;
    private LocalDate placeHolder = LocalDate.now();
    
    @FXML
    TextField titleField;
    
    @FXML
    TextArea descField;
    
    @FXML
    DatePicker dateField;
    
    @FXML
    TextField timeField;
    
    @FXML
    WebView locationField;
    
    @FXML
    Label lblNewTime;
    private WebEngine webEngine;
    
    public AddNewEventController() {
        EventModel event = new EventModel();
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.event = event;
        this.selectedTimeline = TableController.selectedTimeline;
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNewTime.setText(selectedTimeline.getTimelineTitle());
        dateField.setDayCellFactory(picker -> new DateCell() {
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.compareTo(placeHolder) > 0);
                }
        });
        dateField.setValue(this.placeHolder);
        this.webEngine = locationField.getEngine();
        final URL urlGoogleMaps = getClass().getResource("/HTML/GoogleMapsNew.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setUserStyleSheetLocation(getClass().getResource("/HTML/mapsStyleSheet.css").toExternalForm());
    }
    
    @FXML
    private String getEventTitle() {
        String eventTitleStr = null;
        if(!titleField.getText().equals("")) {
            eventTitleStr = titleField.getText();
            return eventTitleStr;
        }
        else {
            eventTitleStr = "";
            Alert alert = new Alert(Alert.AlertType.WARNING, "No title has been entered!");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.out.println("Null title box warning issued: "));
            System.out.println("Title has not been entered");
        }
        return eventTitleStr;
    }
    
    @FXML
    private String getEventDescription() {
        String eventDescriptionStr = null;
        if(!descField.getText().equals("")) {
            eventDescriptionStr = descField.getText();
            return eventDescriptionStr;
        }
        else {
            eventDescriptionStr = "";
        }
        return eventDescriptionStr;
    }
    
    @FXML
    private String getEventDate() {
        Date eventDate = null;
        String eventDateStr = null;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        LocalDate selectedDate = dateField.getValue();
        eventDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        eventDateStr = df.format(eventDate);
        return eventDateStr;
    }
    
    @FXML
    private String getEventTime() {
        String eventTimeStr = null;
        if(!timeField.getText().equals("")) {
            eventTimeStr = timeField.getText();
            Pattern p = Pattern.compile("^\\d{1,2}:\\d{1,2}$");
            if(!p.matcher(eventTimeStr).matches()) {
                eventTimeStr = "";
                Alert alert = new Alert(Alert.AlertType.WARNING, "Not a valid timestamp, please enter the time as HH:mm.");
                alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println("Invalid time warning issued: "));
                System.out.println("Time has not been entered");
            }
        }
        else {
            eventTimeStr = "";
        }
        return eventTimeStr;
    }
    
    public String getAddress() {
        String location = "";
        Document doc = webEngine.getDocument();
        Element check = doc.getElementById("confirmedAddress");
        Element locationEl = doc.getElementById("address");
        HTMLInputElement checkValue = (HTMLInputElement) check;
        HTMLInputElement locationValue = (HTMLInputElement) locationEl;
        String checkBool = checkValue.getValue();
        if(checkBool.equals("OK")) {
            location = locationValue.getValue();
        }
        System.out.println(location + " " + checkBool);
        return location;
    }
    
    @FXML
    public void findLocation() {
        this.webEngine.executeScript("codeAddress()");
    }
    
    
    @FXML
    private void addNewEvent(ActionEvent event) throws ParseException, Exception {
        String eventTitle = getEventTitle().trim();
        String eventDate = getEventDate().trim();
        String eventTime = getEventTime().trim();
        String eventDescription = getEventDescription();
        String location = getAddress();
        if(eventTitle.equals("")||eventDate.equals("")||eventTime.equals("")||eventDescription.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "One or more fields have been left blank."
                +"Please fill any remaining fields and try again.");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.out.println("Null field warning issued: "));
            System.out.println("A field has not been entered");
        }
        else{
            this.event.setEventTitle(eventTitle);
            this.event.setEventDescription(eventDescription);
            String eventDateTimeStr = eventDate.concat(" "+eventTime);
            this.event.setDate(eventDateTimeStr);
            this.event.setDateString(eventDateTimeStr);
            if(!location.equals("")){
                this.event.setEventlocation(location);
            }
            IP3ver2.events.add(this.event);
            put.createEvent(this.event.getEventId(), this.event.getEventTitle(), this.event.getEventDescription(), this.event.getDateString(), this.event.getLocation());
            System.out.println(selectedTimeline.getTimelineId() + this.event.getEventId());
            put.linkEvent(selectedTimeline.getTimelineId(), this.event.getEventId());
            Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
            Scene scene = new Scene(viewRoot);
            IP3ver2.currentStage.setScene(scene);
        }
        
    }
    
    @FXML 
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
    
}
