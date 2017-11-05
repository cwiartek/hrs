package pl.com.bottega.hrs.infrastructure;

import pl.com.bottega.hrs.application.DetailedEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResult;
import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class JPQLEmployeeFinder implements EmployeeFinder {

    private EntityManager entityManager;

    public JPQLEmployeeFinder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EmployeeSearchResult search(EmployeeSearchCriteria criteria) {

        EmployeeSearchResult result = new EmployeeSearchResult();

        String jpql = "SELECT NEW pl.com.bottega.hrs.application.BasicEmployeeDto(e.firstName, e.lastName, e.empNo) FROM Employee e ";
        String wherejpql = " 1 = 1 ";

        if(criteria.getLastNameQuery() !=null) {
            wherejpql += " AND e.lastName LIKE :lastName ";

        }
        if(criteria.getFirstNameQuery() !=null) {
            wherejpql += " AND e.firstName LIKE :firstName ";
        }

        Query query = entityManager.createQuery(jpql + " WHERE " + wherejpql);
        if(criteria.getLastNameQuery() !=null) {
            query.setParameter("lastName",criteria.getLastNameQuery() + "%" );
        }
        if(criteria.getFirstNameQuery() !=null) {
            query.setParameter("firstName", criteria.getFirstNameQuery() + "%");
        }
        result.setResults(query.getResultList());
        return result;
    }
    @Override

    public DetailedEmployeeDto getEmployeeDetails(Integer empNo) {

        return null;

    }
}
