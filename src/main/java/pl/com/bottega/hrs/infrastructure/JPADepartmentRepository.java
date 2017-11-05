package pl.com.bottega.hrs.infrastructure;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;

import javax.persistence.EntityManager;

import static pl.com.bottega.hrs.model.Department_.deptNo;


public class JPADepartmentRepository implements DepartmentRepository {

    public JPADepartmentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private EntityManager entityManager;

    @Override
    public Department get(String deptNo) {

        Department department = entityManager.find(Department.class,deptNo);
        if ( department == null)
            throw new NoSuchEntityExpection();
        return department;
    }

    @Override
    public void save(Department department) {
        entityManager.persist(department);
    }
}
