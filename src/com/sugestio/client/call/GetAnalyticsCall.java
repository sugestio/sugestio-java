package com.sugestio.client.call;

import com.sugestio.client.SugestioConfig;
import com.sugestio.client.SugestioClient.ResourceType;
import com.sugestio.client.SugestioClient.Verb;
import com.sugestio.client.SugestioException;
import com.sugestio.client.SugestioResult;
import com.sugestio.client.model.Analytics;
import com.sugestio.client.model.Report;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;


public class GetAnalyticsCall extends Call implements Callable<SugestioResult<Analytics>> {
    
    private Integer limit = null;
    

    public GetAnalyticsCall(Client jClient, SugestioConfig config, Integer limit) {
        super(jClient, config, ResourceType.ANALYTICS);
        this.limit = limit;
    }

    @Override
    public SugestioResult<Analytics> call() throws SugestioException {
        return get();
    }

    private SugestioResult<Analytics> get() throws SugestioException {
        
        WebResource webResource = jClient.resource(config.getBaseUri()).path(getUri(Verb.GET, resourceType));

        if (limit != null) {
            webResource = webResource.queryParam("limit", limit.toString());
        }
        
        webResource.addFilter(getOauthFilter());
        Builder builder = webResource.type(MediaType.TEXT_XML_TYPE);

        SugestioResult<Analytics> result = null;

        try {

            //System.out.println("GET " + webResource.getURI());
            ClientResponse cr = builder.get(ClientResponse.class);            
            result = new SugestioResult<Analytics>(cr.getClientResponseStatus());

            if (cr.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
                String raw = cr.getEntity(String.class);
                Analytics analytics = new Analytics(raw);
                result.setEntity(analytics);
            } else {
                // handle 4xx and 5xx response codes
                result.setMessage(cr.getEntity(String.class));
            }            

        } catch (Exception e) {

            // handle local problems such as network issues
            result = new SugestioResult<Analytics>(false);
            result.setMessage(e.getMessage());

        } finally {

            result.setVerb(Verb.GET);
            result.setUri(webResource.getURI());            
            return result;
        }
        
    }    

}
