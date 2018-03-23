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
import java.util.HashMap;

public class AllEvents {
    
    private HashMap<String, EventModel> events;
    
    public AllEvents() {
        events = new HashMap<>();
    }
    
    public HashMap<String, EventModel> getEvents() 
	{
		return events;
        } 
    
    public void AddEvent(EventModel newEvent)
	{
		if 
		(!events.containsKey(newEvent.getEventId()))
		{
			events.put(newEvent.getEventId(), newEvent);
		}
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
    
    public void deleteEvent(String eventKey) {
        if(events.containsKey(eventKey)){
            events.remove(eventKey);
        }
    }
}
