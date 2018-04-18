/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import ip3ver2.IP3ver2;
import Models.EventModel;
import Models.TimelineModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

/**
 * FXML Controller class
 *
 * @author jidev
 */
public class EditEventControllerFromTimeline implements Initializable {

    private EventModel event;
    private GetMethods get = new GetMethods();
    private PutMethods put = new PutMethods();
    private TimelineModel selectedTimeline;
    
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
    
    public EditEventControllerFromTimeline() throws ParseException {
        EventModel event = ViewTimelineController.selectedEvent;
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.event = event;
        this.selectedTimeline = TableController.selectedTimeline;
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.webEngine = locationField.getEngine();
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
        lblNewTime.setText(selectedTimeline.getTimelineTitle());
        setTitle();
        setDesc();
        try {
            setDate();
        } catch (ParseException ex) {
            Logger.getLogger(EditEventControllerFromTimeline.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTime();
    }    
    
    @FXML
    private String getTitle() {
        String eventTitleStr = titleField.getText();
        return eventTitleStr;
    }
    
    @FXML
    private String getDesc() {
        String eventDescStr = descField.getText();
        return eventDescStr;
    }
    
    @FXML
    private String getDate() {
        Date eventDate;
        String eventDateStr;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        LocalDate selectedDate = dateField.getValue();
        eventDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        eventDateStr = df.format(eventDate);
        return eventDateStr;
    }
    
    @FXML
    private String getTime() {
        String eventTimeStr = timeField.getText();
        Pattern p = Pattern.compile("^\\d{1,2}:\\d{1,2}$");
        if(!p.matcher(eventTimeStr).matches()) {
            eventTimeStr = "";
            Alert alert = new Alert(Alert.AlertType.WARNING, "Not a valid timestamp, please enter the time as HH:mm.");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.out.println("Invalid time warning issued: "));
            System.out.println("Time has not been entered");
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
    private void setTitle() {
        this.titleField.setText(this.event.getEventTitle());
    }
    
    @FXML
    private void setDesc() {
        this.descField.setText(this.event.getEventDescription().trim());
    }
    
    @FXML
    private void setDate() throws ParseException {
        String eventDateStr = this.event.getDateString();
        String eventDateOnly = eventDateStr.substring(0, 10);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = df.parse(eventDateOnly);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dateField.setValue(localDate);
    }
    
    @FXML
    private void setTime() {
        String eventDateStr = this.event.getDateString();
        String eventTimeOnly = eventDateStr.substring(11);
        this.timeField.setText(eventTimeOnly);
    }
    
    @FXML
    public void findLocation() {
        this.webEngine.executeScript("codeAddress()");
    }
    
    @FXML
    private void editEvent() throws ParseException, Exception {
    
        String eventIdKey = "TimelineEventId";
        String titleKey = "Title";
        String descriptionKey = "Description";
        String locationKey = "Location";
        String eventDateTimeKey = "EventDateTime";
        String eventTitle = getTitle().trim();
        String eventDate = getDate().trim();
        String eventTime = getTime().trim();
        String eventDescription = getDesc();
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
            
            if(IP3ver2.events.contains(this.event)) {
                IP3ver2.events.remove(this.event);
                IP3ver2.events.add(this.event);
                put.editEventTitle(eventIdKey, this.event.getEventId(), titleKey, this.event.getEventTitle());
                put.editDescription(eventIdKey, this.event.getEventId(), descriptionKey, this.event.getEventDescription());
                put.editEventDateTime(eventIdKey, this.event.getEventId(), eventDateTimeKey, this.event.getDateString());
                put.editLocation(eventIdKey, this.event.getEventId(), locationKey, this.event.getLocation());
                Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
                Scene scene = new Scene(viewRoot);
                IP3ver2.currentStage.setScene(scene);
            }
        }
    }
    
    @FXML 
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
}
