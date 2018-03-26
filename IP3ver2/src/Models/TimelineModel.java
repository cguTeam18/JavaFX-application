/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
/**
 *
 * @author jidev
 * To Do:
 * Potential test case: Make sure no two timeline IDs are the same value
 * Parse Linked Event information to an event object, then add that object to the event Arraylist.
 * - Will have to make a for each loop to address this. 
*/
public class TimelineModel implements Serializable {
    
    private String timelineId;
    private String timelineTitle;
    private Date dateCreated;
    private String dateCreatedString;
    private ArrayList timelineEvents = new ArrayList<EventModel>();
    
    
   // public static void main(String[]args) {
   //     TimelineModel tl = new TimelineModel();
   //     tl.randomStringGen();
   //}
    public TimelineModel() {
        long timestamp = 560705990;
        Date date = new Date(timestamp);
        timelineId = randomStringGen();
        timelineTitle = ("Untitled");
        dateCreated = date;
    }
    
    public TimelineModel(String timelineTitle) throws ParseException {
        this.timelineId = randomStringGen();
        this.timelineTitle = timelineTitle;
        Date currentDate = Calendar.getInstance().getTime();
        this.dateCreated = currentDate;
        setDateCreatedString();
    }
    
    public TimelineModel(String timelineId, String timelineTitle, long timestamp) {
        this.timelineId = timelineId;
        this.timelineTitle = timelineTitle;
        Date date = new Date(timestamp);
        this.dateCreated = date;
        setDateCreatedString();
    }
   
    
    public final String randomStringGen() {
        Random random = new Random();
        String characters = "1234567890ABCDEFGHIJK";
        StringBuilder sb = new StringBuilder();
        
        
        for(int i = 0; i < 25; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        String result = sb.toString();
        System.out.println(result);
        return result;
    }
    
    public String getTimelineId() {
        return this.timelineId;
    }
    
    public String getTimelineTitle() {
        return this.timelineTitle;
    }
    
    public void setTimelineTitle(String timelineTitle) {
        this.timelineTitle = timelineTitle;
    }
    
    public String getDateCreatedString() {
        return this.dateCreatedString;
    }
    
    public final void setDateCreatedString() {
        //TO DO
        //Make sure the entered information is digits only and conforms to formatting "dd-MM-yyyy HH:mm"
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = this.dateCreated;
        String dateCreated = df.format(date);
        this.dateCreatedString = dateCreated;
    }
   
    public Date getDateCreatedLDT() {
        return this.dateCreated;
    }
    
    public void setDateCreated() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = df.parse(this.dateCreatedString);
        this.dateCreated = date;
    }
    
    public void setTimelineEvents(ArrayList timelineEvents) {
        this.timelineEvents = timelineEvents;
    }
    
    public void addEvent(EventModel event) {
        if(!this.timelineEvents.contains(event)){
            this.timelineEvents.add(event);
        }
    }
    
    public ArrayList getEvents() {
        return this.timelineEvents;
    }
    
    public void deleteEvent(EventModel event) {
        ArrayList events = this.timelineEvents;
        if(events.contains(event)){
            events.remove(event);
        }
    }
    
    
    
}
