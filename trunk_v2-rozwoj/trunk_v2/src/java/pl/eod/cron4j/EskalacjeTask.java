/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import pl.eod.encje.WnUrlopJpaController;

/**
 *
 * @author arti01
 */
public class EskalacjeTask extends Task {

    @Override
    public void execute(TaskExecutionContext tec) throws RuntimeException {
        WnUrlopJpaController wnuC=new WnUrlopJpaController();
        wnuC.eskalujCron();
        
    }

    @Override
    public boolean canBePaused() {
        return super.canBePaused(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canBeStopped() {
        return super.canBeStopped(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean supportsStatusTracking() {
        return super.supportsStatusTracking(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean supportsCompletenessTracking() {
        return super.supportsCompletenessTracking(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
