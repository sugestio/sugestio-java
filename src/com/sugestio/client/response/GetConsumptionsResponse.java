package com.sugestio.client.response;

import com.sugestio.client.model.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;


@javax.xml.bind.annotation.XmlRootElement(name="consumptions")
public class GetConsumptionsResponse {

    @XmlElement(name="consumption")
    private List<Consumption> list;
            
    public GetConsumptionsResponse() {
        
        if (list == null)
            list = new ArrayList<Consumption>();     
        
    }
    
    public void add(Consumption c) {
        list.add(c);
    }
    
    public List<Consumption> getList() {
        return list;
    }
}
