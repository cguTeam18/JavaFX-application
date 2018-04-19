/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.EventAttachmentModel;
import ip3ver2.IP3ver2;
import Models.EventModel;
import Models.TimelineModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

/**
 * FXML Controller class
 *
 * @author jidev
 */
public class EditEventControllerFromEvent implements Initializable {

    private EventModel event;
    private EventAttachmentModel attachmentObj;
    private GetMethods get = new GetMethods();
    private PutMethods put = new PutMethods();
    private TimelineModel selectedTimeline;
    private final ArrayList<EventAttachmentModel> eventAttachments;
    private String filename = "";
    private File eventAttachment = null;
    
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
    
    @FXML
    HBox root;
    
    private WebEngine webEngine;
    
    public EditEventControllerFromEvent() throws ParseException, Exception {
        EventModel event = ViewTimelineController.selectedEvent;
        this.get = new GetMethods();
        this.put = new PutMethods();
        this.event = event;
        this.selectedTimeline = TableController.selectedTimeline;
        this.eventAttachments = new ArrayList();
        getAllAttachments();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setPadding(new Insets(10));
        root.setSpacing(10);
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
        setAttachment();
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
    public void setAttachment() {
        eventAttachments.forEach((attach) -> {
            try {
                downloadAttachment(attach.getTitle());
                this.attachmentObj = attach;
            } catch (Exception ex) {
                Logger.getLogger(ViewEventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        if(eventAttachment != null)// if file is selected, display file
        {
            Text t = new Text();
            String downloadFilename = eventAttachment.getName();
            Button del = new Button("Remove Attachment");
            del.onMouseClickedProperty().set(new EventHandler<MouseEvent>()  // When button is clicked, proceed
        {
            @Override
            public void handle(MouseEvent event)
            {
                try {
                    put.deleteTitleTimelineEventAttachment(attachmentObj.getId());
                    Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/EditEventFromEvent.fxml"));
                    Scene scene = new Scene(viewRoot);
                    IP3ver2.currentStage.setScene(scene);
                } catch (Exception ex) {
                    Logger.getLogger(EditEventControllerFromTimeline.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
            t.setText(downloadFilename); // Display Filename
            root.getChildren().addAll(t, del); // Add filename to screen
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
    public void addEventAttachment(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        configuringFileChooser(fileChooser); // Configure file chooser
        fileChooser.setTitle("Open Resource File"); // Set window title
        File selectedFile = fileChooser.showOpenDialog(IP3ver2.currentStage);
        Text t = new Text();
        String filename = selectedFile.getName();

        if(selectedFile != null)// if file is selected, display file
        {
            t.setText(filename); // Display Filename
            Button del = new Button("Remove Attachment");
            del.onMouseClickedProperty().set(new EventHandler<MouseEvent>()  // When button is clicked, proceed
        {
            @Override
            public void handle(MouseEvent event)
            {
                root.getChildren().remove(1);
                root.getChildren().remove(del);
            }
        });
            t.setText(filename); // Display Filename
            root.getChildren().addAll(t, del); // Add filename to screen
            this.filename = filename;
            this.eventAttachment = selectedFile;
        }
        else // Display error in text to user
        {
           t.setText("YOU HAVE NOT SELECTED A FILE");
        }
    }
    
    private void
    configuringFileChooser(FileChooser fileChooser)
    {
        // Set title for FileChooser
        fileChooser.setTitle("Select File");

        // Set Initial Directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add Extension Filters

        fileChooser.getExtensionFilters().addAll(//
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("DOC", "*.doc"));

    }   // Close configuring File Chooser method
    
    // 1. Generate upload pre-signed url AIP method using filename as a parameter
    public void uploadFile(String filename, File selectedFile) throws Exception
        {
            if(selectedFile!= null) {
            try{
                put.deleteTitleTimelineEventAttachment(this.attachmentObj.getId());
            }
            catch(NullPointerException e) {
                System.out.println("no attachment found");
            }
            URL responseURL = get.sendGetUploadURL(filename); // 2. Assign the response from that call to a URL data type

            HttpURLConnection URLconnection = (HttpURLConnection)responseURL.openConnection();
            URLconnection.setDoOutput(true);
            URLconnection.setRequestMethod("PUT");
            //URLconnection.connect();
        

            // 3. Make a request to above URL
            //if(URLconnection.getContentType().equalsIgnoreCase("image/jpeg/png/DOC")) // not sure if the file extensions are correct
            //{
            //  System.out.println("Correct content");
                // Sample code had code below but there is an error
                // result = true;
            //}

            // 4. User a FileStream or similar to stream file into a byte data type
            OutputStream os = URLconnection.getOutputStream();
            FileInputStream fs = new FileInputStream(selectedFile);

            // 5. Write bytes to request object
            byte[] fileData = new byte[4096];
            int read = 0;

            while((read = fs.read(fileData, 0, fileData.length))>0)
            {
                os.write(fileData, 0, read);
            }
            fs.close();
            os.close();
            System.out.println(URLconnection.getResponseCode());
            put.createTimelineEventAttachment(this.event.getEventId(), filename);
            get.sendGetAllAttachments(this.event.getEventId());
        }
        
        

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
            try {
                uploadFile(filename, eventAttachment);
            } catch (Exception ex) {
                Logger.getLogger(AddNewEventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(IP3ver2.events.contains(this.event)) {
                IP3ver2.events.remove(this.event);
                IP3ver2.events.add(this.event);
                put.editEventTitle(eventIdKey, this.event.getEventId(), titleKey, this.event.getEventTitle());
                put.editDescription(eventIdKey, this.event.getEventId(), descriptionKey, this.event.getEventDescription());
                put.editEventDateTime(eventIdKey, this.event.getEventId(), eventDateTimeKey, this.event.getDateString());
                put.editLocation(eventIdKey, this.event.getEventId(), locationKey, this.event.getLocation());
                ViewTimelineController.selectedEvent = this.event;
                Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewEvent.fxml"));
                Scene scene = new Scene(viewRoot);
                IP3ver2.currentStage.setScene(scene);
            }
        }
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
    
    @FXML 
    private void backToTimeline(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewTimeline.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
    
    @FXML 
    private void backToEvent(ActionEvent event) throws IOException {
        Parent viewRoot = FXMLLoader.load(getClass().getResource("/GUI/viewEvent.fxml"));
        Scene scene = new Scene(viewRoot);
        IP3ver2.currentStage.setScene(scene);
    }
}

