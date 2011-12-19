package com.sugestio.client.model;

import com.sugestio.client.model.detail.Detail;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Consumption extends Base {
    
    public enum Type {
        VIEW,
        ATTEND,
        RATING,
        WISHLIST,
        BASKET,
        PURCHASE,
        COLLECTION,
        CHECKIN;
    }    
    
    private String id;
    private String userid;
    private String itemid;
    private String type;
    private String detail;
    private String date;
    private String location_simple;
    private String location_latlong;
    
    
    public Consumption() {
    }
    
    public Consumption(String id, String userid, String itemid) {
    	this.id = id;
    	this.userid = userid;
    	this.itemid = itemid;
    }
    
    public Consumption(String userid, String itemid) {
        this.userid = userid;
        this.itemid = itemid;
    }
    
    public String getId() {
    	return this.id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    @XmlElement(required=true)
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }   
    
    @XmlElement(required=true)
    public String getItemid() {
        return this.itemid;
    }
    
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
   
    public void setType(Type type) {
        this.type = type.name();
    }
    
    public String getDetail() {
       return this.detail;
    }
       
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail.toString();
    }
    
    public String getDate() {
        return this.date;
    }

    /**
     * Set the consumption date. See the API documentation ("Working with timestamps") for more information on supported formats.
     * @param date a timestamp in a supported format
     */
    public void setDate(String date) {
        this.date = date;   
    }

    /**
     * Convenience method for assigning a date to this consumption
     * @param milliseconds number of milliseconds that have passed since the UNIX epoch
     */
    public void setDate(long milliseconds) {
        this.date = getDateString(milliseconds);        
    }

    /**
     * Convenience method for assigning a date to this consumption
     * @param year 4 digits e.g., 2010
     * @param month 0-based e.g., 0 for January
     * @param day day of the month (1-31)
     */
    public void setDate(int year, int month, int day) {
        this.date = getDateString(year, month, day);
    }
    
    public String getLocation_simple() {
        return this.location_simple;
    }

    public void setLocation_simple(String location_simple) {
        this.location_simple = location_simple;
    }

    public String getLocation_latLong() {
        return this.location_latlong;
    }

    public void setLocation_latLong(String location_latLong) {
        this.location_latlong = location_latLong;
    }

    public void setLocation_latlong(double latitude, double longitude) {
        this.location_latlong = latitude + "," + longitude;
    }
    
}
