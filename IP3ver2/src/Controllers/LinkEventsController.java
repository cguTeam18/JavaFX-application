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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author jidev
 */
public class LinkEventsController implements Initializable {
    
    
    GetMethods get;
    PutMethods put;
    private EventModel masterEvent;
    private ObservableList<EventModel> linkableEvents = FXCollections.observableArrayList();
    
    @FXML
    TableView linkableEventsTbl;
    
    @FXML
    Label masterEventLbl;
    
    public LinkEventsController() {
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.masterEvent = ViewTimelineController.selectedEvent;
        generateLinkableEvents();
        
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn eventTitle = new TableColumn("Event Title");
        eventTitle.setCellValueFactory(new PropertyValueFactory("eventTitle"));
        eventTitle.setPrefWidth(208);
        this.linkableEventsTbl.getColumns().addAll(new Object[]{eventTitle});
        this.linkableEventsTbl.setItems(linkableEvents);
        this.masterEventLbl.setText(masterEvent.getEventTitle());
        

        this.linkableEventsTbl.getSelectionModel().setSelectionMode(
            SelectionMode.MULTIPLE
        );


    }
    
    public final void generateLinkableEvents() {
        ViewTimelineController.events.stream().filter((event) -> (!ViewTimelineController.allLinkedEvents.contains(event)) && !masterEvent.getEventId().equals(event.getEventId())).forEachOrdered((event) -> {
            addLinkableEvent(event);
        });
    }
    
    public void addLinkableEvent(EventModel event) {
        if(!linkableEvents.contains(event)) {
            linkableEvents.add(event);
        }
    }
    
    @FXML
    private void linkEvents(ActionEvent event) {
        ArrayList<EventModel> selectedEventsList = new ArrayList();
        ArrayList<String> eventTitles = new ArrayList();
        TextArea titles = new TextArea();
        
        for(int i = 0; i < this.linkableEventsTbl.getSelectionModel().getSelectedItems().size(); i++) {
            EventModel object = (EventModel) this.linkableEventsTbl.getSelectionModel().getSelectedItems().get(i);
            selectedEventsList.add(object);
        }
        if(selectedEventsList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No events have been selected to link.");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
                System.out.println("No linked events selected warning issued.");
            });       
        }
        else {
            
            selectedEventsList.forEach(obj -> eventTitles.add(obj.getEventTitle()));
        
            StringBuilder sb = new StringBuilder();
            eventTitles.forEach((eventTitle) -> {sb.append(eventTitle).append(", ");});
            String selectedTitles = sb.toString();
            titles.setText(selectedTitles);
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to link these events?" );
            alert.getDialogPane().setExpandableContent(new ScrollPane(titles));
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
                selectedEventsList.forEach((obj) -> {
                    try {
                        put.linkTimelineEvent(masterEvent.getEventId(), obj.getEventId());
                    } catch (Exception ex) {
                        Logger.getLogger(LinkEventsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                    try {
                        Parent viewRoot;
                        viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
                        Scene scene = new Scene(viewRoot);
                        IP3ver2.currentStage.setScene(scene);
                        this.masterEvent = null;
                        ViewTimelineController.selectedEvent = null;
                    } catch (IOException ex) {
                        Logger.getLogger(LinkEventsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            });
        }
    }
    
    @FXML
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot;
        viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
        this.masterEvent = null;
        ViewTimelineController.selectedEvent = null;
    }
}
