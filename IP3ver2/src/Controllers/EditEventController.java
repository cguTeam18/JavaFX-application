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

/**
 * FXML Controller class
 *
 * @author jidev
 */
public class EditEventController implements Initializable {

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
    TextArea locationField;
    
    @FXML
    Label lblNewTime;
    
    public EditEventController() throws ParseException {
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
        lblNewTime.setText(selectedTimeline.getTimelineTitle());
        setTitle();
        setDesc();
        try {
            setDate();
        } catch (ParseException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTime();
        setLocation();
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
    private void setLocation() {
        this.locationField.setText(this.event.getLocation().trim());
    }
    
    @FXML
    private void editEvent() throws ParseException, Exception {
    
        String eventIdKey = "TimelineEventId";
        String titleKey = "Title";
        String descriptionKey = "Description";
        String locationKey = "Location";
        String eventDateTimeKey = "EventDateTime";
        String eventTitle = getTitle();
        String eventDate = getDate();
        String eventTime = getTime();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String eventDescription = getDesc();
        if(!eventTitle.equals("")&&!eventDate.equals("")&&!eventTime.equals("")&&!eventDescription.equals("")) {
            this.event.setEventTitle(eventTitle);
            this.event.setEventDescription(eventDescription);
            String eventDateTimeStr = eventDate.concat(" "+eventTime);
            this.event.setDate(eventDateTimeStr);
            this.event.setDateString(eventDateTimeStr);
            
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
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "One or more fields have been erased. Please fill in these fields before continuing.");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.out.println("Null field warning issued: "));
            System.out.println("One or more fields erased");
        }
    }
    
    @FXML 
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
}
