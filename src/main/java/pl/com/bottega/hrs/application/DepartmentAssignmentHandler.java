package pl.com.bottega.hrs.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.AssignDepartmentToEmployeeCommand;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

@Component
public class DepartmentAssignmentHandler {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    public DepartmentAssignmentHandler(
            DepartmentRepository repository,
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public void handle(AssignDepartmentToEmployeeCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.assignDepartment(departmentRepository.get(cmd.getDeptNo()));
        employeeRepository.save(employee);
    }
}

