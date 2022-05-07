import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteJDBC {

  public ResultSet CreateDatabase(BTree btree) throws SQLException {
    Connection connection = null;
    // create a database connection
    connection = DriverManager.getConnection("jdbc:sqlite:BTree.db");
    Statement stmt = connection.createStatement();
    // database opened successfully

    stmt.setQueryTimeout(30); // set timeout to 30 sec.

    stmt.executeUpdate("drop table if exists results");
    stmt.executeUpdate("create table treeObjects (long sequence, int frequency)");

    /*
    for(){ //whole table, inorder
      long sequence;
      int frequency;
      stmt.executeUpdate("insert into treeObjects values(" + sequence + ", " + frequency + ")");
    }
    */
    
    ResultSet rs = stmt.executeQuery("select * from treeObjects");
    if (connection != null) {
      connection.close();
    }
    return rs;
  }
}
