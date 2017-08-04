package se.ola.domain.calender;

import org.junit.Before;
import org.junit.Test;
import se.ola.domain.calendar.Calendar;
import se.ola.domain.event.Event;

import javax.persistence.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarDbTest {
    private EntityManager manager;
    private Calendar calendarEvent;

    @Before
    public void setUp() throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        manager = factory.createEntityManager();

        String startDate = "18-02-2017";
        String stopDate = "22-02-2017";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startInputDate = null;
        Date stopInputDate = null;
        try {
            startInputDate = dateFormat.parse(startDate);
            stopInputDate = dateFormat.parse(stopDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarEvent = new Calendar("kalenderinput", startInputDate, stopInputDate);
        calendarEvent.getEvents().add(new Event("Mötte Lisa på Gullmarsplan", "privat", startInputDate));
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            manager.persist(calendarEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();

    }

    @Test
    public void printGetAllEventLists() throws Exception {
        List<Event> eventLists = getAllEventLists();
        System.out.println("------------------");
        for (Event eventList : eventLists) {
            System.out.println(eventList);
        }
    }

    private List<Event> getAllEventLists() {
        Query query = manager.createQuery("SELECT shoplist FROM Event shoplist", Event.class);
        return query.getResultList();
    }
}
