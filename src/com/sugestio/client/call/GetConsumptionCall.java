package com.sugestio.client.call;

import java.util.concurrent.Callable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;

import com.sugestio.client.SugestioClient.ResourceType;
import com.sugestio.client.SugestioClient.Verb;
import com.sugestio.client.SugestioConfig;
import com.sugestio.client.SugestioException;
import com.sugestio.client.SugestioResult;
import com.sugestio.client.model.Consumption;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class GetConsumptionCall extends Call implements Callable<SugestioResult<Consumption>> {
	
	public GetConsumptionCall(Client jClient, SugestioConfig config, String consumptionId) {
        super(jClient, config, ResourceType.CONSUMPTION, null, null, consumptionId);        
    }

    @Override
    public SugestioResult<Consumption> call() throws SugestioException {
        return get();
    }

    private SugestioResult<Consumption> get() throws SugestioException {
        
        WebResource webResource = jClient.resource(config.getBaseUri()).path(getUri(Verb.GET, resourceType));
        
        webResource.addFilter(getOauthFilter());
        Builder builder = webResource.type(MediaType.TEXT_XML_TYPE);

        SugestioResult<Consumption> result = null;

        try {

            //System.out.println("GET " + webResource.getURI());
            ClientResponse cr = builder.get(ClientResponse.class);            
            result = new SugestioResult<Consumption>(cr.getClientResponseStatus());

            if (cr.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
                Consumption consumption = cr.getEntity(Consumption.class);                
                result.setEntity(consumption);
            } else {
                // handle 4xx and 5xx response codes
                result.setMessage(cr.getEntity(String.class));
            }            

        } catch (Exception e) {

            // handle local problems such as network issues
            result = new SugestioResult<Consumption>(false);
            result.setMessage(e.getMessage());

        }
        
        result.setVerb(Verb.GET);
        result.setUri(webResource.getURI());            
        return result;
        
    }    

}
