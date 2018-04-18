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
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author jidev
 */
public class ViewTimelineController implements Initializable {
    GetMethods get;
    PutMethods put;
    private TimelineModel selectedTimeline;
    public static EventModel selectedEvent;
    public static ArrayList<EventModel> events = new ArrayList();
    public static ArrayList<EventModel> allLinkedEvents = new ArrayList();
    
    @FXML
    AnchorPane viewPane;
    
    @FXML
    Label timelineTitleLbl;
    
    @FXML
    ScrollPane hScroll;
    
    @FXML
    AnchorPane hboxAnchor;
    
    @FXML
    HBox HBoxOuter;
    
    @FXML
    Button newEventBtn;
    
    
    public ViewTimelineController() throws Exception {
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.selectedTimeline = TableController.selectedTimeline;
        getTimelineEvents(selectedTimeline.getTimelineId());
        getTimelineObjectLinkedEvents();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
              Group root = new Group();
              Scene scene = new Scene(root, 300, 200);
              hScroll.setFitToHeight(true);
              for(int i=0; i<events.size();i++) {
                ObservableList<EventModel> eventList = FXCollections.observableArrayList();
                ObservableList<EventModel> linkedEventsList = FXCollections.observableArrayList();
                eventList.add(events.get(i));
                VBox container = new VBox();
                TableView mainEvent = new TableView();
                mainEvent.setPrefSize(300, 66);
                TableColumn eventTitleCol = new TableColumn("Event Title");
                TableColumn eventLocationCol = new TableColumn("Location");
                eventTitleCol.setCellValueFactory(new PropertyValueFactory("eventTitle"));
                eventTitleCol.setPrefWidth(95);
                eventLocationCol.setCellValueFactory(new PropertyValueFactory("location"));
                eventLocationCol.setPrefWidth(95);
                mainEvent.getColumns().addAll(new Object[]{eventTitleCol, eventLocationCol});
                TableView linkedEvents = new TableView();
                mainEvent.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
                    if(newItem != null) {
                        deselectTableViews(mainEvent);
                    }
                });
                linkedEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
                    if(newItem != null) {
                        deselectTableViews(linkedEvents);
                    }
                });
                linkedEvents.getSelectionModel().setSelectionMode(
                    SelectionMode.MULTIPLE
                );
                for(EventModel linkedEvent : events.get(i).getLinkedEvents()) {
                    linkedEventsList.add(linkedEvent);
                }
                if(!allLinkedEvents.contains(events.get(i))) {
                    linkedEvents.setPrefSize(300, 120);
                    TableColumn linkedEventTitleCol = new TableColumn("Linked Events");
                    TableColumn linkedEventLocationCol = new TableColumn("Location");
                    linkedEventTitleCol.setCellValueFactory(new PropertyValueFactory("eventTitle"));
                    linkedEventTitleCol.setPrefWidth(95);
                    linkedEventLocationCol.setCellValueFactory(new PropertyValueFactory("location"));
                    linkedEventLocationCol.setPrefWidth(95);
                    linkedEvents.getColumns().addAll(new Object[]{linkedEventTitleCol, linkedEventLocationCol});
                    linkedEvents.setItems(linkedEventsList);
                    mainEvent.setItems(eventList);
                    container.getChildren().addAll(mainEvent, linkedEvents);
                    HBoxOuter.setSpacing(10);
                    HBoxOuter.getChildren().add(container);
                }
              }
             
            /**
             * TimelineModel timeline = selectedTimeline;
             * try {
             * getTimelineEvents(timeline.getTimelineId());
             * } catch (Exception ex) {
             * Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
             * }
             * */
            //getTimelineEvents(selectedTimeline.getTimelineId());
        //} catch (Exception ex) {
            //Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //try {
            /**
             * ArrayList<EventModel> events = new ArrayList(timeline.getEvents());
             * for(EventModel event: events) {
             * 
             * try {
             * getLinkedEvents(event, event.getEventId());
             * } catch (Exception ex) {
             * Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
             * }
             * }
             * */
            //getTimelineObjectLinkedEvents();
        //} catch (Exception ex) {
            //Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
        //}
        HBoxOuter.setPrefWidth(200*events.size());
        hboxAnchor.setPrefWidth(200*events.size());
        timelineTitleLbl.setText(selectedTimeline.getTimelineTitle() + " Events");
    }
    
    public void deselectTableViews(TableView tableViewToKeep) {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            ((VBox)nodeOut).getChildren().stream().filter((nodeIn) -> (nodeIn instanceof TableView && nodeIn != tableViewToKeep)).forEachOrdered((nodeIn) -> {
                ((TableView)nodeIn).getSelectionModel().clearSelection();
            });
            }
        }
            
    }
    
    @FXML
    private void viewTimelineRegister(ActionEvent event) throws Exception {
        events.clear();
        allLinkedEvents.clear();
        selectedEvent = null;
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/TableView.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
        this.selectedTimeline = null;
    }
    
    @FXML
    private void viewAddEvent(ActionEvent event) throws Exception {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/AddEvent.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
    
    @FXML
    private void viewEditEvent(ActionEvent event) throws Exception {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            for(Node nodeIn:((VBox)nodeOut).getChildren()) {
                if(nodeIn instanceof TableView || nodeIn instanceof ListView) {
                    EventModel object = (EventModel)((TableView) nodeIn).getSelectionModel().getSelectedItems().get(0);
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                    if(IP3ver2.events.contains(object)){
                        selectedEvent = object;
                        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/EditEventFromTimeline.fxml"));
                        Scene scene = new Scene(viewRoot);
                        IP3ver2.currentStage.setScene(scene);
                    }
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                }
            }
        }
        }
    }
    
    @FXML
    private void viewEvent(ActionEvent event) throws Exception {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            for(Node nodeIn:((VBox)nodeOut).getChildren()) {
                if(nodeIn instanceof TableView) {
                    EventModel object = (EventModel)((TableView) nodeIn).getSelectionModel().getSelectedItems().get(0);
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                    if(IP3ver2.events.contains(object)){
                        selectedEvent = object;
                        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewEvent.fxml"));
                        Scene scene = new Scene(viewRoot);
                        IP3ver2.currentStage.setScene(scene);
                    }
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                }
            }
        }
        }
        
    }
    
    @FXML
    private void deleteEventFromTimeline(ActionEvent event) throws Exception {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            for(Node nodeIn:((VBox)nodeOut).getChildren()) {
                if(nodeIn instanceof TableView || nodeIn instanceof ListView) {
                    EventModel object = (EventModel)((TableView) nodeIn).getSelectionModel().getSelectedItems().get(0);
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                    if(IP3ver2.events.contains(object)){
                        String eventId = object.getEventId();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?");
                        alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> {
                            try {
                                deleteEventObject(eventId);
                                Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
                                Scene scene = new Scene(viewRoot);
                                IP3ver2.currentStage.setScene(scene);
                            } catch (Exception ex) {
                                Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            }
        }
        }
    }
    
    @FXML
    private void deleteEventAndLinkedEvents(ActionEvent event) throws Exception {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            for(Node nodeIn:((VBox)nodeOut).getChildren()) {
                if(nodeIn instanceof TableView || nodeIn instanceof ListView) {
                    EventModel object = (EventModel)((TableView) nodeIn).getSelectionModel().getSelectedItems().get(0);
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                    if(IP3ver2.events.contains(object)){
                        String eventId = object.getEventId();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?");
                        alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> {
                            try {
                                for(EventModel linkedEvent : object.getLinkedEvents()) {
                                    put.unLinkTimelineEvent(object.getEventId(), linkedEvent.getEventId());
                                    allLinkedEvents.remove(linkedEvent);
                                    deleteEventObject(linkedEvent.getEventId());
                                }
                                deleteEventObject(eventId);
                                Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
                                Scene scene = new Scene(viewRoot);
                                IP3ver2.currentStage.setScene(scene);
                            } catch (Exception ex) {
                                Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            }
        }
        }
    }
    
    @FXML
    private void linkEvent(ActionEvent event) throws Exception {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            for(Node nodeIn:((VBox)nodeOut).getChildren()) {
                if(nodeIn instanceof TableView || nodeIn instanceof ListView) {
                    EventModel object = (EventModel)((TableView) nodeIn).getSelectionModel().getSelectedItems().get(0);
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                    if(IP3ver2.events.contains(object)){
                        if(!allLinkedEvents.contains(object)) {
                            try{
                                selectedEvent = object;
                                Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/LinkEvents.fxml"));
                                Scene scene = new Scene(viewRoot);
                                IP3ver2.currentStage.setScene(scene);
                                }
                            catch(Exception ex) {
                                Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @FXML
    private void unlinkEvent(ActionEvent event) throws Exception {
        for(int i = 0; i<this.HBoxOuter.getChildren().size(); i++) {
        Node nodeOut = this.HBoxOuter.getChildren().get(i);
        if(nodeOut instanceof VBox) {
            for(Node nodeIn:((VBox)nodeOut).getChildren()) {
                if(nodeIn instanceof TableView || nodeIn instanceof ListView) {
                    EventModel object = (EventModel)((TableView) nodeIn).getSelectionModel().getSelectedItems().get(0);
                    ((TableView) nodeIn).getSelectionModel().clearSelection();
                    if(IP3ver2.events.contains(object)){
                        if(allLinkedEvents.contains(object)) {
                            for(int j = 0; j < events.size(); j++) {
                                if(events.get(j).getLinkedEvents().contains(object)) {
                                    System.out.println(events.get(j).getEventId() + " " + object.getEventId());
                                    put.unLinkTimelineEvent(events.get(j).getEventId(), object.getEventId());
                                    events.get(j).removeLinkedEvent(object);
                                    allLinkedEvents.remove(object);
                                    try{
                                        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
                                        Scene scene = new Scene(viewRoot);
                                        IP3ver2.currentStage.setScene(scene);
                                    }
                                    catch(IOException ex) {
                                        Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Selected event is a master event. Please select an event linked to a master event.");
                            alert.showAndWait()
                            .filter(response -> response == ButtonType.OK)
                            .ifPresent(response -> {System.out.println("No event links present warning issued");});
                            ((TableView) nodeIn).getSelectionModel().clearSelection();
                        }
                    }
                }
            }
        }
        }
    }
    
    
    public void deleteEvent(EventModel event) throws Exception {
        if(IP3ver2.events.contains(event)){
            IP3ver2.events.remove(event);
            events.remove(event);
            allLinkedEvents.remove(event);
            getTimelineEvents(selectedTimeline.getTimelineId());
            getTimelineObjectLinkedEvents();
        }
    }
    
    public void deleteEventObject(String eventId) throws Exception {
        EventModel event = getEvent(eventId);
        if(event != null) {
            put.unlinkEvent(this.selectedTimeline.getTimelineId(), event.getEventId());
            put.deleteTimelineEvent(event.getEventId());
            deleteEvent(event);
        }
        else{
            System.out.println("Event does not exist");
        }
    }
    
    public final void getTimelineObjectLinkedEvents() throws Exception {
        events.forEach((event) -> {
            try {
                getLinkedEvents(event, event.getEventId());
            } catch (Exception ex) {
                Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public final void getTimelineEvents(String timelineIdVal) throws Exception {
            EventModel event;
            String[] separatedValue = get.sendGetLinkedEvents(timelineIdVal);
            
        for(int i =0; i<separatedValue.length; i++) {
            String newString = separatedValue[i];
            String finalNewString = newString.replaceAll("\"","");
          
           try{
            Scanner readString = new Scanner(finalNewString);
            readString.useDelimiter(",");
                    
            String id;
                    
            id = readString.next();
            readString.close();
            
            String finalId = id.replaceAll("\\s+", "");
            finalId = finalId.replaceAll("\\{", "");
            finalId = finalId.replaceAll("\\[","");
            finalId = finalId.replaceAll("\\]", "");
            finalId = finalId.replace("TimelineEvent","");
            finalId = finalId.replace("Id:","");
            
            System.out.println(finalId);
                try{
                    event = getEvent(finalId);
                    if(event != null) {
                        System.out.println(event);
                        addEvent(event);
                    }
                }
                catch(NullPointerException n) {
                    System.out.println("Event does not exist");
                }
            }
           catch(NoSuchElementException e){
               System.out.println(e);
           }
            }
        System.out.println(events.toString());
        }
    
     public /**ObservableList<EventModel>**/void getLinkedEvents(EventModel masterEvent, String eventIdVal) throws Exception {
        String[] separatedValue = get.sendGetLinkedTimelineEvents(eventIdVal);
        EventModel event;
        ArrayList<EventModel> linkedEventList = new ArrayList();
        
        for(int i =0; i<separatedValue.length; i++) {
            String newString = separatedValue[i];
            String finalNewString = newString.replaceAll("\"","");
            
            try (Scanner readString = new Scanner(finalNewString)) {
                readString.useDelimiter(",");
                String masterEventId;
                String linkedEventId;
                masterEventId = readString.next();
                linkedEventId = readString.next();
                readString.close();
                String finalMasterId = masterEventId.replaceAll("\\s+", "");
                finalMasterId = finalMasterId.replaceAll("\\{", "");
                finalMasterId = finalMasterId.replaceAll("\\[","");
                finalMasterId = finalMasterId.replaceAll("\\]", "");
                finalMasterId = finalMasterId.replace("TimelineEvent","");
                finalMasterId = finalMasterId.replace("Id:","");
                
                String finalLinkedEventId = linkedEventId.replaceAll("\\s+", "");
                finalLinkedEventId = finalLinkedEventId.replace("LinkedToTimelineEvent","");
                finalLinkedEventId = finalLinkedEventId.replace("Id:","");
                System.out.println(finalLinkedEventId);
                try{
                    event = getEvent(finalLinkedEventId);
                    if(event != null) {
                        System.out.println(event);
                        masterEvent.addLinkedEvent(event);
                        addLinkedEvent(event);
                    }
                }
                catch(NullPointerException e){
                    System.out.println("Linked event does not exist");
                }
            }
            catch(NoSuchElementException ex){
                System.out.println("No linked events exist for this event object");
            }
        }
    }
    
    public EventModel getEvent(String id) {
        for(EventModel event:IP3ver2.events) {
            if(event.getEventId().equals(id)) {
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
    
    public void addLinkedEvent(EventModel event) {
        if(!allLinkedEvents.contains(event)) {
            allLinkedEvents.add(event);
        }
    }
    
}
