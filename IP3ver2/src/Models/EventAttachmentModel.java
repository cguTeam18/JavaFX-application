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
public class EventAttachmentModel {
    
    private String title;
    private String id;
    
    public EventAttachmentModel() {
        this.title = "no title";
        this.id = "no id";
    }
    
    public EventAttachmentModel(String title, String id) {
        this.title = title;
        this.id = id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
