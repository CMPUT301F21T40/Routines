package com.example.routines;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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


public class HabitListUnitTest {
    private HabitList mockHabitList;
    private ArrayList<String> frequency = new ArrayList<>();
    private LayoutInflater mockLayoutInflater;
    private Context mockContext = mock(Context.class);

    @BeforeEach
    public void createHabitList(){ // create a new mock habit list for every test
        mockHabitList = new HabitList(mockContext, new ArrayList<>());
        mockLayoutInflater = (LayoutInflater) mockContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    private Habit mockHabit(){
        frequency.add("Monday");
        frequency.add("Thursday");
        return new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
    }
    public void addHabit(){
        mockHabitList.addHabit(mockHabit());
    }

    @Test
    public void addHabitTest(){
        mockHabitList.addHabit(mockHabit());
        assertEquals(1, mockHabitList.habitCount()); // count will be 1 if the add worked
    }

    @Test
    public void hasHabitTest(){
        Habit newHabit = mockHabit();
        assertFalse(mockHabitList.containsHabit(newHabit)); // should be false
        mockHabitList.addHabit(newHabit);
        assertTrue(mockHabitList.containsHabit(newHabit)); // should be true
    }

    @Test
    public void countHabitTest(){
        int before = mockHabitList.habitCount(); // should be 0
        mockHabitList.addHabit(mockHabit());
        int after = mockHabitList.habitCount(); // should be 1
        assertEquals(0, before); // check if it equals 0
        assertEquals(1, after); // check if it equals 1
    }
    @Test
    public void getViewTest(){
        // the line below is causing the error, LayoutInflator not mocked

        //mockLayoutInflater = LayoutInflater.from(mockContext);
        LayoutInflater layoutInflater = new MockLayoutInflater(mockLayoutInflater, mockContext);
        View view = mockHabitList.getView(0, null, null);
        addHabit(); // adds a habit to the habitlist
        TextView habitName = (TextView) view.findViewById(R.id.habitName);
        assertNotNull(habitName, "Name TextView is null ");
        
    }

    // found this online but it still does not work
    static class MockLayoutInflater extends LayoutInflater {
        public MockLayoutInflater(Context c) {
            super(c);
        }
        public MockLayoutInflater(LayoutInflater original, Context newContext) {
            super(original, newContext);
        }
        @Override
        public View onCreateView(String name, AttributeSet attrs)
                throws ClassNotFoundException {
            return super.onCreateView(name, attrs);
        }
        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return null;
        }
    }

}
