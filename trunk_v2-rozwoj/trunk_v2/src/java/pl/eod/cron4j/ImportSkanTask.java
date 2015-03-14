/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.eod2.encje.DcPlikImportJpaController;

/**
 *
 * @author arti01
 */
public class ImportSkanTask extends Task {

    @Override
    public void execute(TaskExecutionContext tec) throws RuntimeException {
        DcPlikImportJpaController dcpC=new DcPlikImportJpaController();
        try {
            dcpC.importFromDir();
        } catch (IOException ex) {
            Logger.getLogger(ImportSkanTask.class.getName()).log(Level.SEVERE, null, ex);
        }
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
