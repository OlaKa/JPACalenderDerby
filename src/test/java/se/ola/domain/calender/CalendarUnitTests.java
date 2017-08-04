package se.ola.domain.calender;


import org.junit.After;
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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CalendarUnitTests {
    private EntityManager manager;
    private Calendar calendarEvent;

    @Before
    public void setUp() throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        manager = factory.createEntityManager();

        String startDate = "18-02-2017";
        String stopDate = "22-05-2017";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startInputDate = null;
        Date stopInputDate = null;
        try {
            startInputDate = dateFormat.parse(startDate);
            stopInputDate = dateFormat.parse(stopDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        createCalendarEvents(startInputDate, stopInputDate, "Mötte Lisa på Cafeèt på Gullmarsplan", "privat", true);

    }

    @After
    public void tearDown() {
        clear();
        clearEvents();
    }

    @Test
    public void testStartDate() throws Exception {
        //Retrive some dates
        List<Date> dateList = getStartDate(manager);
        System.out.println("Datelist have size " + dateList.size());
        assertFalse(dateList.isEmpty());
        for (Date l : dateList) {
            System.out.println(l.toString());
        }
    }

    @Test
    public void testEventsType() throws Exception {
        //Retreive some specific events
        List<Event> eventList = getEvents();
        System.out.println("Eventlist have size: " + eventList.size());
        assertFalse(eventList.isEmpty());
        for (Event l : eventList) {
            System.out.println("Res: " + l);
        }
    }

    @Test
    public void testCalendarEventByDay() throws Exception {
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

        createCalendarEvents(startInputDate, stopInputDate, "Joggade 10 kilometer på 40 minuter", "privat", false);
        //Check which events that existed on a certain day
        List<Event> eventList = getEvents();
        System.out.println("Eventlist have size: " + eventList.size());
        assertFalse(eventList.isEmpty());
        for (Event l : eventList) {
            String date = convertDateToString(l.getDate(), "dd-MM-yyyy");
            if (date.equals("18-02-2017")) {

                System.out.println("Activities found on date 18-05-2017: " + l.getDescription());
            }
        }
    }

    @Test
    public void testCalendarEventOfACertainType() throws Exception {
        String startDate = "18-02-2017";
        String stopDate = "22-02-2017";
        String newStartDate = "20-02-2017";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startInputDate = null;
        Date stopInputDate = null;
        Date nStartDate = null;

        try {
            startInputDate = dateFormat.parse(startDate);
            stopInputDate = dateFormat.parse(stopDate);
            nStartDate = dateFormat.parse(newStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        createCalendarEvents(startInputDate, stopInputDate, "Joggade 10 kilometer på 40 minuter", "privat", false);
        createCalendarEvents(nStartDate, stopInputDate, "Gick på Bio och såg Rogue One", "privat", false);
        //Check which event there is of a category private and print the date also
        List<Event> eventList = getEvents();
        assertTrue(eventList.size() > 2);
        assertFalse(eventList.isEmpty());
        System.out.println("You have these events of category private ");
        for (Event l : eventList) {
            String date = convertDateToString(l.getDate(), "dd-MM-yyyy");
            if (l.getCategory().equals("privat")) {
                System.out.println("Description " + l.getDescription() + " happend on date " + date);
            }
        }
    }

    @Test
    public void testCalendarEventOfACertainTypeByDate() throws Exception {
        String startDate = "18-02-2017";
        String stopDate = "22-02-2017";
        String newStartDate = "20-02-2017";
        String moreStartDate = "20-02-2017";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startInputDate = null;
        Date stopInputDate = null;
        Date nStartDate = null;
        Date mStartDate = null;

        try {
            startInputDate = dateFormat.parse(startDate);
            stopInputDate = dateFormat.parse(stopDate);
            nStartDate = dateFormat.parse(newStartDate);
            mStartDate = dateFormat.parse(moreStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Add some extra events in calendar
        createCalendarEvents(startInputDate, stopInputDate, "Joggade 10 kilometer på 40 minuter", "privat", false);
        createCalendarEvents(nStartDate, stopInputDate, "Löste javaprogrammeringsuppgift", "skola", false);
        createCalendarEvents(mStartDate, stopInputDate, "Kom försent till skolan och fick kvarsittning", "skola", false);
        //Check which event there is of a category skola and of a certain date
        List<Event> eventList = getEvents();
        assertFalse(eventList.isEmpty());
        assertTrue(eventList.size() > 3);
        System.out.println("These events of category skola happend on May 20 2017 ");
        for (Event l : eventList) {
            String date = convertDateToString(l.getDate(), "dd-MM-yyyy");
            if (l.getCategory().equals("skola") && date.equals("20-02-2017")) {
                System.out.println("Event '" + l.getDescription() + "' happend on date " + date);
            }
        }
    }


    private List<Calendar> getAllCalendarLists() {
        Query query = manager.createQuery("SELECT cal FROM Calendar cal", Calendar.class);
        return query.getResultList();
    }

    private List<Date> getStartDate(EntityManager manager) {
        System.out.println("Retriving startdates");
        Query query = manager.createQuery("SELECT c.startDate FROM Calendar c", Calendar.class);
        return query.getResultList();
    }

    private List<Event> getEvents() {
        Query query = manager.createQuery("SELECT anEvent FROM Event anEvent", Event.class);
        return query.getResultList();
    }

    private void clear() {
        clearCalendar();
    }

    private void clearCalendar() {
        EntityTransaction tx = manager.getTransaction();
        List<Calendar> resultList = getAllCalendarLists();
        tx.begin();
        try {
            for (Calendar next : resultList) {
                manager.remove(next);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
    }

    private void clearEvents() {
        EntityTransaction tx = manager.getTransaction();
        List<Event> resultList = manager.createQuery("Select a From Event a", Event.class).getResultList();
        tx.begin();
        try {
            for (Event next : resultList) {
                manager.remove(next);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
    }

    public void createCalendarEvents(Date startInputDate, Date stopInputDate, String description, String type, boolean createNewCalendarEvent) {
        if (createNewCalendarEvent) {
            calendarEvent = new Calendar("kalenderinput", startInputDate, stopInputDate);
        }
        calendarEvent.getEvents().add(new Event(description, type, startInputDate));
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            manager.persist(calendarEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
    }

    public String convertDateToString(Date date, String format) {
        String dateStr = null;
        DateFormat df = new SimpleDateFormat(format);
        try {
            dateStr = df.format(date);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateStr;
    }
}
