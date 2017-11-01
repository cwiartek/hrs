package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "salaries")
public class Salary {




    @Embeddable
    public static class SalaryId implements Serializable {

        @Column(name = "emp_no")
        private Integer empNo;

        @Column(name = "from_date")
        private LocalDate fromDate;

        @Transient
        private TimeProvider timeProvider;

        SalaryId(){}

        public SalaryId(Integer empNo, TimeProvider timeProvider) {
            this.empNo = empNo;
            this.fromDate = timeProvider.today();
            this.timeProvider = timeProvider;
        }

        public boolean startsToday() {

            return fromDate.isEqual(LocalDate.now());
        }


    }
    @EmbeddedId
    private SalaryId id;


    private Integer salary;

    @Transient
    private TimeProvider timeProvider;

    @Column(name = "to_date")
    private LocalDate toDate;

    public Salary(){}

    public Salary(Integer empNo, Integer salary, TimeProvider timeProvider) {

        id = new SalaryId(empNo,timeProvider);
        this.salary = salary;
        this.timeProvider = timeProvider;
        toDate = Constans.MAX_DATE;

    }


    public boolean isCurrent() {
        return toDate.isAfter(timeProvider.today());
    }

    public void terminate() {
        toDate = timeProvider.today();

    }

    public boolean startsToday() {

        return id.startsToday();
    }
    public int getValue() {
        return salary;
    }

    public LocalDate getFromDate() {
        return id.fromDate;
    }
    public LocalDate getToDate() {
        return toDate;
    }



    }




