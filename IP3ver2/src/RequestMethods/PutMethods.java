/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequestMethods;

/**
 *
 * @author jidev
 */
import java.io.*;
import java.net.*;

public class PutMethods {

    private final String tenantId;
    private final String authToken;


    public PutMethods() {
        String tenantId = "Team18";
        String authToken = "ddd8af7c-3e22-4d31-aefa-3e834071c954";
        this.tenantId = tenantId;
        this.authToken = authToken;
    }
    /**
    public static void main(String[] args) throws Exception {

        PutMethods http = new PutMethods();

        System.out.println("Testing 1 - CreateTimeline method "); //DONE
        http.createTimeline("12345678","WorkingTimeline"); //DONE
        //System.out.println("Testing 2 - Edit Timeline Title method "); //DONE
        //http.editTimelineTitle("TimelineId", "ID1","Title", "NewTitle"); //DONE
        //System.out.println("Testing 3 - Delete Timeline method "); //DONE
        //http.deleteTimeline("TimelineId","ID1"); DONE
        //System.out.println("Testing 4 - LinkEvent method "); //DONE
        //http.linkEvent("TimelineId","ID1","EventId","1234578"); //DONE
        //System.out.println("Testing 5 - UnLinkEvent method "); //DONE
        //http.unlinkEvent("TimelineId","ID1","EventId","1234578"); //DONE
        //System.out.println("Testing 6 - Create Event method "); //DONE
        //http.createEvent("TimelineEventId","1234578","Title","Delayed Flight","Description","Flight was delayed due to a spanner in the engine","EventDateTime","02-11-2018 18:00","Location","Glasgow"); //DONE
        //System.out.println("Testing 7 - Edit Event Title method "); //DONE
        //http.editEventTitle("TimelineEventId","IDE1","Title","New Event Title"); //DONE
        //System.out.println("Testing 8 - Edit Event Description method "); //DONE
        //http.editDescription("TimelineEventId","IDE1","Description","The spanner was removed right quick, so it was."); //DONE
        //System.out.println("Testing 9 - Edit Event Location  method "); //DONE
        //http.editLocation("TimelineEventId","IDE1","Location","Earth"); //DONE
        //System.out.println("Testing 10 - Edit Event Date and Time method "); //DONE
        //http.editEventDateTime("TimelineEventId","IDE1","EventDateTime","13-03-2018 01:22"); //DONE
        //System.out.println("Testing 11 - Delete Timeline Event method "); //DONE
        //http.deleteTimelineEvent ("TimelineEventId","IDE1"); //DONE
        //System.out.println("Testing 12 - Link Timeline Events method "); //DONE
        //http.linkTimelineEvent ("TimelineEventID","1234578","LinkedToTimelineEventId","5555"); //DONE
        //System.out.println("Testing 13 - unLink Timeline Events method "); //DONE
        //http.unLinkTimelineEvent ("TimelineEventId", "1234578", "UnlinkedFromTimelineEventId","5555"); //DONE
        //System.out.println("Testing 14 - Create Timeline Event Attachment method "); //DONE
        //http.createTimelineEventAttachment ("TimelineEventId", "5555", "AttachmentId", "Attach1", "Title", "Attachment Title" ); //DONE
        //System.out.println("Testing 15 - Edit Timeline Event Attachment method "); //DONE
        //http.editTitleTimelineEventAttachment( "AttachmentId", "Attach1","Title","Edited Title" ); //DONE
        //System.out.println("Testing 16 - Delete Timeline Event Attachment method "); //DONE
        //http.deleteTitleTimelineEventAttachment( "AttachmentId","Attach1" ); //DONE
        
    }
    * */

    public void createTimeline(String TimelineIdVal, String TimelineTitleVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/Timeline/Create");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineId", TimelineIdVal);
        httpCon.setRequestProperty("Title", TimelineTitleVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', 'TimelineId':'"+TimelineIdVal+"','Title':'"+TimelineTitleVal+"'"+"}");
        System.out.println("Sending PUT request to: " + url);
        System.out.println("TenantId : " + tenantId + " AuthToken : " + authToken);
        System.out.println("TimelineIdKey: TimelineId, TimelineIdVal "+ TimelineIdVal);
        out.close();

        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
                System.out.println(response.toString());
        System.out.println(response.toString());
    }

