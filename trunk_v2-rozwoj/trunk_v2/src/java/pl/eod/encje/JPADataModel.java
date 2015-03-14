/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Arrangeable;
import org.richfaces.model.ArrangeableState;
import org.richfaces.model.FilterField;
import org.richfaces.model.SortField;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.Join;

public abstract class JPADataModel<T, Z, X> extends ExtendedDataModel<T> implements Arrangeable {

    public EntityManager entityManager;
    private Object rowKey;
    public ArrangeableState arrangeableState;
    public Class<T> entityClass;

    public JPADataModel(Class<T> entityClass) {
        super();
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
        this.entityManager = emf.createEntityManager();
        this.entityClass = entityClass;
    }
    private EntityManagerFactory emf = null;

    @Override
    public void arrange(FacesContext context, ArrangeableState state) {
        arrangeableState = state;
    }

    @Override
    public void setRowKey(Object key) {
        rowKey = key;
    }

    @Override
    public Object getRowKey() {
        return rowKey;
    }

    private CriteriaQuery<Long> createCountCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);

        Expression<Boolean> filterCriteria = createFilterCriteria(criteriaBuilder, root);
        if (filterCriteria != null) {
            criteriaQuery.where(filterCriteria);
        }

        Expression<Long> count = criteriaBuilder.count(root);
        criteriaQuery.select(count);

        return criteriaQuery;
    }

    public CriteriaQuery<T> createSelectCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        if (arrangeableState != null) {

            List<Order> orders = createOrders(criteriaBuilder, root);
            if (!orders.isEmpty()) {
                criteriaQuery.orderBy(orders);
            }

            Expression<Boolean> filterCriteria = createFilterCriteria(criteriaBuilder, root);
            if (filterCriteria != null) {
                criteriaQuery.where(filterCriteria);
            }
        }

        return criteriaQuery;
    }

    public List<Order> createOrders(CriteriaBuilder criteriaBuilder, Root<T> root) {
        List<Order> orders = Lists.newArrayList();
        List<SortField> sortFields = arrangeableState.getSortFields();
        if (sortFields != null && !sortFields.isEmpty()) {

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
        }

        return orders;
    }

    protected ArrangeableState getArrangeableState() {
        return arrangeableState;
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    public Expression<Boolean> createFilterCriteriaForField(String propertyName, Object filterValue, Root<T> root,
            CriteriaBuilder criteriaBuilder) {
        String stringFilterValue = (String) filterValue;
        if (Strings.isNullOrEmpty(stringFilterValue)) {
            return null;
        }

        stringFilterValue = stringFilterValue.toLowerCase(arrangeableState.getLocale());

        Path<String> expression = root.get(propertyName);
        Expression<Integer> locator = criteriaBuilder.locate(criteriaBuilder.lower(expression), stringFilterValue, 1);
        return criteriaBuilder.gt(locator, 0);
    }

    public Expression<Boolean> createFilterCriteriaForFieldJoin1(String propertyName, Object filterValue, Join<T, Z> root,
            CriteriaBuilder criteriaBuilder) {
        String stringFilterValue = (String) filterValue;
        if (Strings.isNullOrEmpty(stringFilterValue)) {
            return null;
        }

        stringFilterValue = stringFilterValue.toLowerCase(arrangeableState.getLocale());

        Path<String> expression = root.get(propertyName);
        Expression<Integer> locator = criteriaBuilder.locate(criteriaBuilder.lower(expression), stringFilterValue, 1);
        return criteriaBuilder.gt(locator, 0);
    }

    public Expression<Boolean> createFilterCriteriaForFieldJoin2(String propertyName, Object filterValue, Join<T, X> root,
            CriteriaBuilder criteriaBuilder) {
        String stringFilterValue = (String) filterValue;
        if (Strings.isNullOrEmpty(stringFilterValue)) {
            return null;
        }

        stringFilterValue = stringFilterValue.toLowerCase(arrangeableState.getLocale());

        Path<String> expression = root.get(propertyName);
        Expression<Integer> locator = criteriaBuilder.locate(criteriaBuilder.lower(expression), stringFilterValue, 1);
        return criteriaBuilder.gt(locator, 0);
    }

    public Expression<Boolean> createFilterCriteria(CriteriaBuilder criteriaBuilder, Root<T> root) {
        Expression<Boolean> filterCriteria = null;
        List<FilterField> filterFields = arrangeableState.getFilterFields();
        if (filterFields != null && !filterFields.isEmpty()) {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            for (FilterField filterField : filterFields) {
                System.err.println(filterField.getFilterExpression().getValue(facesContext.getELContext()));
                System.err.println(facesContext.getELContext());

                String propertyName = (String) filterField.getFilterExpression().getValue(facesContext.getELContext());
                Object filterValue = filterField.getFilterValue();

                Expression<Boolean> predicate = createFilterCriteriaForField(propertyName, filterValue, root, criteriaBuilder);

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
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        CriteriaQuery<T> criteriaQuery = createSelectCriteriaQuery();
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

        SequenceRange sequenceRange = (SequenceRange) range;
        if (sequenceRange.getFirstRow() >= 0 && sequenceRange.getRows() > 0) {
            query.setFirstResult(sequenceRange.getFirstRow());
            query.setMaxResults(sequenceRange.getRows());
        }

        List<T> data = query.getResultList();
        for (T t : data) {
            visitor.process(context, getId(t), argument);
        }
    }

    @Override
    public boolean isRowAvailable() {
        return rowKey != null;
    }

    @Override
    public int getRowCount() {
        CriteriaQuery<Long> criteriaQuery = createCountCriteriaQuery();
        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    @Override
    public T getRowData() {
        return entityManager.find(entityClass, rowKey);
    }

    @Override
    public int getRowIndex() {
        return -1;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    // TODO - implement using metadata
    protected abstract Object getId(T t);
}