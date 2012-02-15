package com.sugestio.client.call;

import com.sugestio.client.SugestioClient.ResourceType;
import com.sugestio.client.SugestioClient.Verb;
import com.sugestio.client.SugestioConfig;
import com.sugestio.client.SugestioException;
import com.sugestio.client.SugestioResult;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.util.concurrent.Callable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;


public class DeleteRecommendationCall extends Call implements Callable<SugestioResult<String>> {

    public DeleteRecommendationCall(Client jClient, SugestioConfig config, String userId, String itemId) {
        super(jClient, config, ResourceType.RECOMMENDATION, userId, itemId, null);
    }

    @Override
    public SugestioResult<String> call() throws SugestioException {
        return delete();
    }

    private SugestioResult<String> delete() throws SugestioException {

        WebResource webResource = jClient.resource(config.getBaseUri()).path(getUri(Verb.DELETE, resourceType));
        webResource.addFilter(getOauthFilter());        
        Builder builder = webResource.type(MediaType.TEXT_XML_TYPE);

        SugestioResult<String> result = null;

        try {

            //System.out.println("POST " + webResource.getURI());
            ClientResponse cr = builder.delete(ClientResponse.class);
            result = new SugestioResult<String>(cr.getClientResponseStatus());

            // Handle 4xx and 5xx response codes
            if (cr.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
                result.setEntity(cr.getEntity(String.class));
            } else {            
            	result.setMessage(cr.getEntity(String.class));
            }

        } catch (Exception e) {

            // handle local problems such as network issues
            result = new SugestioResult<String>(false);
            result.setMessage(e.getMessage());

        }
        
        result.setVerb(Verb.DELETE);
        result.setUri(webResource.getURI());
        return result;
    }
    
}
