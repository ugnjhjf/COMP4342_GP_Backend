package legacy;

import comp4342.backend.database.DatabaseConnector;

import java.sql.*;

public class DatabaseConnectorTest {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connection = databaseConnector.getConnection();
            testUserSelect();
            testChangeName("Megumi v3",2);
            testUserSelect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void testUserSelect() throws ClassNotFoundException, SQLException {

//        4.执行SQL对象Statement，执行SQL的对象
        Statement statement = connection.createStatement();
//        5.执行SQL的对象去执行SQL，返回结果集
        String sql = "SELECT * FROM user where uid=" +2 +";";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println("id = " + resultSet.getString("uid"));
            System.out.println("Name = " + resultSet.getString("uname"));
            System.out.println("Email = " + resultSet.getString("email"));
            System.out.println("Password = " + resultSet.getString("password"));
            System.out.println("Registration Time = " + resultSet.getString("regtime"));
        }

//        6.释放连接
        resultSet.close();
        statement.close();
    }
    public static void testChangeName(String newName,int userId ) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE user SET uname = ? WHERE uid = ?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

}

