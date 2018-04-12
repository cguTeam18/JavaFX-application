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

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class EventModel {
    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private String location;
    private String eventAttachmentId;
    private String eventAttachmentTitle;
    private Date date;
    private String dateString;
    private static final String DATE_FORMAT = ("dd-MM-yyyy HH:mm");
    private ArrayList linkedEvents = new ArrayList<EventModel>();
    
    public EventModel() {
        long timestamp = 560705990;
        Date date = new Date(timestamp);
        this.eventId = randomStringGen();
        this.eventTitle = ("Untitled");
        this.eventDescription = ("Description");
        this.location = ("location");
        this.eventAttachmentId = randomStringGen();
        this.eventAttachmentTitle = ("Attachment title");
        this.date = date;
        this.dateString = date.toString();
        this.linkedEvents = new ArrayList();
    }
    
    public EventModel(String eventTitle, String eventDescription, String dateString, String location) throws ParseException {
        this.eventId = randomStringGen();
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = df.parse(dateString);
        this.date = date;
        this.location = location;
        this.eventAttachmentId = randomStringGen();
        this.eventAttachmentTitle = eventAttachmentTitle;
        this.linkedEvents = new ArrayList();
    }
    
    public EventModel(String eventId, String eventTitle, String eventDescription, String dateString, String location) throws ParseException {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.dateString = dateString;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = df.parse(dateString);
        this.date = date;
        this.location = location;
        this.linkedEvents = new ArrayList();
    }
    
    public EventModel(String eventId, String eventTitle, String eventDescription, String dateString, String location, String eventAttachmentId, String eventAttachmentTitle) throws ParseException {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = df.parse(dateString);
        this.date = date;
        this.location = location;
        this.eventAttachmentId = eventAttachmentId;
        this.eventAttachmentTitle = eventAttachmentTitle;
        this.linkedEvents = new ArrayList();
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
    
     public String getEventId() {
        return this.eventId;
    }
    
    public String getEventTitle() {
        return this.eventTitle;
    }
    
    public String getEventDescription() {
        return this.eventDescription;
    }
    
    public String getDateString() {
        return this.dateString;
    }
    
    public Date getDateCreatedLDT() {
        return this.date;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public String getEventAttachmentId() {
        return this.eventAttachmentId;
    }
    
    public String getEventAttachmentTitle() {
        return this.eventAttachmentTitle;
    }
    
    public ArrayList<EventModel> getLinkedEvents() {
        return this.linkedEvents;
    }
    
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    
    public void setEventlocation(String location) {
        this.location = location;
    }
    
    public void setDateString(String dateString) {
        //TO DO
        //Make sure the entered information is digits only and conforms to formatting "dd-MM-yyyy HH:mm"
        this.dateString = dateString;
    }
   
    
    public void setDate(String dateString) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = df.parse(dateString);
        this.date = date;
    }
    
    public void setEventAttachmentTitle(String eventAttachmentTitle) {
        this.eventAttachmentTitle = eventAttachmentTitle;
    }
    
    public void setLinkedEvents(ArrayList<EventModel> linkedEvents) {
        this.linkedEvents = linkedEvents;
    }
    
    public void addLinkedEvent(EventModel event) {
        if(!linkedEvents.contains(event)) {
            linkedEvents.add(event);
        }
    }
    
    public void removeLinkedEvent(EventModel event) {
        if(linkedEvents.contains(event)) {
            linkedEvents.remove(event);
        }
    }
}
