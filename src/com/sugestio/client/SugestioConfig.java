package com.sugestio.client;


public class SugestioConfig {

    private String account;
    private String secret;
    private String baseUri = "http://api.sugestio.com";

    private int bulkMaxCount = 100;
    private int bulkThreads = 5;

    private int readTimeout = 10000;
    private int connectTimeout = 5000;

    public SugestioConfig(String account, String secret) {
        this.account = account;
        this.secret = secret;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return the baseUri
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * The base URI of the Sugestio web service. No trailing slash.
     * @param baseUri the baseUri to set.
     */
    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * @return the bulkMaxCount
     */
    public int getBulkMaxCount() {
        return bulkMaxCount;
    }

    /**
     * @param bulkMaxCount the bulkMaxCount to set
     */
    public void setBulkMaxCount(int bulkMaxCount) {
        this.bulkMaxCount = bulkMaxCount;
    }

    /**
     * @return the bulkThreads
     */
    public int getBulkThreads() {
        return bulkThreads;
    }

    /**
     * @param bulkThreads the bulkThreads to set
     */
    public void setBulkThreads(int bulkThreads) {
        this.bulkThreads = bulkThreads;
    }

    /**
     * Read timeout interval property. How many milliseconds to wait. Set to zero to wait forever.
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Read timeout interval property. How many milliseconds to wait. Set to zero to wait forever.
     * @param readTimeout the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * Connect timeout interval property. How many milliseconds to wait. Set to zero to wait forever.
     * @return the connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Connect timeout interval property. How many milliseconds to wait. Set to zero to wait forever.
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

}
