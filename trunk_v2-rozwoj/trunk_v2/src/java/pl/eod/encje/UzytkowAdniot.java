/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author arti01
 */
@Documented
@Constraint(validatedBy = UzytkowAdniotValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UzytkowAdniot {

    String message() default "{pl.eod.encje.UzytkowAdniot}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
   boolean value();
}
