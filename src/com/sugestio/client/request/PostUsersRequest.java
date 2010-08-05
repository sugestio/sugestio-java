package com.sugestio.client.request;

import com.sugestio.client.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kris
 */
@XmlRootElement(name="users")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostUsersRequest {
        
    @XmlElement(name="user")
    private List<User> list;
    
    public PostUsersRequest() {
        
        if (list == null)
            list = new ArrayList<User>();     
        
    }
    
    public void add(User user) {
        list.add(user);
    }
    
    public List<User> getList() {
        return list;
    }
    
    public void setList(List<User> list) {
        this.list = list;
    }
}
