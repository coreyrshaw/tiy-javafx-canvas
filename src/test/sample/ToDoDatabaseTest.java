package sample;

import jdk.nashorn.internal.AssertsEnabled;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Corey Shaw on 5/12/2016.
 */
public class ToDoDatabaseTest {
    ToDoDatabase todoDatabase;
    @Before
    public void setUp() throws Exception {
        todoDatabase = new ToDoDatabase();
        todoDatabase.init();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInit() throws Exception {
        // test to make sure we can access the new database
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement todoQuery = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = todoQuery.executeQuery();
        assertNotNull(results);
    }

    @Test
    public void testInsertToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String userName = "coreyrshaw";
        String fullName = "Corey Shaw";
        todoDatabase.insertToDo(conn, userName);
        // make sure we can retrieve the todo we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos INNER JOIN users ON users.id = todos.user_id WHERE users.id = 1");
        stmt.setString(1, userName);
        stmt.setString(2,fullName);

        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        // count the records in results to make sure we get what we expected
        int numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(1, numResults);

     //   todoDatabase.deleteToDo(conn, todoText,results.getInt(1));//,results.getInt("id")

        // make sure there are no more records for our test todo
        results = stmt.executeQuery();
        numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(0, numResults);
    }

  @Test
    public void testSelectAllToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String firstToDoText = "UnitTest-ToDo1";
        String secondToDoText = "UnitTest-ToDo2";

        int firstToDoID = todoDatabase.insertToDo(conn, firstToDoText);
        int secondToDoID = todoDatabase.insertToDo(conn, secondToDoText);

        ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn);

        System.out.println("Found " + todos.size() + " todos in the database");

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1);

        todoDatabase.deleteToDo(conn, firstToDoText,firstToDoID);
        todoDatabase.deleteToDo(conn, secondToDoText,secondToDoID);
    }


    @Test
    public void testToggleToDo()throws Exception{

            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            String todoText = "UnitTest-ToDo";
//          todoDatabase.deleteToDo(conn,todoText);
            int toDoID= todoDatabase.insertToDo(conn, todoText);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= ?");
            stmt.setString(1, todoText);
            ResultSet results = stmt.executeQuery();
            results.next();
            boolean beforeToggleModel = results.getBoolean("is_done");
            todoDatabase.toggleToDo(conn, results.getInt("id"));
            results = stmt.executeQuery();
            results.next();
            boolean afterToggleModel = results.getBoolean("is_done");
            assertNotEquals(beforeToggleModel, afterToggleModel);
//        }

          ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn);
          todoDatabase.toggleToDo(conn,toDoID);
          todoDatabase.deleteToDo(conn,todoText,toDoID);

    }

    @Test
    public void testDeleteToDo()throws Exception {//
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "How are you?";
        todoDatabase.insertToDo(conn, todoText);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= ?");
        stmt.setString(1, todoText);
        ResultSet results = stmt.executeQuery();
        results.next();
        int idBeforeDelete = results.getInt("id");
        todoDatabase.insertToDo(conn, todoText);
        stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= ?");

    }
    }




