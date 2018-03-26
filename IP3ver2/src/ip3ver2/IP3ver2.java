/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3ver2;
import Models.EventModel;
import Models.TimelineModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import com.sun.javaws.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IP3ver2 extends Application {

    public static Stage currentStage;
    public static ObservableList<TimelineModel> timelines = FXCollections.observableArrayList();
    public static ObservableList<EventModel> events = FXCollections.observableArrayList();
    public static ObservableList<EventModel> linkedEvents = FXCollections.observableArrayList();
    GetMethods get = new GetMethods();
    PutMethods put = new PutMethods();
    
    @Override
    public void start(Stage primaryStage) throws IOException, Exception {
        
        createTimelineObjects();
        createEventObjects();
        URL url = getClass().getResource("/GUI/TableView.fxml");
        AnchorPane pane = FXMLLoader.load(url);
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX TableView");
        primaryStage.show();
        currentStage = primaryStage;
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
    
    public void createEventObjects() throws Exception {
        
        String[] separatedValue = get.sendGetTimelineEvents();
        for(int i =0; i<separatedValue.length-1; i++) {
            
                    String newString = separatedValue[i];//.substring(separatedValue[i].lastIndexOf(":")+1);
                    String finalNewString = newString.replaceAll("\"","");
                    
                    Scanner readString = new Scanner(finalNewString);
                    readString.useDelimiter(",");
                    String title;
                    String datetime;
                    String description;
                    String location;
                    String id;
                    String isDeleted;
                    title = readString.next();
                    datetime = readString.next();
                    description = readString.next();
                    isDeleted = readString.next();
                    location = readString.next();
                    id = readString.next();
                    readString.next();
                    readString.close();
                    String finalIsDeleted = isDeleted.replaceAll("\\s+", "");
                    if(finalIsDeleted.equals("IsDeleted:false")){
                        String finalTitle = title.replace("Title:", "");
                    finalTitle = finalTitle.replaceAll("\\{", "");
                    finalTitle = finalTitle.replaceAll("\\s+", "");
                    finalTitle = finalTitle.replaceAll("\\[","");
                    
                    String finalDescription = description.replace("Description: ", "");
                   
                    String finalDateTime = datetime.replace("EventDateTime:","");
                    finalDateTime = finalDateTime.trim();
                    
                    String finalLocation = location.replace("Location: ", "");
                    
                    String finalId = id.replace("Id:", "");
                    finalId = finalId.replaceAll("\\s+", "");
                    System.out.println(finalId + finalTitle + finalDateTime + finalDescription+finalLocation);
                    EventModel event = new EventModel(finalId, finalTitle, finalDescription, finalDateTime, finalLocation);
                    events.add(event);  
                    }
                        
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}
/**
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author jidev
 */
/**
public class IP3ver2 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
/**
    public static void main(String[] args) {
        launch(args);
    }
    * */
    
//}
