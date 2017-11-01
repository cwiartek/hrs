package pl.com.bottega.hrs.ui;

import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SearchScreen {

    private Scanner scanner;
    private EntityManager em;
    private String firstName;
    private String lastName;

    public SearchScreen(Scanner scanner, EntityManager em) {
        this.scanner = scanner;
        this.em = em;
    }


    public Employee show() {
        while (true) {
            System.out.println("Podaj kryteria wyszukiwania");
            System.out.println("Imie: ");
            String firstName = getFirstName();
            System.out.println("Nazwisko");
            String lastName = getLastName();

            Query query = em.createQuery("SELECT e FROM Employee WHERE e.firstName LIKE :firstName AND e.lastName LIKE :lastName").
                    setParameter("firstName",firstName).
                    setParameter("lastName",lastName);
            List<Employee> employees = query.getResultList();

            if(employees.size() != 0)
                return selectEmployee(employees);



        }

    }

    private Employee selectEmployee(List<Employee> employees) {
        while (employees.size() == 0)
            return employees.get(0);

        for(int i=0; i<=employees.size();i++) {
            System.out.print(String.format("%d - %s %s",i,employees.get(i-1).getFirstName(),employees.get(i-1).getLastName() ));
        }
        int employeeNumber;
        do {
            System.out.println("Wybierz numer pracownika");
            employeeNumber = scanner.nextInt();
        } while ((employeeNumber<1) || (employeeNumber>employees.size()));
        return employees.get(employeeNumber-1);

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
