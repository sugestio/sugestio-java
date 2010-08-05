package com.sugestio.client;

import java.io.PrintStream;
import java.net.URI;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;


public class SugestioResult<T> {

    private boolean ok;
    private SugestioClient.Verb verb;
    private URI uri;
    private StatusType statusType = null;
    private String message = null;
    private T entity = null;


    public SugestioResult(StatusType statusType) {
        this.statusType = statusType;
        this.ok = (statusType.getFamily() == Family.SUCCESSFUL);
    }

    public SugestioResult(boolean ok) {
        this.ok = ok;
    }
    

    /**
     * The response status received from the server.
     * Returns null if the request never reached the server due to a client side problem,
     * such as a network issue.
     * @return status
     */
    public StatusType getStatusType() {
        return this.statusType;
    }

    /**
     * Indicates if the request was succesful. Returns false if there was a server side or client side problem.
     * @return success
     */
    public boolean isOK() {
        return ok;
    }

    /**
     * Contains the textual response from the server (if any),
     * or a problem description in case of a local error.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the entity
     */
    public T getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**     
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * @return the verb
     */
    public SugestioClient.Verb getVerb() {
        return verb;
    }

    /**
     * @param verb the verb to set
     */
    public void setVerb(SugestioClient.Verb verb) {
        this.verb = verb;
    }

    /**
     * Prints a full report of this service request. If the request succeeded, the output goes to System.out.
     * If there was a problem, the output goes to System.err.
     */
    public void printReport() {

        if (isOK())
            printReport(System.out);
        else
            printReport(System.err);
        
    }

    /**
     * Sends a full report of this service request to a PrintStream
     * @param ps the printstream
     */
    public void printReport(PrintStream ps) {

        ps.println(getVerb() + " " + getUri());

        if (getStatusType() == null) {

            // the request never reached the service due to a local problem i.e. a network problem
            ps.println("\tLocal problem:");
            ps.println("\t" + getMessage());

        } else if (getStatusType().getFamily() == Family.SUCCESSFUL) {

            ps.println("\t" + getStatusPhrase());

        } else if (getStatusType().getFamily() == Family.CLIENT_ERROR) {

            // the service rejected our request due to wrong credentials, non-existing resource, ...
            ps.println("\tClient side problem:");
            ps.println("\t" + getStatusPhrase() + ": " + getMessage());

        } else if (getStatusType().getFamily() == Family.SERVER_ERROR) {

            // there was a problem on the server side
            ps.println("\tServer-side problem:");
            ps.println("\t" + getStatusPhrase() + ": " + getMessage());

        } else {
            // Response codes 1xx and 3xx
            ps.println("\tUnexpected result:");
            ps.println("\t" + getStatusPhrase() + ": " + getMessage());
        }

    }

    private String getStatusPhrase() {
        return getStatusType().getStatusCode() + " " + getStatusType().getReasonPhrase();
    }
}
