package com.sugestio.client.request;

import com.sugestio.client.model.Item;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="items")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostItemsRequest {
        
    @XmlElement(name="item")
    private List<Item> list;
    
    public PostItemsRequest() {
        
        if (list == null)
            list = new ArrayList<Item>();     
        
    }
    
    public void add(Item item) {
        list.add(item);
    }
    
    public List<Item> getList() {
        return list;
    }
    
    public void setList(List<Item> list) {
        this.list = list;
    }
}
