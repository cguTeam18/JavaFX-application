/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author jidev
 */
import Models.EventModel;
import Models.TimelineModel;
import RequestMethods.GetMethods;
import RequestMethods.PutMethods;
import java.text.ParseException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AllTimelines {
    
    private HashMap<String, TimelineModel> timelines;
    private HashMap<String, EventModel> events;
    private HashMap<String, EventModel> linkedEvents;
    GetMethods get;
    PutMethods put;
    
    
    public AllTimelines() {
        timelines = new HashMap<>();
        events = new HashMap<>();
        get = new GetMethods();
        put = new PutMethods();
    }
    
    public HashMap<String, TimelineModel> getTimelines() 
	{
		return timelines;
        } 
    
    public HashMap<String, EventModel> getEvents() 
	{
		return events;
        } 
    
    public void setTimelines(HashMap<String, TimelineModel> timelines) {
        this.timelines = timelines;
    }
    
    public void setEvents(HashMap<String, EventModel>events) {
        this.events = events;
    }
    
    
    public void AddTimeline(TimelineModel newTimeline)
	{
		if 
		(!timelines.containsKey(newTimeline.getTimelineId()))
		{
			timelines.put(newTimeline.getTimelineId(), newTimeline);
		}
        }
    
    public void AddEvent(EventModel newEvent)
	{
		if 
		(!events.containsKey(newEvent.getEventId()))
		{
			events.put(newEvent.getEventId(), newEvent);
		}
        }
    
    public TimelineModel getTimeline(String timelineKey) {
        
        TimelineModel timeline;
        if(timelines.containsKey(timelineKey)) {
            timeline = timelines.get(timelineKey);
        }
        else {
            timeline = null;
        }
        return timeline;
    }
    
    public EventModel getEvent(String eventKey) {
        
        EventModel event;
        if(events.containsKey(eventKey)) {
            event = events.get(eventKey);
        }
        else {
            event = null;
        }
        return event;
    }
    
    public String getTimelineTitle(String timelineKey) {
        TimelineModel timeline;
        String timelineTitle;
        if(timelines.containsKey(timelineKey)) {
            timeline = timelines.get(timelineKey);
            timelineTitle = timeline.getTimelineTitle();
        }
        else {
            timelineTitle = ("No title found...");
        }
        return timelineTitle;
    }
    
    public String getTimelineDate(String timelineKey) {
        TimelineModel timeline;
        String timelineDate;
        if(timelines.containsKey(timelineKey)) {
            timeline = timelines.get(timelineKey);
            timelineDate = timeline.getDateCreatedString();
        }
        else {
            timelineDate = ("No Date found...");
        }
        return timelineDate;
    }
    
    public void deleteTimeline(String timelineKey) {
        if(timelines.containsKey(timelineKey)){
            timelines.remove(timelineKey);
        }
    }
    
    public void deleteEvent(String eventKey) {
        if(events.containsKey(eventKey)){
            events.remove(eventKey);
        }
    }
    
    public void updateTimeline(TimelineModel timeline, String timelineKey) {
        if(timelines.containsKey(timelineKey)){
            timelines.replace(timelineKey, timeline);
        }
        
    }
    
    public void updateEvent(EventModel event, String eventKey) {
        if(events.containsKey(eventKey)){
            events.replace(eventKey, event);
        }
        
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
                    AddTimeline(timeline);    
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
                    
                    String finalLocation = location.replace("Location: ", "");
                    
                    String finalId = id.replace("Id:", "");
                    finalId = finalId.replaceAll("\\s+", "");
                    System.out.println(finalId + finalTitle + finalDateTime + finalDescription+finalLocation);
                    EventModel event = new EventModel(finalId, finalTitle, finalDescription, finalDateTime, finalLocation);
                    AddEvent(event);  
                    }
                        
        }
    }
    
    public void getTimelineObject(String timeLineIdKey, String timeLineIdVal) throws Exception {
        
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
        AddTimeline(timeline);
    }
    
    public void getTimelineEvents(String timelineIdKey, String timelineIdVal) throws Exception {
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
            timeline = getTimeline(timelineIdVal);
            event = getEvent(finalId);
            timeline.addEvent(event);
            updateTimeline(timeline, timelineIdVal);
            
            //System.out.println(finalEventId);
            //String[] separateEvents = events.split("}");
            }
           catch(NoSuchElementException e){
               System.out.println(e);
           }
            }
        }
    public void addTimelineObject(String timelineTitle) throws ParseException, Exception{
        TimelineModel timeline = new TimelineModel(timelineTitle);
        AddTimeline(timeline);
        put.createTimeline(timeline.getTimelineId(), timeline.getTimelineTitle());
    }
    
    public void addEventObject(String eventTitle, String eventDescription, String dateString, String location) throws ParseException, Exception {
        EventModel event = new EventModel(eventTitle, eventDescription, dateString, location);
        AddEvent(event);
        put.createEvent(event.getEventId(),event.getEventTitle(), event.getEventDescription(), event.getDateString(), event.getLocation());
    }
    
    public void deleteTimelineObject(String timelineId) throws Exception {
        TimelineModel timeline = getTimeline(timelineId);
        if(timeline != null) {
            deleteTimeline(timeline.getTimelineId());
            put.deleteTimeline(timeline.getTimelineId());
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
