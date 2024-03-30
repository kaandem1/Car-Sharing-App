package carsharing;

import carsharing.ui.UserInt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

    static final String JDBC_DRIVER = "org.h2.Driver";
    private static String dbUrl = "jdbc:h2:./src/carsharing/db/carsharing";
    public static String getDbUrl() {
        return dbUrl;
    }

    public static void main(String[] args) {
        UserInt userInterface = new UserInt();
        Connection conn = null;
        Statement stmt = null;
        for (int i = 0; i < args.length; i++) {
            if ("-databaseFileName".equals(args[i]) && i + 1 < args.length) {
                dbUrl = "jdbc:h2:./src/carsharing/db/" + args[i + 1];
                break;
            }

        }
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INT NOT NULL AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "PRIMARY KEY (ID))";
            stmt.executeUpdate(sql);
            String carTable = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
            stmt.executeUpdate(carTable);
            String customerTable = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "RENTED_CAR_ID INT, " +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
            stmt.executeUpdate(customerTable);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        userInterface.app();
    }
}