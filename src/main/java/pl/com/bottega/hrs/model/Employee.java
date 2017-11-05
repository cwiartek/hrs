package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "employees")
public class Employee {

    @Transient
    private TimeProvider timeProvider;

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "enum('M' , 'F')")
    private Gender gender = Gender.M;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<Salary> salaries = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<Title> titles = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<DepartmentAssignment> departmentAssignments = new LinkedList<>();



    public Employee(Integer empNo, String firstName, String lastName,LocalDate birthDate, Address address,TimeProvider timeProvider) {
        this.empNo = empNo;
        this.birthDate = birthDate;
        this.timeProvider = timeProvider;
        this.hireDate = timeProvider.today();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Employee() {}

    public void updateProfile(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public void updateProfile(String firstName, String lastName, LocalDate birthDate, Address address, Gender gender) {
        updateProfile(firstName,lastName,birthDate);
        this.address = address;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }

    public Collection<Salary> getSalaries() {
        return salaries;
    }

    public Optional<Salary> getCurrentSalary() {

     /*   for(Salary salary : salaries) {
            if (salary.getToDate().isAfter(timpeProvider.today()))
                return Optional.of(salary);
        }
        return Optional.empty();
       */

       return salaries.stream().filter((salary) -> salary.isCurrent()).findFirst();
    }


    public void changeSalary(Integer newSalary) {

        Optional<Salary> optionalSalary = getCurrentSalary();
        if (optionalSalary.isPresent()) {
            Salary currentSalary = optionalSalary.get();
            removeOrTerminateSalary(newSalary, currentSalary);
        }
            addNewSalary(newSalary);
        }



    private void addNewSalary(Integer newSalary) {
        salaries.add(new Salary(empNo, newSalary,timeProvider));
    }

    private void removeOrTerminateSalary(Integer newSalary, Salary currentSalary) {
        if (currentSalary.startsToday()) {
           salaries.remove(currentSalary);
        }
        else {
            currentSalary.terminate();

        }
    }

    public void assignDepartment( Department department) {

        if(!isCurrentlyAssignedTo(department))

        departmentAssignments.add(new DepartmentAssignment(empNo, department,timeProvider));


    }

    private boolean isCurrentlyAssignedTo(Department department) {
        return getCurrentDepartments().contains(department) ;
    }

    public void unassignDepartment( Department department) {

        departmentAssignments.stream().filter((assignment) -> assignment.isAssigned(department)).
                findFirst().ifPresent(DepartmentAssignment::unassign);


    }



    public Collection<Department> getCurrentDepartments() {

        return departmentAssignments.stream().filter(DepartmentAssignment::isCurrent).map(DepartmentAssignment::getDepartment).collect(Collectors.toList());
    }


    public Collection<DepartmentAssignment> getDepartmentHistory() {
        return departmentAssignments;
    }

    public Optional<Title> getCurrentTitle() {
        return titles.stream().filter(Title::isCurrent).findFirst();
    }

    public void changeTitle(String titleName) {
        getCurrentTitle().ifPresent((t) -> {
            if (t.startsToday())
                titles.remove(t);
            else
                t.terminate();
        });
        titles.add(new Title(empNo, titleName, timeProvider));
    }

    public Collection<Title> getTitleHistory() {
        return titles;
    }

}


