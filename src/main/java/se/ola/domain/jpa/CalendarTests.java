package se.ola.domain.jpa;

import se.ola.domain.calendar.Calendar;
import se.ola.domain.event.Event;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CalendarTests {
    private EntityManager manager;

    public CalendarTests(EntityManager manager) {
        this.manager = manager;
    }

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        EntityManager manager = factory.createEntityManager();
        CalendarTests test = new CalendarTests(manager);

        clear(manager);

        test1(manager, test);
        test2(manager, test);


        System.out.println(".. done");

    }

    private static void clear(EntityManager manager) {
        clearEventList(manager);
        clearCalendar(manager);
    }

    private static void clearEventList(EntityManager manager) {
        EntityTransaction tx = manager.getTransaction();
        List<Event> resultList = manager.createQuery("Select a From Event a", Event.class).getResultList();
        System.out.println("Event list size: " + resultList.size());
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

    private static void test1(EntityManager manager, CalendarTests test) {
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            test.createCalendarEvent("10-02-2017", "18-02-2017");
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
        test.listEvents();
    }

    private static void test2(EntityManager manager, CalendarTests test) {
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            test.createCalendarEvent("03-04-2017", "11-04-2017");
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
        test.listCalendarObjects();
    }

    private void createCalendarEvent(String stDate, String spDate) throws ParseException {
        String startDate = stDate;
        String stopDate = spDate;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startInputDate = dateFormat.parse(startDate);
        Date stopInputDate = dateFormat.parse(stopDate);

        Calendar calendarEvent = new Calendar("kalenderinput", startInputDate, stopInputDate);
        calendarEvent.getEvents().add(new Event("Mötte Lisa på cafeét", "privat", startInputDate));
        calendarEvent.getEvents().add(new Event("Färdigställde javauppgiften", "skola", startInputDate));
        calendarEvent.getEvents().add(new Event("Sprang 10 km på 40 minuter", "fritid", startInputDate));
        manager.persist(calendarEvent);
        System.out.println("Printing Calendar Events");
        System.out.println(calendarEvent);
    }

    private void listEvents() {
        List<Event> resultList = manager.createQuery("SELECT a FROM Event a", Event.class).getResultList();
        System.out.println("Number of Events:" + resultList.size());
        for (Event next : resultList) {
            System.out.println("Next item: " + next);
            System.out.println(" ");
        }
    }

    private void listCalendarObjects() {
        List<Calendar> calendarList = manager.createQuery("Select a From Calendar a", Calendar.class).getResultList();
        System.out.println("Listing Calendar objects:" + calendarList.size());
        for (Calendar next : calendarList) {
            System.out.println("Next calendar event: " + next);
        }
    }

    private static void clearCalendar(EntityManager manager) {
        EntityTransaction tx = manager.getTransaction();
        List<Calendar> resultList = manager.createQuery("Select a From Calendar a", Calendar.class).getResultList();
        System.out.println("Calender list: " + resultList.size());
        tx.begin();
        try {
            resultList.forEach(manager::remove);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
    }
}
