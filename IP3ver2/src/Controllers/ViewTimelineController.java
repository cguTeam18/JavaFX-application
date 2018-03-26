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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private ArrayList<EventModel> events = new ArrayList();
    
    @FXML
    AnchorPane viewPane;
    
    @FXML
    Label timelineTitleLbl;
    
    @FXML
    VBox vBoxOuter;
    
    @FXML
    HBox HBoxOuter;
    
    
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
              for(int i=0; i<events.size();i++) {
                ObservableList<EventModel> eventList = FXCollections.observableArrayList();
                eventList.add(events.get(i));
                VBox container = new VBox();
                TableView mainEvent = new TableView();
                mainEvent.setPrefSize(141, 66);
                TableColumn eventTitleCol = new TableColumn("Event Title");
                eventTitleCol.setCellValueFactory(new PropertyValueFactory("eventTitle"));
                eventTitleCol.setPrefWidth(139);
                mainEvent.getColumns().addAll(new Object[]{eventTitleCol});
                mainEvent.setItems(eventList);
                Label linkedEventsLbl = new Label("Linked Events");
                ListView linkedEvents = new ListView();
                linkedEvents.setPrefSize(141, 66);
               
                container.getChildren().addAll(mainEvent, linkedEventsLbl, linkedEvents);
                HBoxOuter.setSpacing(10);
                HBoxOuter.getChildren().add(container);
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
        timelineTitleLbl.setText(selectedTimeline.getTimelineTitle() + " Events");
    }
    
    @FXML
    private void viewTimelineRegister(ActionEvent event) throws Exception {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/TableView.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
        this.selectedTimeline = null;
    }
    
    public void getTimelineObjectLinkedEvents() throws Exception {
        events.forEach((event) -> {
            try {
                getLinkedEvents(event, event.getEventId());
            } catch (Exception ex) {
                Logger.getLogger(ViewTimelineController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void getTimelineEvents(String timelineIdVal) throws Exception {
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
                    if(!event.equals("null")) {
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
    
     public /**ObservableList<EventModel>**/void getLinkedEvents(EventModel event, String eventIdVal) throws Exception {
        String[] separatedValue = get.sendGetLinkedTimelineEvents(eventIdVal);
        
        for (String newString : separatedValue) {
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
                System.out.println(event.getEventId() + finalMasterId);
                if(event.getEventId().equals(masterEventId)){
                
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
    
}
