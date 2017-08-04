package se.ola.domain.event;

import se.ola.domain.calendar.Calendar;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String category;
    @Column(nullable = true) //Vi vill tillåta att datumet är null
    private Date date;

    @ManyToOne
    private Calendar calendarList;

    public Event() {
    }

    public Event(String description, String category, Date date) {
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Calendar getCalendarList() {
        return calendarList;
    }

    public void setCalendarList(Calendar calendarList) {
        this.calendarList = calendarList;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", calendarList=" + calendarList +
                '}';
    }
}
