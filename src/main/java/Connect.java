import java.sql.*;

public class Connect {
    private static Connect connection = new Connect();
    private Connection con;

    public Connection getCon() {
        return con;
    }

    public static Connect getInstance() {
        if (connection == null)
            new Connect();
        return connection;
    }

    private Connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/dungeons_dragons";
            String user = "root";
            String passwd = "";
            con = DriverManager.getConnection(url, user, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
