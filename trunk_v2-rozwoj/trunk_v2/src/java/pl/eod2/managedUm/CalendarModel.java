/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedUm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import pl.eod2.encje.UmRezerwacje;

@ManagedBean(name = "CalendarModel")
@SessionScoped
public class CalendarModel implements CalendarDataModel {

    @ManagedProperty(value = "#{RezerwacjeMg}")
    private RezerwacjeMg rezerwacje;

    private static final String WEEKEND_DAY_CLASS = "wdc";
    private static final String BUSY_DAY_CLASS = "bdc";
    //private static final String BOUNDARY_DAY_CLASS = "rf-ca-boundary-dates";

    private boolean checkBusyDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        Calendar calplus = Calendar.getInstance();
        calplus.setTime(calendar.getTime());
        calplus.add(Calendar.DATE, 1);
        /*for (UmRezerwacje rezerwacja : rezerwacje.getUrzadzenie().getRezerwacjeList()) {
            if (rezerwacja.getDataOd().after(calplus.getTime())) {
                //System.err.println("11111111111");
                continue;
            }
            if (rezerwacja.getDataOd().before(calendar.getTime()) && rezerwacja.getDataDo().before(calendar.getTime())) {
                //System.err.println("2222222222222");
                continue;
            }
            return true;
        }*/
        return false;
    }

    private boolean checkWeekend(Calendar calendar) {
        return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
    }

    @Override
    public CalendarDataModelItem[] getData(Date[] dateArray) {
        CalendarDataModelItem[] modelItems = new CalendarModelItem[dateArray.length];
        Calendar current = GregorianCalendar.getInstance();
        //Calendar today = GregorianCalendar.getInstance();
        //today.setTime(new Date());
        for (int i = 0; i < dateArray.length; i++) {
            current.setTime(dateArray[i]);
            CalendarModelItem modelItem = new CalendarModelItem();
            if (checkBusyDay(current)) {
                modelItem.setEnabled(true);
                modelItem.setStyleClass(BUSY_DAY_CLASS);
            } else if (checkWeekend(current)) {
                modelItem.setEnabled(false);
                modelItem.setStyleClass(WEEKEND_DAY_CLASS);
            } else {
                modelItem.setEnabled(true);
                modelItem.setStyleClass("");
            }
            modelItems[i] = modelItem;
        }

        return modelItems;
    }

    @Override
    public Object getToolTip(Date date) {
        return null;
    }

    public RezerwacjeMg getRezerwacje() {
        return rezerwacje;
    }

    public void setRezerwacje(RezerwacjeMg rezerwacje) {
        this.rezerwacje = rezerwacje;
    }

}
