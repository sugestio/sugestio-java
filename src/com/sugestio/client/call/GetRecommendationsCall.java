package com.sugestio.client.call;

import com.sugestio.client.SugestioConfig;
import com.sugestio.client.SugestioClient.PartitionType;
import com.sugestio.client.SugestioClient.ResourceType;
import com.sugestio.client.SugestioClient.Verb;
import com.sugestio.client.SugestioException;
import com.sugestio.client.SugestioResult;
import com.sugestio.client.model.Recommendation;
import com.sugestio.client.response.GetRecommendationsResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import java.util.concurrent.Callable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;


public class GetRecommendationsCall extends Call implements Callable<SugestioResult<List<Recommendation>>> {
    
	private RecommendationFilter filter;
    private PartitionType partitionType;
    private String partitionId;


    public GetRecommendationsCall(Client jClient, SugestioConfig config, ResourceType resourceType, String userId, String itemId, PartitionType partitionType, String partitionId) {
        super(jClient, config, resourceType, userId, itemId);
        this.partitionType = partitionType;
        this.partitionId = partitionId;
    }
    
    public GetRecommendationsCall(Client jClient, SugestioConfig config, ResourceType resourceType, String userId, String itemId, RecommendationFilter filter) {
        super(jClient, config, resourceType, userId, itemId);
        this.filter = filter;
    }

    @Override
    public SugestioResult<List<Recommendation>> call() throws SugestioException {
        return get();
    }

    private SugestioResult<List<Recommendation>> get() throws SugestioException {
        
        WebResource webResource = jClient.resource(config.getBaseUri()).path(getUri(Verb.GET, resourceType));
        
        if (filter != null) {
        	webResource = webResource.queryParams(filter.toQueryParams());
        }

        if (partitionType != null && partitionId != null && partitionId.length() > 0) {
            webResource = webResource.queryParam(partitionType.name().toLowerCase(), partitionId);
        }
        
        webResource.addFilter(getOauthFilter());
        Builder builder = webResource.type(MediaType.TEXT_XML_TYPE);

        SugestioResult<List<Recommendation>> result = null;

        try {

            //System.out.println("GET " + webResource.getURI());
            ClientResponse cr = builder.get(ClientResponse.class);
            result = new SugestioResult<List<Recommendation>>(cr.getClientResponseStatus());            

            if (cr.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
                result.setEntity(cr.getEntity(GetRecommendationsResponse.class).getList());
            } else {
                // handle 4xx and 5xx response codes
                result.setMessage(cr.getEntity(String.class));
            }
            
        } catch (Exception e) {
            // handle local problems such as network issues
            result = new SugestioResult<List<Recommendation>>(false);
            result.setMessage(e.getMessage());
        }
        
        result.setVerb(Verb.GET);
        result.setUri(webResource.getURI());
        return result;
    }    

}
