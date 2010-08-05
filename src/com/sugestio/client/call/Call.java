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


    protected Call(Client jClient, SugestioConfig config, ResourceType resourceType) {
        this(jClient, config, resourceType, null, null);
    }

    protected Call(Client jClient, SugestioConfig config, ResourceType resourceType, String userId, String itemId) {
        this.jClient = jClient;
        this.config = config;
        this.resourceType = resourceType;
        this.userId = userId;
        this.itemId = itemId;
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
                uri += "/consumptions";
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.ITEM) {

            if (verb == Verb.POST) {
                uri += "/items";
            } else {
                return null;
            }

        } else if (resourceType == ResourceType.USER) {

            if (verb == Verb.POST) {
                uri += "/users";
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
