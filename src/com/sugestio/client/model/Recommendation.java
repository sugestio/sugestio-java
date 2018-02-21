package com.sugestio.client.model;

import java.util.Formatter;
import java.util.Locale;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class Recommendation implements Comparable<Recommendation> {
    
    @XmlElement(required = true)
    protected String itemid;
    @XmlElement(required = true)
    private Double score;
    @XmlElement(required = false)
    private String algorithm;
    @XmlElement(required=false)
    private Item item;
     
    
    public Recommendation() {
        
    }
    
    
    public Recommendation(String itemid, Double score) {
        this(itemid, score, "");
    }
    
    
    public Recommendation(String itemid, Double score, String algorithm) {
        this.itemid = itemid;
        this.score = score;
        this.algorithm = algorithm;
    }
    
    public Recommendation(String itemid, Double score, String algoritm, Item item) {
        this(itemid, score, algoritm);
        this.item = item;
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int compareTo(Recommendation o) {
        return score.compareTo(o.getScore());
    }
    
    @Override
    public String toString() {
        Formatter formatter = new Formatter(Locale.US);
        String out = formatter.format(
            "<recommendation><itemid>%s</itemid>%f<score></score><algorithm>%s</algorithm></recommendation>",             
            this.itemid, this.score, this.algorithm).toString();
        formatter.close();
        return out;
    }   
}
