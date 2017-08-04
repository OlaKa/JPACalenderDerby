package se.ola.domain.calendar;

import se.ola.domain.event.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Calendar {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Column(nullable = true) //Vi vill tillåta att datumet är null
    private Date startDate;
    private Date stopDate;

    @OneToMany(mappedBy = "calendarList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    public Calendar() {
        super();
    }

    public Calendar(String name, Date startDate, Date stopDate) {
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", stopDate=" + stopDate +
                ", events=" + events +
                '}';
    }
}