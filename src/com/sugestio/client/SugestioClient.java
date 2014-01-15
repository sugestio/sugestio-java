package com.sugestio.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sugestio.client.call.DeleteCall;
import com.sugestio.client.call.DeleteRecommendationCall;
import com.sugestio.client.call.GetAnalyticsCall;
import com.sugestio.client.call.GetItemCall;
import com.sugestio.client.call.GetRecommendationsCall;
import com.sugestio.client.call.PostCall;
import com.sugestio.client.call.RecommendationFilter;
import com.sugestio.client.model.Analytics;
import com.sugestio.client.model.Consumption;
import com.sugestio.client.model.Item;
import com.sugestio.client.model.Recommendation;
import com.sugestio.client.model.Report;
import com.sugestio.client.model.User;
import com.sugestio.client.request.PostConsumptionsRequest;
import com.sugestio.client.request.PostItemsRequest;
import com.sugestio.client.request.PostUsersRequest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class SugestioClient {
    
    private SugestioConfig config;
    private Client jClient;
    private ExecutorService executor;

    //<editor-fold defaultstate="collapsed" desc=" Enums ">
                
    public enum ResourceType {
        USER,
        ITEM,
        CONSUMPTION,
        RECOMMENDATION,
        SIMILAR,
        ANALYTICS
    }    
    
    public enum PartitionType {
        CATEGORY,
        SEGMENT
    }

    public enum Verb {
        GET,
        POST,
        PUT,
        DELETE
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Init and Shutdown ">

    /**
     * Creates a new instance of the SugestioClient with the given access credentials.
     * @param account your account key
     * @param secret your secret key
     */
    public SugestioClient(String account, String secret) {

        config = new SugestioConfig(account, secret);
        init();
    }

    /**
     * Creates a new instance of the SugestioClient with the given access credentials and concurrency options.
     * @param account your account key
     * @param secret your secret key
     * @param bulkMaxCount number of users, items or consumptions to submit in a single request
     * @param bulkThreads number of concurrent request
     */
    public SugestioClient(String account, String secret, int bulkMaxCount, int bulkThreads) {
        
        config = new SugestioConfig(account, secret);
        config.setBulkMaxCount(bulkMaxCount);
        config.setBulkThreads(bulkThreads);

        init();
    }

    /**
     * Creates a new instance of the SugestioClient with the given Config object.
     * This constructor allows for more customization of the client behaviour.
     * @param config
     */
    public SugestioClient(SugestioConfig config) {
        
        this.config = config;
        init();
    }

    /**
     * Initializes the Jersey REST client and the Executor object.
     */
    private void init() {

        ClientConfig clientConfig = new DefaultClientConfig();
        this.jClient = Client.create(clientConfig);
        this.jClient.setConnectTimeout(config.getConnectTimeout());
        this.jClient.setReadTimeout(config.getReadTimeout());
        executor = Executors.newFixedThreadPool(config.getBulkThreads());
    }

    /**
     * Initiates an orderly shutdown of the client.
     * Any pending web service requests are executed,
     * but new requests will be rejected.
     * @throws Exception
     */
    public void shutdown() {

        try {
            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Recommendations and Similar items ">

    /**
     * Gets recommendations for the given userId
     * @param userId id of the user
     * @return a list of recommendations
     * @throws SugestioException
     */
    public List<Recommendation> getRecommendations(String userId) throws SugestioException {
        return getRecommendations(userId, null, null);
    }
    
    /**
     * Gets recommendations for the given userId
     * @param userId id of the user
     * @param filter recommendation filter
     * @return a filtered list of recommendations
     * @throws SugestioException
     */
    public List<Recommendation> getRecommendations(String userId, RecommendationFilter filter) throws SugestioException {
    	
    	GetRecommendationsCall gc = new GetRecommendationsCall(
                jClient, config, ResourceType.RECOMMENDATION,
                userId, null, filter);
    	
    	return gc.call().getEntity();
    }

    /**
     * Gets recommendations for the given userId, limited to the given partition
     * @param userId id of the user
     * @param partitionType limit recommendations to one category, one product segment, ...
     * @param partitionId id of the category or segment
     * @return a list of recommendations
     * @throws SugestioException
     */
    public List<Recommendation> getRecommendations(String userId, PartitionType partitionType, String partitionId) throws SugestioException {

        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);        

        SugestioResult<List<Recommendation>> result = 
                getSingleRecommendation(getRecommendations(userIds, partitionType, partitionId));

        List<Recommendation> recommendations = result.getEntity();

        if (recommendations == null) {
            throw new SugestioException(result);
        }

        return recommendations;       
        
    }

    /**
     * Gets recommendations for the given userIds
     * @param userIds a list of userIds
     * @return for each user, a list of recommendations
     */
    public Map<String, SugestioResult<List<Recommendation>>> getRecommendations(List<String> userIds) {
        return getRecommendations(userIds, null, null);
    }

    /**
     * Gets recommendations for the given userIds, limited to the given partition
     * @param userIds a list of userIds
     * @param partitionType limit recommendations to one category, one segment, ...
     * @param partitionId id of the category or segment
     * @return for each user, a list of recommendations
     */
    public Map<String, SugestioResult<List<Recommendation>>> getRecommendations(List<String> userIds, PartitionType partitionType, String partitionId) {
        
        Map<Future<SugestioResult<List<Recommendation>>>, String> futures =
                Collections.synchronizedMap(new HashMap<Future<SugestioResult<List<Recommendation>>>, String>());

        for (String userid : userIds) {

            Callable<SugestioResult<List<Recommendation>>> call =
                    new GetRecommendationsCall(
                        jClient, config, ResourceType.RECOMMENDATION,
                        userid, null, partitionType, partitionId);
            
            Future<SugestioResult<List<Recommendation>>> future = executor.submit(call);
            futures.put(future, userid);
        }

        return collectRecommendations(futures);
    }

    /**
     * Gets items that are similar to the given item
     * @param itemId id of the item
     * @return list of similar items
     * @throws SugestioException
     */
    public List<Recommendation> getSimilar(String itemId) throws SugestioException {
        return getSimilar(itemId, null, null);
    }
    
    /**
     * Gets items that are similar to the given item
     * @param itemId id of the item
     * @param filter recommendation filter
     * @return filtered list of similar items
     * @throws SugestioException
     */
    public List<Recommendation> getSimilar(String itemId, RecommendationFilter filter) throws SugestioException {
    	
    	GetRecommendationsCall gc = new GetRecommendationsCall(
                jClient, config, ResourceType.RECOMMENDATION,
                null, itemId, filter);
    	
    	return gc.call().getEntity();
    }

    /**
     * Gets items that are similar to the given item, limited to the given partition
     * @param itemId id of the item
     * @param partitionType limit items to one category, one segment, ...
     * @param partitionId id of the category or segment
     * @return list of similar items
     * @throws SugestioException
     */
    public List<Recommendation> getSimilar(String itemId, PartitionType partitionType, String partitionId) throws SugestioException {

        List<String> itemIds = new ArrayList<String>();
        itemIds.add(itemId);

        Map<String, SugestioResult<List<Recommendation>>> results = getSimilar(itemIds, partitionType, partitionId);

        SugestioResult<List<Recommendation>> result = getSingleRecommendation(results);
        List<Recommendation> recommendations = result.getEntity();

        if (recommendations == null) {
            throw new SugestioException(result);
        }

        return recommendations;
    }

    /**
     * Gets similar items for the given itemIds
     * @param itemIds list of item ids
     * @return for each item, a list of similar items
     * @throws SugestioException
     */
    public Map<String, SugestioResult<List<Recommendation>>> getSimilar(List<String> itemIds) throws SugestioException {
        return getSimilar(itemIds, null, null);
    }

    /**
     * Gets similar items for the given itemIds, limited to the given partition
     * @param itemIds the items for which to get similar items
     * @param partitionType limit items to one category, one segment, ...
     * @param partitionId id of the category or segment
     * @return for each item, a list of similar items
     * @throws SugestioException
     */
    public Map<String, SugestioResult<List<Recommendation>>> getSimilar(List<String> itemIds, PartitionType partitionType, String partitionId) throws SugestioException {

        Map<Future<SugestioResult<List<Recommendation>>>, String> futures =
                Collections.synchronizedMap(new HashMap<Future<SugestioResult<List<Recommendation>>>, String>());

        for (String itemid : itemIds) {

            Callable<SugestioResult<List<Recommendation>>> call =
                    new GetRecommendationsCall(
                        jClient, config, ResourceType.SIMILAR,
                        null, itemid, partitionType, partitionId);

            Future<SugestioResult<List<Recommendation>>> future = executor.submit(call);
            futures.put(future, itemid);
        }

        return collectRecommendations(futures);
    }


    /**
     * Indicate that the user did not appreciate a certain recommendation. This item will never return in
     * the user's recommendations
     * @param userId userId
     * @param itemId itemId
     * @return result
     * @throws SugestioException
     */
    public SugestioResult<String> deleteRecommendation(String userId, String itemId) throws SugestioException {

        Callable<SugestioResult<String>> call = new DeleteRecommendationCall(jClient, config, userId, itemId);
        Future<SugestioResult<String>> future = executor.submit(call);
        SugestioResult<String> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<String>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Consumptions ">

    /**
     * Submit a single consumption
     * @param consumption
     * @return result
     * @throws SugestioException
     */
    public SugestioResult<String> addConsumption(Consumption consumption) throws SugestioException {

        List<Consumption> consumptions = new ArrayList<Consumption>();
        consumptions.add(consumption);        
        
        SugestioResult<String> result = getSingleResult(addConsumptions(consumptions));

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }

    /**
     * Efficient method for adding multiple consumptions.
     * @param consumptions list of consumptions
     * @return result     
     */
    public Map<List<Consumption>, SugestioResult<String>> addConsumptions(List<Consumption> consumptions) {

        List<List<Consumption>> sublists = divide(consumptions);

        Map<Future<SugestioResult<String>>, List<Consumption>> futures =
                Collections.synchronizedMap(new HashMap<Future<SugestioResult<String>>, List<Consumption>>());

        for (List<Consumption> sublist : sublists) {
            PostConsumptionsRequest pcr = new PostConsumptionsRequest();
            pcr.setList(sublist);
            Callable<SugestioResult<String>> call = new PostCall(jClient, config, ResourceType.CONSUMPTION, pcr);
            Future<SugestioResult<String>> future = executor.submit(call);
            futures.put(future, sublist);
        }

        return collectResults(futures);
    }
    
    /**
     * Deletes the consumption data of the given user. The user's metadata is not deleted.
     * @param userId
     * @return
     * @throws SugestioException
     */
    public SugestioResult<String> deleteUserConsumptions(String userId) throws SugestioException {
    
    	Callable<SugestioResult<String>> call = new DeleteCall(jClient, config, ResourceType.CONSUMPTION, userId, null, null);
        Future<SugestioResult<String>> future = executor.submit(call);
        SugestioResult<String> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<String>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }
    
    /**
     * Deletes the consumption identified by the given consumptionId.
     * @param consumptionId
     * @return
     * @throws SugestioException
     */
    public SugestioResult<String> deleteConsumption(String consumptionId) throws SugestioException {
    	
    	Callable<SugestioResult<String>> call = new DeleteCall(jClient, config, ResourceType.CONSUMPTION, null, null, consumptionId);
        Future<SugestioResult<String>> future = executor.submit(call);
        SugestioResult<String> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<String>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }
    
    /**
     * Deletes the consumptions identified by the given consumptionIds
     * @param consumptionIds
     * @return
     * @throws SugestioException
     */
    public Map<String, SugestioResult<String>> deleteConsumptions(List<String> consumptionIds) throws SugestioException {
    	
    	Map<Future<SugestioResult<String>>, String> futures =
                Collections.synchronizedMap(new HashMap<Future<SugestioResult<String>>, String>());

        for (String consumptionId : consumptionIds) {

            Callable<SugestioResult<String>> call =
            		new DeleteCall(jClient, config, ResourceType.CONSUMPTION, 
            				null, null, consumptionId);

            Future<SugestioResult<String>> future = executor.submit(call);
            futures.put(future, consumptionId);
        }

        return collectDeleteResults(futures);
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Users ">

    /**
     * Add or update a single user.
     * @param user
     * @return result
     * @throws SugestioException
     */
    public SugestioResult<String> addUser(User user) throws SugestioException {

        List<User> users = new ArrayList<User>();
        users.add(user);

        SugestioResult<String> result = getSingleResult(addUsers(users));

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }

    /**
     * Efficient method for adding multiple users
     * @param users list of users
     * @return result
     */
    public Map<List<User>, SugestioResult<String>> addUsers(List<User> users) {

        List<List<User>> sublists = divide(users);

        Map<Future<SugestioResult<String>>, List<User>> futures =
                Collections.synchronizedMap(new HashMap<Future<SugestioResult<String>>, List<User>>());

        for (List<User> sublist : sublists) {
            PostUsersRequest req = new PostUsersRequest();
            req.setList(sublist);
            Callable<SugestioResult<String>> call = new PostCall(jClient, config, ResourceType.USER, req);
            Future<SugestioResult<String>> future = executor.submit(call);
            futures.put(future, sublist);
        }

        return collectResults(futures);
    }
    
    /**
     * Deletes the metadata of the given user. The user's consumptions are not deleted.
     * @param userId
     * @return
     * @throws SugestioException
     */
    public SugestioResult<String> deleteUser(String userId) throws SugestioException {
    
    	Callable<SugestioResult<String>> call = new DeleteCall(jClient, config, ResourceType.USER, userId, null, null);
        Future<SugestioResult<String>> future = executor.submit(call);
        SugestioResult<String> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<String>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Items ">

    /**
     * Retrieves the item metadata for the given itemId.
     * @param itemId
     * @return item metadata
     * @throws SugestioException
     */
    public Item getItem(String itemId) throws SugestioException {
    	
    	Callable<SugestioResult<Item>> call = new GetItemCall(jClient, config, itemId);
        Future<SugestioResult<Item>> future = executor.submit(call);
        SugestioResult<Item> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<Item>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result.getEntity();    	
    }
    
    /**
     * Add or update a single item.
     * @param item
     * @return
     * @throws SugestioException
     */
    public SugestioResult<String> addItem(Item item) throws SugestioException {

        List<Item> items = new ArrayList<Item>();
        items.add(item);

        SugestioResult<String> result = getSingleResult(addItems(items));

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }    
    
    /**
     * Efficient method for adding multiple items
     * @param items list of items
     * @return result
     */
    public Map<List<Item>, SugestioResult<String>> addItems(List<Item> items) {

        List<List<Item>> sublists = divide(items);

        Map<Future<SugestioResult<String>>, List<Item>> futures =
                Collections.synchronizedMap(new HashMap<Future<SugestioResult<String>>, List<Item>>());

        for (List<Item> sublist : sublists) {
            PostItemsRequest req = new PostItemsRequest();
            req.setList(sublist);
            Callable<SugestioResult<String>> call = new PostCall(jClient, config, ResourceType.ITEM, req);
            Future<SugestioResult<String>> future = executor.submit(call);
            futures.put(future, sublist);
        }

        return collectResults(futures);        
    }
    
    /**
     * Deletes the metadata of the given user. The user's consumptions are not deleted.
     * @param itemId
     * @return
     * @throws SugestioException
     */
    public SugestioResult<String> deleteItem(String itemId) throws SugestioException {
    
    	Callable<SugestioResult<String>> call = new DeleteCall(jClient, config, ResourceType.ITEM, null, itemId, null);
        Future<SugestioResult<String>> future = executor.submit(call);
        SugestioResult<String> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<String>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Analytics ">
    /**
     * Get analytics data. 
     * @param limit number of reports
     * @return list of reports
     * @throws SugestioException
     */
    public List<Report> getAnalytics(Integer limit) throws SugestioException {

        Callable<SugestioResult<Analytics>> call = new GetAnalyticsCall(jClient, config, limit);
        Future<SugestioResult<Analytics>> future = executor.submit(call);
        SugestioResult<Analytics> result = null;

        try {
            result = future.get();
        } catch (Exception e) {
            result = new SugestioResult<Analytics>(false);
            result.setMessage(e.getMessage());
        }

        if (!result.isOK())
            throw new SugestioException(result);

        return result.getEntity().getReports();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Helper methods ">

    private <T> Map<String, SugestioResult<List<Recommendation>>> collectRecommendations(Map<Future<SugestioResult<List<Recommendation>>>, String> futures) {

        Map<String, SugestioResult<List<Recommendation>>> results =
                Collections.synchronizedMap(new HashMap<String, SugestioResult<List<Recommendation>>>());

        for (Entry<Future<SugestioResult<List<Recommendation>>>, String> entry : futures.entrySet()) {
            
            try {

                results.put(entry.getValue(), entry.getKey().get());

            } catch (Exception ex) {

                // exceptions are caught within the call method of the callable object
                // so we should never end up in this catch block.
                // create an error result object 

                SugestioResult<List<Recommendation>> error = new SugestioResult<List<Recommendation>>(false);
                error.setMessage(ex.getMessage());
                results.put(entry.getValue(), error);
            }
        }

        return results;

    }

    private SugestioResult<List<Recommendation>> getSingleRecommendation(Map<String, SugestioResult<List<Recommendation>>> results) throws SugestioException {

        if (results.size() != 1) {
            //throw new SugestioException(null, "");
        }

        SugestioResult<List<Recommendation>> result = null;

        for (Entry<String, SugestioResult<List<Recommendation>>> entry : results.entrySet()) {
            result = entry.getValue();
        }        

        return result;
    }
    
    private Map<String, SugestioResult<String>> collectDeleteResults(Map<Future<SugestioResult<String>>, String> futures) {

        Map<String, SugestioResult<String>> results =
                Collections.synchronizedMap(new HashMap<String, SugestioResult<String>>());

        for (Entry<Future<SugestioResult<String>>, String> entry : futures.entrySet()) {
            try {
                results.put(entry.getValue(), entry.getKey().get());
                // we skip over exceptions, if the client doesn't receive a response for a set of requests,
                // it must assume that the request failed
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                //throw new SugestioException(null, null, "Interrupted!");
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                // create 500 status object here
                //throw new SugestioException(null, null, "Execution exception!");
            }
        }

        return results;

    }

    private <T> Map<List<T>, SugestioResult<String>> collectResults(Map<Future<SugestioResult<String>>, List<T>> futures) {

        Map<List<T>, SugestioResult<String>> results =
                Collections.synchronizedMap(new HashMap<List<T>, SugestioResult<String>>());

        for (Entry<Future<SugestioResult<String>>, List<T>> entry : futures.entrySet()) {
            try {
                results.put(entry.getValue(), entry.getKey().get());
                // we skip over exceptions, if the client doesn't receive a response for a set of requests,
                // it must assume that the request failed
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                //throw new SugestioException(null, null, "Interrupted!");
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                // create 500 status object here
                //throw new SugestioException(null, null, "Execution exception!");
            }
        }

        return results;

    }

    private <T> SugestioResult<String> getSingleResult(Map<List<T>, SugestioResult<String>> results) throws SugestioException {

        if (results.size() != 1) {
            //throw new SugestioException(null, "");
        }

        SugestioResult<String> result = null;

        for (Entry<List<T>, SugestioResult<String>> entry : results.entrySet()) {
            result = entry.getValue();
        }

        return result;
    }

    private <T> List<List<T>> divide(List<T> list) {

        List<List<T>> sublists = new ArrayList<List<T>>();

        for (int fromIndex=0; fromIndex<list.size(); fromIndex+=config.getBulkMaxCount()) {
            int toIndex = Math.min(fromIndex + config.getBulkMaxCount(), list.size());
            sublists.add(list.subList(fromIndex, toIndex));
        }

        return sublists;
    }

    //</editor-fold>
    
}
