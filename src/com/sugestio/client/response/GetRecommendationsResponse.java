package com.sugestio.client.response;

import com.sugestio.client.model.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;


@javax.xml.bind.annotation.XmlRootElement(name="recommendations")
public class GetRecommendationsResponse {

    @XmlElement(name="recommendation")
    private List<Recommendation> list;
            
    public GetRecommendationsResponse() {
        
        if (list == null)
            list = new ArrayList<Recommendation>();     
        
    }
    
    public void add(Recommendation r) {
        list.add(r);
    }
    
    public List<Recommendation> getList() {
        return list;
    }
}
