package com.sugestio.client.call;

import com.sugestio.client.SugestioClient;
import com.sugestio.client.SugestioConfig;
import com.sugestio.client.SugestioException;
import com.sugestio.client.SugestioResult;
import com.sugestio.client.model.Consumption;
import com.sugestio.client.response.GetConsumptionsResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import java.util.concurrent.Callable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;

/**
 *
 */
public class GetConsumptionHistoryCall extends Call implements Callable<SugestioResult<List<Consumption>>> {
    
    public GetConsumptionHistoryCall(Client jClient, SugestioConfig config, String userId, String itemId) {
        super(jClient, config, SugestioClient.ResourceType.CONSUMPTION, userId, itemId, null);        
    }

    @Override
    public SugestioResult<List<Consumption>> call() throws Exception {
        return get();
    }
    
    private SugestioResult<List<Consumption>> get() throws SugestioException {
        
        WebResource webResource = jClient.resource(config.getBaseUri()).path(getUri(SugestioClient.Verb.GET, resourceType));
        
        webResource.addFilter(getOauthFilter());
        WebResource.Builder builder = webResource.type(MediaType.TEXT_XML_TYPE);

        SugestioResult<List<Consumption>> result = null;

        try {

            //System.out.println("GET " + webResource.getURI());
            ClientResponse cr = builder.get(ClientResponse.class);            
            result = new SugestioResult<List<Consumption>>(cr.getClientResponseStatus());
            
            if (cr.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
                result.setEntity(cr.getEntity(GetConsumptionsResponse.class).getList());
            } else {
                // handle 4xx and 5xx response codes
                result.setMessage(cr.getEntity(String.class));
            }            

        } catch (Exception e) {

            // handle local problems such as network issues
            result = new SugestioResult<List<Consumption>>(false);
            result.setMessage(e.getMessage());

        } finally {

            result.setVerb(SugestioClient.Verb.GET);
            result.setUri(webResource.getURI());            
            return result;
        }
        
    }   
}