    //else    {
    //   System.out.println("POST request not worked");
    // }

    public void editTimelineTitle(String TimelineIdVal, String TimelineTitleVal) throws Exception {

        //String url = "https://gcu.ideagen-development.com//Timeline/LinkEvent";
        URL url = new URL("https://gcu.ideagen-development.com/Timeline/EditTitle");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineId", TimelineIdVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', 'TimelineId':'"+TimelineIdVal+"','Title':'"+TimelineTitleVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());

    }

    public void deleteTimeline(String TimelineIdVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/Timeline/Delete");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineId", TimelineIdVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', "+"'TimelineId':'"+TimelineIdVal+"'}");
        System.out.println("Sending PUT request to: " + url);
        //System.out.println("TenantId : " + tenantId + " AuthToken : " + authToken);
        //System.out.println("TimelineEventIdKey : " + TimelineEventIdKey + " TimelineEventIdVal : " + TimelineEventIdVal);
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void linkEvent(String TimelineIdVal, String TimelineEventIdVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/Timeline/LinkEvent");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineId", TimelineIdVal);
        httpCon.setRequestProperty("EventId", TimelineEventIdVal);

        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', 'TimelineId':'"+TimelineIdVal+"', 'EventId':'"+TimelineEventIdVal+"'}");
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void unlinkEvent(String TimelineIdVal, String TimelineEventIdVal) throws Exception {
        URL url = new URL("https://gcu.ideagen-development.com/Timeline/UnlinkEvent");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineId", TimelineIdVal);
        httpCon.setRequestProperty("EventId", TimelineEventIdVal);

        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', 'TimelineId':'"+TimelineIdVal+"', 'EventId':'"+TimelineEventIdVal+"'}");
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void createEvent(String TimelineEventIdVal,String TitleVal,String DescriptionVal,
            String EventDateTimeVal, String LocationVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/Create");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineEventId", TimelineEventIdVal);
        httpCon.setRequestProperty("Title", TitleVal);
        httpCon.setRequestProperty("Description", DescriptionVal);
        httpCon.setRequestProperty("EventDateTime", EventDateTimeVal);
        httpCon.setRequestProperty("Location", LocationVal);

        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954','TimelineEventId':'"+TimelineEventIdVal+"','Title':'"+TitleVal+"','Description':'"+DescriptionVal+"','EventDateTime':'"+EventDateTimeVal+"','Location':'"+LocationVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void editEventTitle(String TimelineEventIdKey, String TimelineEventIdVal, String TimelineEventTitleKey, String TimelineEventTitleVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/EditTitle");
        
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(TimelineEventIdKey, TimelineEventIdVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18','AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954','"+TimelineEventIdKey+"':'"+TimelineEventIdVal+"','"+TimelineEventTitleKey+"':'"+TimelineEventTitleVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void editDescription(String TimelineEventIdKey, String TimelineEventIdVal, String TimelineEventDescriptionKey, String TimelineEventDescriptionVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/EditDescription");
        
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(TimelineEventIdKey, TimelineEventIdVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954',"+"'"+TimelineEventIdKey+"':'"+TimelineEventIdVal+"','"+TimelineEventDescriptionKey+"':'"+TimelineEventDescriptionVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void editLocation(String TimelineEventIdKey, String TimelineEventIdVal, String TimelineEventLocationKey, String TimelineEventLocationVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/EditLocation");
        
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(TimelineEventIdKey, TimelineEventIdVal);
        httpCon.setRequestProperty(TimelineEventLocationKey, TimelineEventLocationVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954',"+"'"+TimelineEventIdKey+"':'"+TimelineEventIdVal+"','"+TimelineEventLocationKey+"':'"+TimelineEventLocationVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void editEventDateTime(String TimelineEventIdKey, String TimelineEventIdVal, String TimelineEventDateTimeKey, String TimelineEventDateTimeVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/EditEventDateTime");
        
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(TimelineEventIdKey, TimelineEventIdVal);
        httpCon.setRequestProperty(TimelineEventDateTimeKey, TimelineEventDateTimeVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954',"+"'"+TimelineEventIdKey+"':'"+TimelineEventIdVal+"','"+TimelineEventDateTimeKey+"':'"+TimelineEventDateTimeVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void deleteTimelineEvent(String TimelineEventIdVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/Delete");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineEventId", TimelineEventIdVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', "+"'TimelineEventId':'"+TimelineEventIdVal+"'}");
        System.out.println("Sending PUT request to: " + url);
        //System.out.println("TenantId : " + tenantId + " AuthToken : " + authToken);
        //System.out.println("TimelineEventIdKey : " + TimelineEventIdKey + " TimelineEventIdVal : " + TimelineEventIdVal);
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void linkTimelineEvent(String TimelineEventIdVal, String LinkedToTimelineEventIdVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/LinkEvents");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty("TimelineEventId", TimelineEventIdVal);
        httpCon.setRequestProperty("LinkedToTimelineEventId", LinkedToTimelineEventIdVal);

        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', 'TimelineEventId':'"+TimelineEventIdVal+"','LinkedToTimelineEventId':'"+LinkedToTimelineEventIdVal+"' }");
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void unLinkTimelineEvent(String TimelineEventIdKey, String TimelineEventIdVal, String UnlinkedFromTimelineEventIdKey, String UnlinkedFromTimelineEventIdVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEvent/UnlinkEvents");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(TimelineEventIdKey, TimelineEventIdVal);
        httpCon.setRequestProperty(UnlinkedFromTimelineEventIdKey, UnlinkedFromTimelineEventIdVal);

        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', '"+TimelineEventIdKey+"':'"+TimelineEventIdVal+"','"+UnlinkedFromTimelineEventIdKey+"':'"+UnlinkedFromTimelineEventIdVal+"' }");
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void createTimelineEventAttachment (String TimelineEventIdKey, String TimelineEventIdVal, String AttachmentIdKey, String AttachmentIdVal, String TitleKey, String TitleVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEventAttachment/Create");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(TimelineEventIdKey, TimelineEventIdVal);
        httpCon.setRequestProperty(AttachmentIdKey, AttachmentIdVal);
        httpCon.setRequestProperty(TitleKey, TitleVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', "+"'"+TimelineEventIdKey+"':'"+TimelineEventIdVal+"','"+AttachmentIdKey+"':'"+AttachmentIdVal+"','"+TitleKey+"':'"+TitleVal+"'"+"}");
        out.close();

        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }

    public void editTitleTimelineEventAttachment (String AttachmentIdKey, String AttachmentIdVal, String TitleKey, String TitleVal) throws Exception {

        //String url = "https://gcu.ideagen-development.com//Timeline/LinkEvent";
        URL url = new URL("https://gcu.ideagen-development.com/TimelineEventAttachment/EditTitle");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(AttachmentIdKey, AttachmentIdVal);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954',"+"'"+AttachmentIdKey+"':'"+AttachmentIdVal+"','"+TitleKey+"':'"+TitleVal+"'}");
        out.close();
        
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());

    }

    public void deleteTitleTimelineEventAttachment (String AttachmentIdKey, String AttachmentIdVal) throws Exception {

        URL url = new URL("https://gcu.ideagen-development.com/TimelineEventAttachment/Delete");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("tenantId", tenantId);
        httpCon.setRequestProperty("AuthToken", authToken);
        httpCon.setRequestProperty(AttachmentIdKey, AttachmentIdKey);
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

        out.write("{'TenantId':'Team18', 'AuthToken':'ddd8af7c-3e22-4d31-aefa-3e834071c954', "+"'"+AttachmentIdKey+"':'"+AttachmentIdVal+"'}");
        System.out.println("Sending PUT request to: " + url);
        //System.out.println("TenantId : " + tenantId + " AuthToken : " + authToken);
        //System.out.println("TimelineEventIdKey : " + TimelineEventIdKey + " TimelineEventIdVal : " + TimelineEventIdVal);
        out.close();
        BufferedReader in = new BufferedReader(
		        new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
        System.out.println(response.toString());
    }
}
