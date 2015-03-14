/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import java.util.Date;
import pl.eod.encje.KomKolejka;
import pl.eod.encje.KomKolejkaJpaController;

/**
 *
 * @author arti01
 */
public class MailWysylkaTask extends Task {

    @Override
    public void execute(TaskExecutionContext tec) throws RuntimeException {
        //System.out.println("wysylkaMaili");
        KomKolejkaJpaController kkc = new KomKolejkaJpaController();
        for (KomKolejka kk : kkc.findNiewyslane()) {
            //System.out.println("wysylkaMaili" + kk.getTemat() + kk.getTresc() + kk.getAdresList());
            MailWyslij mw = new MailWyslij(kk.getTemat(), kk.getTresc(), kk.getAdresList());
            try {
                mw.wyslij();
                kk.setStatus(1);
                Date data=new Date();
                kk.setDataWysylk(data);
                kkc.edit(kk);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
