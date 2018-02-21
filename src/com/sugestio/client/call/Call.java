package com.sugestio.client.call;

import com.sugestio.client.SugestioConfig;
import com.sugestio.client.SugestioClient;
import com.sugestio.client.SugestioClient.ResourceType;
import com.sugestio.client.SugestioClient.Verb;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;


public abstract class Call {

    protected Client jClient;
    protected SugestioConfig config;
    protected ResourceType resourceType;
    protected String userId;
    protected String itemId;
    protected String consumptionId;


    protected Call(Client jClient, SugestioConfig config, ResourceType resourceType) {
        this(jClient, config, resourceType, null, null, null);
    }

    protected Call(Client jClient, SugestioConfig config, ResourceType resourceType, 
    		String userId, String itemId, String consumptionId) {
        this.jClient = jClient;
        this.config = config;
        this.resourceType = resourceType;
        this.userId = userId;
        this.itemId = itemId;
        this.consumptionId = consumptionId;
    }

    protected String getUri(Verb verb, ResourceType resourceType) {

        String uri = "/sites/" + config.getAccount();

        if (resourceType == SugestioClient.ResourceType.RECOMMENDATION) {

            uri += "/users/" + userId + "/recommendations";

            if (verb == Verb.GET) {
                uri += ".xml";
            } else if (verb == Verb.DELETE) {
                uri += "/" + itemId;
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.SIMILAR) {

            uri += "/items/" + itemId + "/similar";

            if (verb == Verb.GET) {
                uri += ".xml";
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.CONSUMPTION) {

            if (verb == Verb.POST) {
                uri += "/consumptions.xml";
            } else if (verb == Verb.GET) {
                if (consumptionId != null) {
                    uri += "/consumptions/" + consumptionId + ".xml";
                } else if (userId != null) {
                    uri += "/users/" + userId;
                    uri += "/consumptions";
                    if (itemId != null) {
                        uri += "/" + itemId;
                    }
                    uri += ".xml";
                } else {
                    return null;
                }
            } else if (verb == Verb.DELETE) {
            	if (consumptionId != null) {
            		// delete one consumption in particular
            		uri += "/consumptions/" + consumptionId + ".xml";
            	} else if (userId != null && itemId != null) {
            		// delete all user-item pair consumptions
            		uri += "/users/" + userId + "/consumptions/" + itemId + ".xml";            		
            	} else if (userId != null) {
            		// delete all consumptions of this user
            		uri += "/users/" + userId + "/consumptions.xml"; 
            	} else {
            		return null;
            	}
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.ITEM) {
        	
            if (verb == Verb.POST) {
                uri += "/items";
            } else if ((verb == Verb.GET || verb == Verb.DELETE) && itemId != null) {
            	uri += "/items/" + itemId + ".xml";
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.USER) {

            if (verb == Verb.POST) {
                uri += "/users";
            } else if (verb == Verb.DELETE && userId != null) {
            	uri += "/users/" + userId + ".xml";
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.ANALYTICS) {

            if (verb == Verb.GET) {
                uri += "/analytics.csv";
            }  else {
                return null;
            }
        }

        return uri;
    }

    protected OAuthClientFilter getOauthFilter() {

        OAuthParameters params = new OAuthParameters();
        params.signatureMethod("HMAC-SHA1").consumerKey(config.getAccount()).nonce().timestamp().version();

        // OAuth secrets to access resource
        OAuthSecrets secrets = new OAuthSecrets();
        secrets.consumerSecret(config.getSecret());

        // if parameters and secrets remain static, filter can be added to each web resource
        return new OAuthClientFilter(jClient.getProviders(), params, secrets);

    }
}
