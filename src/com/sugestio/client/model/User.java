package com.sugestio.client.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class User extends Base {
    
    public enum Gender {
        M,
        F
    }    
    
    private String id;
    private String location_simple;
    private String location_latlong;
    private Gender gender;
    private String birthday;
    private String apml;
    private String foaf;

        
    public User() {
        
    }    
    
    public User(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getLocation_simple() {
        return this.location_simple;
    }

    public void setLocation_simple(String location_simple) {
        this.location_simple = location_simple;
    }

    public String getLocation_latlong() {
        return this.location_latlong;
    }

    public void setLocation_latlong(String location_latlong) {
        this.location_latlong = location_latlong;
    }

    public void setLocation_latlong(double latitude, double longitude) {
        this.location_latlong = latitude + "," + longitude;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    /**
     * Set the birthday for this user.
     * @param birthday a date string of the format yyyy-MM-dd e.g., 1980-04-19
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Convenience method for assigning a birthday to this user
     * @param year 4 digits e.g., 2010
     * @param month 0-based e.g., 0 for January
     * @param day day of the month (1-31)
     */
    public void setBirthday(int year, int month, int day) {
        this.birthday = getDateString(year, month, day);
    }

    /**
     * Convenience method for assigning a birthday to this user
     * @param milliseconds number of milliseconds that have passed since the UNIX epoch
     */
    public void setBirthday(long milliseconds) {
        this.birthday = getDateString(milliseconds);
    }

    public String getApml() {
        return this.apml;
    }

    public void setApml(String apml) {
        this.apml = apml;    
    }

    public String getFoaf() {
        return this.foaf;
    }

    public void setFoaf(String foaf) {
        this.foaf = foaf;
    }
    
}
