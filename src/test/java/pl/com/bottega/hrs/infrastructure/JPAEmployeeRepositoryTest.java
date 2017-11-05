package pl.com.bottega.hrs.infrastructure;

import org.junit.Test;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.StandardTimeProvider;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPAEmployeeRepositoryTest extends InfrastructureTest {


    private EntityManager entityManager = createEntityManager();
    private JPAEmployeeRepository sut = new JPAEmployeeRepository(entityManager);

    @Test
    public void firstNumberShouldBeOne() {

        //when
        Integer number = sut.generateNumber();

        //then
        assertEquals(new Integer(1),number);
    }

    @Test
    public void shouldSaveEmployee() {

        //given
        Employee employee = createEmployee(1, "Kowalski");

        //when

        executeTransaction(entityManager -> sut.save(employee));

        //then
        executeTransaction(entityManager -> {
            Employee employeeFromRepo = sut.get(1);
            assertNotNull(employeeFromRepo);
            assertEquals("Kowalski", employeeFromRepo.getLastName());
        });
    }

    @Test
    public void shouldGenerateNextEmpNo() {
        //given
        Employee c1 = createEmployee(1, "Kowalski");
        Employee c2 = createEmployee(2, "Janowski");
        executeTransaction(entityManager, () -> sut.save(c1));
        executeTransaction(entityManager, () -> sut.save(c2));

        //when
        Integer number = sut.generateNumber();

        //then
        assertEquals(new Integer(3),number);

    }

    private Employee createEmployee(Integer no, String lastName) {
        Address address = new Address("al. Warszawska 10", "Lublin");
        return new Employee(no,"Jan",lastName, LocalDate.now(),address,new StandardTimeProvider());

    }


}
