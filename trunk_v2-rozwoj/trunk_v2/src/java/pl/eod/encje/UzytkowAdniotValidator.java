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
public class UzytkowAdniotValidator implements ConstraintValidator<UzytkowAdniot, String> {

    boolean takNie;

    @Override
    public void initialize(UzytkowAdniot constraintAnnotation) {
        this.takNie = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!takNie) {
            //zrobione dla testów, by obsłużyć value w adnotacji
            //System.err.println("valid tutaj");
            return true;
        }
        if(value.equals("")) return true;
        
        UzytkownikJpaController uC=new UzytkownikJpaController();
        if (uC.findStruktura(value)!=null) {
            //System.err.println("valid tutaj 1");
            return false;
        } else {
            //System.err.println("valid tutaj 2");
            return true;
        }
    }
}
