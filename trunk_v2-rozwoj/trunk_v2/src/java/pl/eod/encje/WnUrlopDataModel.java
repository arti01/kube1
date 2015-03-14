/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import org.richfaces.model.FilterField;

/**
 *
 * @author arti01
 */
public class WnUrlopDataModel extends JPADataModel<WnUrlop, Uzytkownik, WnStatusy> {

    public WnUrlopDataModel() {
        super(WnUrlop.class);
    }

    @Override
    public Expression<Boolean> createFilterCriteria(CriteriaBuilder criteriaBuilder, Root<WnUrlop> root) {
        Expression<Boolean> filterCriteria = null;
        if (getArrangeableState().getFilterFields() == null) {
            return null;
        }
        List<FilterField> filterFields = getArrangeableState().getFilterFields();
        Join<WnUrlop, Uzytkownik> juser = root.join(WnUrlop_.uzytkownik);
        Join<WnUrlop, WnStatusy> jstatus = root.join(WnUrlop_.statusId);
        if (filterFields != null && !filterFields.isEmpty()) {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            for (FilterField filterField : filterFields) {
                String propertyName = (String) filterField.getFilterExpression().getValue(facesContext.getELContext());
                Object filterValue = filterField.getFilterValue();

                Expression<Boolean> predicate = null;
                if (filterField.getFilterExpression().getValue(facesContext.getELContext()).equals("fullname")) {
                    predicate = createFilterCriteriaForFieldJoin1(propertyName, filterValue, juser, criteriaBuilder);
                }
                if (filterField.getFilterExpression().getValue(facesContext.getELContext()).equals("opis")) {
                    WnStatusy filterValueS = (WnStatusy) filterValue;
                    if (filterValueS != null) {
                        predicate = createFilterCriteriaForFieldJoin2(propertyName, filterValueS.getOpis(), jstatus, criteriaBuilder);
                    }
                }

                if (predicate == null) {
                    continue;
                }

                if (filterCriteria == null) {
                    filterCriteria = predicate.as(Boolean.class);
                } else {
                    filterCriteria = criteriaBuilder.and(filterCriteria, predicate.as(Boolean.class));
                }
            }
        }
        return filterCriteria;
    }

    @Override
    protected Object getId(WnUrlop t) {
        return t.getId();
    }
}