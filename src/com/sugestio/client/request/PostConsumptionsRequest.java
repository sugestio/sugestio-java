package com.sugestio.client.request;

import com.sugestio.client.model.Consumption;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="consumptions")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostConsumptionsRequest {
        
    @XmlElement(name="consumption")
    private List<Consumption> list;
    
    public PostConsumptionsRequest() {
        
        if (list == null)
            list = new ArrayList<Consumption>();
        
    }
    
    public void add(Consumption consumption) {
        list.add(consumption);
    }
    
    public List<Consumption> getList() {
        return list;
    }
    
    public void setList(List<Consumption> list) {
        this.list = list;
    }
}
