/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.EventAttachmentModel;
import Models.EventModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import ip3ver2.IP3ver2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private final ArrayList<EventAttachmentModel> eventAttachments;
    private File eventAttachment = null;
    private static int bookmark = -1;
    private HostServices hostServices ;
    
    @FXML
    Label eventTitleLbl;
    
    @FXML
    TextArea dateArea;
    
    @FXML
    TextArea descArea;
    
    @FXML
    WebView locationArea;
    
    @FXML
    VBox root;
    
    @FXML
    Button btnViewAttachment;
    
    @FXML
    Button nextEventBtn;
    
    @FXML
    Button prevEventBtn;
    private WebEngine webEngine;
    
    public ViewEventController() throws Exception {
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.constEvent = ViewTimelineController.selectedEvent;
        this.linkedEvents = constEvent.getLinkedEvents();
        this.eventAttachments = new ArrayList();
        setHostServices();
        if(!linkedEvents.isEmpty() && bookmark != -1) {
            this.event = linkedEvents.get(bookmark);
        }
        else {
            this.event = ViewTimelineController.selectedEvent;
        }
        getAllAttachments();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        root.setPadding(new Insets(10));
        root.setSpacing(5);
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
        setAttachment();
        if(eventAttachment == null) {
            btnViewAttachment.setVisible(false);
        }
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

    public HostServices getHostServices() {
        return hostServices ;
    }

    public void setHostServices() {
        this.hostServices = IP3ver2.services;
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
    public void setAttachment() {
        eventAttachments.forEach((attach) -> {
            try {
                downloadAttachment(attach.getTitle());
            } catch (Exception ex) {
                Logger.getLogger(ViewEventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        if(eventAttachment != null)// if file is selected, display file
        {
            Text t = new Text();
            String filename = eventAttachment.getName();

        
            String ext = filename.substring(filename.lastIndexOf(".") +1, filename.length());
            if(ext.equals("doc")) {
                t.setText(filename); // Display Filename
                root.getChildren().add(t); // Add filename to screen
            }

            Image image  = new Image(eventAttachment.toURI().toString()); // Create new image
            ImageView iv = new ImageView(image); // Create image view
            iv.setFitHeight(100); // Set image height
            iv.setFitWidth(100); // Set image width
            root.getChildren().add(iv);
        }
    }
    
    public void downloadAttachment(String filename) throws Exception {
        
        String root = System.getProperty("user.dir");
        File downloadedFile = null;
                
            URL responseURL = get.sendGetDownloadURL(filename); // 2. Assign the response from that call to a URL data type
        
            HttpURLConnection URLconnection = (HttpURLConnection)responseURL.openConnection();
            //URLconnection.connect();
            int lengthOfFile = URLconnection.getContentLength();
        
            InputStream input = URLconnection.getInputStream();
            OutputStream writeToFile = new FileOutputStream(root + "/" + filename);
        
            byte dataStream[] = new byte[4096];
            int count;
            long total = 0;
            while((count = input.read(dataStream)) != -1) {
                total +=count;
                writeToFile.write(dataStream, 0, count);
            }
            writeToFile.flush();
            writeToFile.close();
            input.close();
            downloadedFile = new File(root + "/" + filename);
            this.eventAttachment = downloadedFile;
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
    
    public void getAllAttachments() throws Exception {
        
        String[] separatedValue = get.sendGetAllAttachments(this.event.getEventId());
        for(int i =0; i<separatedValue.length; i++) {
            
                    String newString = separatedValue[i];
                    String finalNewString = newString.replaceAll("\"","");
                    
                    String title;
                    String id;
                    
                try (Scanner readString = new Scanner(finalNewString)) {
                    readString.useDelimiter(",");
                    title = readString.next();
                    readString.next();
                    readString.next();
                    id = readString.next();
                    readString.close();
                    
                    String finalTitle = title.replace("Title:", "");
                    finalTitle = finalTitle.replaceAll("\\{", "");
                    //finalTitle = finalTitle.replaceAll("\\s+", "");
                    finalTitle = finalTitle.replaceAll("\\[","");
                    finalTitle = finalTitle.trim();
                    System.out.println(finalTitle);
                    
                    String finalId = id.replace("Id:", "");
                    finalId = finalId.replaceAll("\\s+", "");
                    
                    EventAttachmentModel attach = new EventAttachmentModel(finalTitle, finalId);
                    this.eventAttachments.add(attach);
                    
                    
                }
                catch(NoSuchElementException e) {
                    System.out.println("End of list");
                }
        }
    }
    
    public void viewAttachment() throws MalformedURLException {
        if(eventAttachment!=null) {
            getHostServices().showDocument(eventAttachment.toURI().toURL().toExternalForm());
        }
    }
}
