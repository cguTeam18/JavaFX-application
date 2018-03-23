/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.AllEvents;
import Models.AllTimelines;
import Models.EventModel;
import Models.TimelineModel;
import RequestMethods.GetMethods;
import java.text.ParseException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author jidev
 */
public class GetController {
   
    static AllTimelines timelines;
   /** AllTimelines timelines;
    AllEvents events;
    GetMethods get;
    
    public GetController() {
        AllTimelines allTimelines = new AllTimelines();
        AllEvents allEvents = new AllEvents();
        GetMethods get = new GetMethods();
        this.timelines = allTimelines;
        this.events = allEvents;
        this.get = get;
    }
    
    public HashMap<String, TimelineModel> getTimelines() {
        return timelines.getTimelines();
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
                    timelines.AddTimeline(timeline);    
                }
    }
    
    public void addTimelineObject(String timeLineIdKey, String timeLineIdVal) throws Exception {
        
        String newString = get.sendGetTimeline(timeLineIdKey, timeLineIdVal);
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
        timelines.AddTimeline(timeline);
    }
    
    public String getTimelineName(String timelineId) {
        return timelines.getTimelineTitle(timelineId);
    }
    
    public String getTimelineDate(String timelineId) {
        return timelines.getTimelineDate(timelineId);
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
                    
                    String finalLocation = location.replace("Location: ", "");
                    
                    String finalId = id.replace("Id:", "");
                    finalId = finalId.replaceAll("\\s+", "");
                    System.out.println(finalId + finalTitle + finalDateTime + finalDescription+finalLocation);
                    EventModel event = new EventModel(finalId, finalTitle, finalDescription, finalDateTime, finalLocation);
                    events.AddEvent(event);  
                    }
                        
        }
    }
    
    public void getTimelineEvent(String timelineIdKey, String timelineIdVal) throws Exception {
            TimelineModel timeline;
            EventModel event;
            
            String[] separatedValue = get.sendGetLinkedEvents(timelineIdKey, timelineIdVal);
            
        for(int i =0; i<separatedValue.length; i++) {
            String newString = separatedValue[i];
            String finalNewString = newString.replaceAll("\"","");
            //System.out.println(finalNewString);
            
            //String eventString = finalNewString.substring(finalNewString.indexOf("TimelineEvents:"));
           try{
            Scanner readString = new Scanner(finalNewString);
            readString.useDelimiter(",");
                    
            String id;
            String eventId;
                    
            id = readString.next();
            readString.close();
            
            //Scanner readEvents = new Scanner(eventString);
            //readEvents.useDelimiter(",");
            //eventId = readEvents.next();
            //readString.close();
            
            String finalId = id.replaceAll("\\s+", "");
            finalId = finalId.replaceAll("\\{", "");
            finalId = finalId.replaceAll("\\[","");
            finalId = finalId.replaceAll("\\]", "");
            finalId = finalId.replace("TimelineEvent","");
            finalId = finalId.replace("Id:","");
           
            
            //String finalEventId = eventId.replaceAll("\\s+","");
            //finalEventId = finalEventId.replaceAll("\\{", "");
            //finalEventId = finalEventId.replace("TimelineEvents:[","");
            //finalEventId = finalEventId.replace("Id:","");
            //if (finalEventId.equals("]")) {
            //    finalEventId = null;
            //}
            System.out.println(finalId);
            timeline = timelines.getTimeline(timelineIdVal);
            event = events.getEvent(finalId);
            timeline.addEvent(event);
            timelines.update(timeline, timelineIdVal);
            
            //System.out.println(finalEventId);
            //String[] separateEvents = events.split("}");
            }
           catch(NoSuchElementException e){
               System.out.println(e);
           }
            }
        }
        * */
}
