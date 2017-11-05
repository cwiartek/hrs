package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.Salary;
import pl.com.bottega.hrs.model.TimeProvider;
import pl.com.bottega.hrs.model.commands.ChangeSalaryCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class ChangeSalaryHandler {

    EmployeeRepository repository;

    public ChangeSalaryHandler( EmployeeRepository repository) {
        this.repository = repository;

    }

    public void handle(ChangeSalaryCommand cmd) {
        Employee employee = repository.get(cmd.getEmpNo());
        employee.changeSalary(cmd.getAmount());
        repository.save(employee);
    }
}
