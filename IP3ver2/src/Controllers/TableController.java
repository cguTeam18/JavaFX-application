/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

/**
 *
 * @author jidev
 */
import Models.TimelineModel;
import Models.EventModel;
import Models.AllTimelines;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableController implements Initializable {

    GetMethods get;
    PutMethods put;
    ObservableList<TimelineModel> timelines = FXCollections.observableArrayList();
    ObservableList<EventModel> events = FXCollections.observableArrayList();
    
    @FXML
    TableView tableview;

    public TableController() {
        get = new GetMethods();
        put = new PutMethods();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        

    }

    public void initialize(URL url, ResourceBundle rb) {
        TableColumn timelineIdCol = new TableColumn ("Timeline ID");
        TableColumn timelineTitleCol = new TableColumn("Timeline Title");
        TableColumn dateCreatedCol = new TableColumn("Date Created");
        try {
            createTimelineObjects();
        } catch (Exception ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        }

        timelineIdCol.setCellValueFactory(new PropertyValueFactory("timelineId"));
        timelineTitleCol.setCellValueFactory(new PropertyValueFactory("timelineTitle"));
        dateCreatedCol.setCellValueFactory(new PropertyValueFactory("dateCreatedString"));
        
        this.tableview.getColumns().addAll(new Object[]{timelineIdCol, timelineTitleCol, dateCreatedCol});

        this.tableview.setItems(timelines);
    }

    @FXML
    private void deleteRowFromTable(ActionEvent event) throws Exception {
        TimelineModel object = (TimelineModel) this.tableview.getSelectionModel().getSelectedItems().get(0);
        String timelineId = object.getTimelineId();
        deleteTimelineObject(timelineId);
        //this.tableview.getItems().removeAll(new Object[]{this.tableview.getSelectionModel().getSelectedItem()});
        
        //deleteTimelineObject
        

    }
    public void createTimelineObjects() throws Exception {
        
        String[] separatedValue = get.sendGetAllTimelines();
        for(int i =0; i<separatedValue.length-1; i++) {
            
                    String newString = separatedValue[i];
                    String finalNewString = newString.replaceAll("\"","");
                    
                    Scanner readString = new Scanner(finalNewString);
                    readString.useDelimiter(",");
                    String title;
                    String timestamp;
                    String id;
                    title = readString.next();
                    timestamp = readString.next();
                    readString.next();
                    id = readString.next();
                    readString.next();
                    readString.close();
                    
                    String finalTitle = title.replace("Title:", "");
                    finalTitle = finalTitle.replaceAll("\\{", "");
                    finalTitle = finalTitle.replaceAll("\\s+", "");
                    finalTitle = finalTitle.replaceAll("\\[","");
                    
                    String finalTimestamp = timestamp.replace("CreationTimeStamp:","");
                    finalTimestamp = finalTimestamp.replaceAll("\\s+","");
                    Long longTimestamp = Long.parseLong(finalTimestamp);
                    
                    String finalId = id.replace("Id:", "");
                    finalId = finalId.replaceAll("\\s+", "");
                    System.out.println(finalId + finalTitle + longTimestamp);
                    TimelineModel timeline = new TimelineModel(finalId, finalTitle, longTimestamp);
                    timelines.add(timeline);    
                }
    }
    
    public TimelineModel getTimeline(String timelineId) {
        for(TimelineModel timeline:this.timelines) {
            if(timeline.getTimelineId().equals(timelineId)){
                return timeline;
            }
        }
        return null;
    }
    
    public void addTimeline(TimelineModel timeline) {
        if(!timelines.contains(timeline)){
            timelines.add(timeline);
        }
    }
    
    public void deleteTimeline(TimelineModel timeline) {
        if(timelines.contains(timeline)){
            timelines.remove(timeline);
        }
    }
    
    public EventModel getEvent(String eventId) {
        for(EventModel event:events) {
            if(eventId.equals(event.getEventId())) {
                return event;
            }
        }
        return null;
    }
    
    public void addEvent(EventModel event) {
        if(!events.contains(event)) {
            events.add(event);
        }
    }
    
    public void deleteEvent(EventModel event) {
        if(events.contains(event)){
            events.remove(event);
        }
    }
    
    
     public void addTimelineObject(String timelineTitle) throws ParseException, Exception{
        TimelineModel timeline = new TimelineModel(timelineTitle);
        addTimeline(timeline);
        put.createTimeline(timeline.getTimelineId(), timeline.getTimelineTitle());
    }
    
    public void addEventObject(String eventTitle, String eventDescription, String dateString, String location) throws ParseException, Exception {
        EventModel event = new EventModel(eventTitle, eventDescription, dateString, location);
        addEvent(event);
        put.createEvent(event.getEventId(),event.getEventTitle(), event.getEventDescription(), event.getDateString(), event.getLocation());
    }
    
    public void deleteTimelineObject(String timelineId) throws Exception {
        TimelineModel timeline = getTimeline(timelineId);
        if(timeline != null) {
            put.deleteTimeline(timeline.getTimelineId());
            deleteTimeline(timeline);
        }
        else{
            System.out.println("Timeline does not exist");
        }
    }
    
    public void editTimelineTitle(String timelineId, String newTitle) throws Exception {
       TimelineModel timeline = getTimeline(timelineId);
       if(timeline != null) {
           timeline.setTimelineTitle(newTitle);
           put.editTimelineTitle(timelineId, newTitle);
       }
       else{
           System.out.println("Timeline does not exist");
       }
   }

}
