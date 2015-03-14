/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import com.google.common.collect.Lists;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.richfaces.model.FilterField;

public class StrukturaDataModel extends JPADataModel<Struktura, Uzytkownik, Dzial> {
    Spolki spolka;
public StrukturaDataModel(Spolki spolka) {
        super(Struktura.class);
        this.spolka=spolka;
    }

    @Override
    public Expression<Boolean> createFilterCriteria(CriteriaBuilder criteriaBuilder, Root<Struktura> root) {
        Expression<Boolean> filterCriteria = null;
        if(getArrangeableState().getFilterFields()==null) return null;
        List<FilterField> filterFields = getArrangeableState().getFilterFields();
        Join<Struktura, Uzytkownik>juser=root.join(Struktura_.userId);
        Join<Struktura, Dzial> jstatus=root.join(Struktura_.dzialId);
        
        if(spolka!=null){
        Predicate spolkiP ;
        spolkiP = criteriaBuilder.and(criteriaBuilder.equal(juser.get(Uzytkownik_.spolkaId), spolka),criteriaBuilder.isNotNull(juser.get(Uzytkownik_.spolkaId)));
        filterCriteria = criteriaBuilder.and(spolkiP);
        }
        
        if (filterFields != null && !filterFields.isEmpty()) {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            for (FilterField filterField : filterFields) {
                //System.err.println(filterField.getFilterExpression().getValue(facesContext.getELContext()));
                String propertyName = (String) filterField.getFilterExpression().getValue(facesContext.getELContext());
                Object filterValue = filterField.getFilterValue();

                Expression<Boolean> predicate=null;
                if(filterField.getFilterExpression().getValue(facesContext.getELContext()).equals("fullname")){
                    predicate = createFilterCriteriaForFieldJoin1(propertyName, filterValue, juser, criteriaBuilder);
                }
                if(filterField.getFilterExpression().getValue(facesContext.getELContext()).equals("nazwa")){
                    Dzial filterValueS=(Dzial) filterValue;
                    if(filterValueS!=null){
                        predicate = createFilterCriteriaForFieldJoin2(propertyName, filterValueS.getNazwa(), jstatus, criteriaBuilder);
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
       public List<Order> createOrders(CriteriaBuilder criteriaBuilder, Root<Struktura> root) {
        List<Order> orders = Lists.newArrayList();
        Join<Struktura, Uzytkownik>juser=root.join(Struktura_.userId);
        //List<SortField> sortFields = arrangeableState.getSortFields();
        Order jpaOrder;
        jpaOrder=criteriaBuilder.asc(juser.get(Uzytkownik_.fullname));
        orders.add(jpaOrder);
        /*if (sortFields != null && !sortFields.isEmpty()) {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            for (SortField sortField : sortFields) {
                String propertyName = (String) sortField.getSortBy().getValue(facesContext.getELContext());
                if (propertyName != null) {


                    Path<Object> expression = root.get(propertyName);

                    Order jpaOrder;
                    SortOrder sortOrder = sortField.getSortOrder();
                    if (sortOrder == SortOrder.ascending) {
                        jpaOrder = criteriaBuilder.asc(expression);
                    } else if (sortOrder == SortOrder.descending) {
                        jpaOrder = criteriaBuilder.desc(expression);
                    } else {
                        throw new IllegalArgumentException(sortOrder.toString());
                    }

                    orders.add(jpaOrder);
                }
            }
        }*/

        return orders;
    }
    
    @Override
    protected Object getId(Struktura t) {
        return t.getId();
    }
}