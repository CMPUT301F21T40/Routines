package com.example.routines;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class EventListUnitTest {
    private EventCustomList eventList;
    private Context mockContext = mock(Context.class);
    private View mockView = mock(View.class);
    private ViewGroup mockViewGroup = mock(ViewGroup.class);
    private TextView mockTextView = mock(TextView.class);

    @BeforeEach
    public void createEventList() {
        eventList = new EventCustomList(mockContext, new ArrayList<>());

    }

    public Event stubEvent() {
        return new Event("Test Event", "Test Description", "Habit Id", "2020-01-01", "Edmonton");
    }



    @Test
    public void addEventTest() {
        eventList.addEvent(stubEvent());
        assertEquals(1, eventList.eventCount());
    }

    @Test
    public void hasEventTest() {
        Event newEvent = stubEvent();
        assertFalse(eventList.containsEvent(newEvent));
        eventList.addEvent(newEvent);
        assertTrue(eventList.containsEvent(newEvent));
    }


    @Test
    public void countEventTest() {
        int before = eventList.eventCount();
        eventList.addEvent(stubEvent());
        int after = eventList.eventCount();
        assertEquals(0, before);
        assertEquals(1, after);
    }

    @Test
    public void getViewTest() {
        when(mockView.findViewById(anyInt())).thenReturn(mockTextView);
        eventList.addEvent(stubEvent());
        eventList.getView(0,mockView,mockViewGroup);

        verify(mockTextView, Mockito.times(1)).setText("Test Event");
        verify(mockTextView, Mockito.times(1)).setText("2020-01-01");
    }

}
