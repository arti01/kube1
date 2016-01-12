/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import it.sauronsoftware.cron4j.TaskExecutor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.eod2.encje.DcPlikImportJpaController;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class ImportSkanTask extends Task {

    @Override
    public void execute(TaskExecutionContext tec) throws RuntimeException {
        for (TaskExecutor taskE : tec.getTaskExecutor().getScheduler().getExecutingTasks()) {
            if (taskE.supportsCompletenessTracking()) {
                if (taskE.getCompleteness() > 0) {
                    System.err.println("trwa import plikow - wychodze");
                    return;
                }
            }
        }
        //System.err.println("start import plikow");
        try {
            tec.setCompleteness(1);
            DcPlikImportJpaController dcpC = new DcPlikImportJpaController();
            dcpC.importFromDir();
            dcpC.czyscImport();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ImportSkanTask.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            tec.setCompleteness(0);
        }
        //System.err.println("stop import plikow");
    }

    @Override
    public boolean canBePaused() {
        return true;
        //return super.canBePaused(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canBeStopped() {
        return super.canBeStopped(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean supportsStatusTracking() {
        return true;
        //return super.supportsStatusTracking(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean supportsCompletenessTracking() {
        return true;
        //return super.supportsCompletenessTracking(); //To change body of generated methods, choose Tools | Templates.
    }

}
