/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author arti01
 */
public class DzialValidValidator implements ConstraintValidator<DzialValid, Dzial> {
    
    @Override
    public void initialize(DzialValid constraintAnnotation) {
        ;
    }
    
    @Override
    public boolean isValid(Dzial value, ConstraintValidatorContext context) {
       if(value == null) return false;
        System.err.println(value+"dzial walid");
       return true;
       /*DzialJpaController uC=new DzialJpaController();
        if (uC.findDzialByNazwa(value)!=null) {
            //System.err.println("valid tutaj 1");
            return false;
        } else {
            //System.err.println("valid tutaj 2");
            return true;
        }*/
    }
}
