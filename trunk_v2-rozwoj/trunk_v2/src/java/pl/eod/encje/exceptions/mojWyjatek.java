/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje.exceptions;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author arti01
 */
public class mojWyjatek extends org.eclipse.persistence.exceptions.DatabaseException {
    private static final long serialVersionUID = 1L;
    private List<String> messages;
    public mojWyjatek(String message) {
        super(message);
        System.err.println("dfasdasd");    
    }

    public mojWyjatek(SQLException exception) {
        super(exception);
        System.err.println("dfasdsaaaaaaaaaaaaaaaaaasd");   
    }

    public List<String> getMessages() {
        return messages;
    }
    
    
}
