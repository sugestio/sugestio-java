package com.sugestio.client.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Item extends Base {

    public enum Available {
        Y,
        N
    }
    
    private String id;
    private String title;
    private Available available;
    private String description_short;
    private String description_long;
    private String from;
    private String until;
    private String location_simple;
    private String location_latlong;

    @XmlElement(name="creator")
    private List<String> creators;    

    @XmlElement(name="tag")
    private List<String> tags;

    @XmlElement(name="category")
    private List<String> categories;

    @XmlElement(name="segment")
    private List<String> segments;

    private String permalink;
    
    
    public Item() {
        this.tags = new ArrayList<String>();
        this.creators = new ArrayList<String>();
        this.categories = new ArrayList<String>();
        this.segments = new ArrayList<String>();
    }    
    
    public Item(String id) {
        this();
        this.id = id;
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Available getAvailable() {
        return this.available;
    }

    public void setAvailable(Available available) {
        this.available = available;
    }
    
    public String getDescription_short() {
        return this.description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;        
    }

    public String getDescription_long() {
        return this.description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;       
    }

    /**
     * The date from when the item may be recommended.
     * @return from date
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set the date from when the item may be recommended. See the API documentation ("Working with timestamps") for more information on supported formats.
     * @param from a timestamp in a supported format
     */
    public void setFrom(String from) {
        this.from = from;        
    }

    /**
     * Convenience method for setting the 'from' attribute
     * @param milliseconds number of milliseconds that have passed since the UNIX epoch
     */
    public void setFrom(long milliseconds) {
        this.from = getDateString(milliseconds);
    }

    /**
     * Convenience method for setting the 'from' attribute
     * @param year 4 digits e.g., 2010
     * @param month 0-based e.g., 0 for January
     * @param day day of the month (1-31)
     */
    public void setFrom(int year, int month, int day) {
        this.from = getDateString(year, month, day);
    }

    /**
     * Get the date until when this item may be recommended
     * @return until date
     */
    public String getUntil() {
        return this.until;
    }

    /**
     * Set the date until when this item may be recommended. See the API documentation ("Working with timestamps") for more information on supported formats.
     * @param until a timestamp in a supported format     
     */
    public void setUntil(String until) {
        this.until = until;        
    }

    /**
     * Convenience method for setting the 'until' attribute
     * @param milliseconds number of milliseconds that have passed since the UNIX epoch
     */
    public void setUntil(long milliseconds) {
        this.until = getDateString(milliseconds);
    }

    /**
     * Convenience method for setting the 'until' attribute
     * @param year 4 digits e.g., 2010
     * @param month 0-based e.g., 0 for January
     * @param day day of the month (1-31)
     */
    public void setUntil(int year, int month, int day) {
        this.until = getDateString(year, month, day);
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

    public List<String> getCreators() {
        return this.creators;
    }

    public void setCreators(List<String> creators) {
        this.creators = creators;
    }

    public void addCreator(String creator) {
        this.creators.add(creator);
    }
    
    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    /**
     * @return the segments
     */
    public List<String> getSegments() {
        return segments;
    }

    /**
     * @param segments the segments to set
     */
    public void setSegments(List<String> segments) {
        this.segments = segments;
    }

    public void addSegment(String segment) {
        this.segments.add(segment);
    }

    public String getPermalink() {
        return this.permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

}
