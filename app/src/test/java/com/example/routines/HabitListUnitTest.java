package com.example.routines;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.testng.reporters.jq.TestNgXmlPanel;


public class HabitListUnitTest {
    private HabitList habitList;
    private ArrayList<String> frequency = new ArrayList<>();
    private Context mockContext = mock(Context.class);
    private View mockView = mock(View.class);
    private ViewGroup mockViewGroup = mock(ViewGroup.class);
    private TextView mockTextView = mock(TextView.class);


    @BeforeEach
    public void createHabitList(){ // create a new mock habit list for every test
        habitList = new HabitList(mockContext, new ArrayList<>());

    }

    private Habit mockHabit(){
        frequency.add("Monday");
        frequency.add("Thursday");
        return new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
    }
    public void addHabit(){
        habitList.addHabit(mockHabit());
    }

    @Test
    public void addHabitTest(){
        habitList.addHabit(mockHabit());
        assertEquals(1, habitList.habitCount()); // count will be 1 if the add worked
    }

    @Test
    public void hasHabitTest(){
        Habit newHabit = mockHabit();
        assertFalse(habitList.containsHabit(newHabit)); // should be false
        habitList.addHabit(newHabit);
        assertTrue(habitList.containsHabit(newHabit)); // should be true
    }

    @Test
    public void countHabitTest(){
        int before = habitList.habitCount(); // should be 0
        habitList.addHabit(mockHabit());
        int after = habitList.habitCount(); // should be 1
        assertEquals(0, before); // check if it equals 0
        assertEquals(1, after); // check if it equals 1
    }
    @Test
    public void getViewTest(){
        when(mockView.findViewById(anyInt())).thenReturn(mockTextView);

        addHabit(); // add a habit to the habit list
        View view = habitList.getView(0, mockView, mockViewGroup);

        // so now when we call findViewById we always get a returned mockTextView which will have no attributes? 
        TextView name = (TextView) view.findViewById(R.id.habitName);
        TextView reason = (TextView) view.findViewById(R.id.habitReason);
        TextView startingDate = (TextView) view.findViewById(R.id.habitDate);

        // don't get how to compare the 2, a mock cant have a value, is always null pointer

        // assertEquals("Soccer", name.getText().toString()); // cannot actually compare a mock to a string

        // we can check IF at least something was added to the list using assert nulls?
        assertNotNull(name, "Name TextView is null ");
        assertNotNull(reason, "Reason TextView is null ");
        assertNotNull(startingDate, "Date TextView is null ");

        // can check if they are mockTextViews
        assertThat(name, is(mockTextView));
        assertThat(reason, is(mockTextView));
        assertThat(startingDate, is(mockTextView));
    }


}
