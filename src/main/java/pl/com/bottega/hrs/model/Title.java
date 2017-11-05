package pl.com.bottega.hrs.model;


import pl.com.bottega.hrs.infrastructure.StandardTimeProvider;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "titles")
public class Title {

    @Embeddable

    public static class TitleId implements Serializable {

        @Column(name = "emp_no")
        private Integer empNo;

        @Transient
        private TimeProvider timeProvider;

        @Column(name = "from_date")
        private LocalDate fromDate;

        private String title;

        public TitleId() {}

        public TitleId(Integer empNo, String title, TimeProvider timeProvider) {
            this.empNo = empNo;
            this.timeProvider = timeProvider;
            this.title = title;
            this.fromDate = timeProvider.today();
        }

        public boolean startsToday() {
            return fromDate.isEqual(timeProvider.today());
        }
    }

    @Transient
    private TimeProvider timeProvider = new StandardTimeProvider();

    @EmbeddedId
    private TitleId id;

    @Column(name = "to_date")
    private LocalDate toDate;

    Title() {}

    public Title(Integer empNo, String titleName, TimeProvider timeProvider) {
        this.id = new TitleId(empNo, titleName, timeProvider);
        this.timeProvider = timeProvider;
        toDate = TimeProvider.MAX_DATE;
    }

    public String getName() {
        return id.title;
    }

    public boolean isCurrent() {
        return toDate.isAfter(timeProvider.today());
    }

    public boolean startsToday() {
        return id.startsToday();
    }

    public void terminate() {
        toDate = timeProvider.today();
    }

    public LocalDate getFromDate() {
        return id.fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

}


