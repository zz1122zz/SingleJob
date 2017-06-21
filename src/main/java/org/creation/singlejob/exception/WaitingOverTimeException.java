package org.creation.singlejob.exception;

public class WaitingOverTimeException extends Exception {

    public WaitingOverTimeException(String uniqueKey) {
        super("WaitingOverTime : "+uniqueKey);
    }

    /**	
     * Member Description
     */
    
    private static final long serialVersionUID = 1L;

}
