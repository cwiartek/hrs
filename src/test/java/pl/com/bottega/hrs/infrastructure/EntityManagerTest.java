package pl.com.bottega.hrs.infrastructure;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.com.bottega.hrs.infrastructure.InfrastructureTest;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.StandardTimeProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntityManagerTest extends InfrastructureTest {


    @Test
    public void tracksChangesToEntities() {


        //given
        executeTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        //when
        executeTransaction((em) -> {
            Employee employee = em.find(Employee.class,1);
            updateFirstName("Janusz",employee);
        });

        //then
        executeTransaction((em) -> {
            Employee employee = em.find(Employee.class,1);
            assertEquals("Janusz",employee.getFirstName());
        });

    }

    @Test
    public void mergesEntities() {

        //given
        executeTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.merge(employee);
        });

        //when
        executeTransaction((em) -> {
            Employee employee = createEmployee("Janusz");
            Employee employeeCopy = em.merge(employee);
            updateFirstName("Stefan", employee);
            updateFirstName("Eustachy",employeeCopy);
        });

        //then

        executeTransaction((em) -> {
            Employee employee = em.find(Employee.class,1);
            assertEquals("Eustachy", employee.getFirstName());
        });
    }

    private Employee tmpEmployee;

    @Test(expected = LazyInitializationException.class)
    public void throwsLazyInitExpection() {

        //given
        executeTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });
        executeTransaction((em) -> {
            tmpEmployee = em.find(Employee.class, 1);

        });
        tmpEmployee.getSalaries().size();
    }

    @Test
    public void removeEntities() {

        //given
        executeTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.merge(employee);
        });

        //when

        executeTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            em.remove(employee);
        });

        //then
        executeTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertNull(employee);

        });
    }

    @Test
    public void cascadesOperations() {

        executeTransaction((em) -> {
            Employee employee = createEmployee("Janek");
            em.persist(employee);
        });
        //then
        executeTransaction((em) -> {
            Address address = em.find(Address.class,1);
            assertNotNull(address);
        });
    }



    private Employee createEmployee (String firstName) {
        Address address = new Address("al. Warszawska 10", "Lublin");
        return new Employee(1, firstName, "Nowak",LocalDate.now(),address,new StandardTimeProvider());
    }

    private void updateFirstName(String newName, Employee employee) {
        employee.updateProfile(newName, "Nowak", LocalDate.now());
    }


}
