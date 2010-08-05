package com.sugestio.client;


public class SugestioException extends Exception {
    
    private SugestioResult sugestioResult;
    
    
    public SugestioException(SugestioResult sugestioResult) {
        super(sugestioResult.getMessage());
        this.sugestioResult = sugestioResult;
    }
    
    public SugestioResult getSugestioResult() {
        return this.sugestioResult;
    }
    
}
