package pl.com.bottega.hrs;

import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.infrastructure.StandardTimeProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class App2 {

    public static void main(String[] args)

    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HRS");


        Address address = new Address("Al. Warszawska 10","Lublin");
        Employee employee = new Employee(50056,"Krzysztof", "Jerzyna",LocalDate.parse("2012-12-23"),address,new StandardTimeProvider());
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(employee);
        em.flush();

        em.getTransaction().commit();
        em.close();


    }
}
