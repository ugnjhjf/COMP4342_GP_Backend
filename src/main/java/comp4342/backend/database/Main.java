package comp4342.backend.database;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
//        2.用户信息和url
        String url = "jdbc:mysql://www.gnetwork.space/chat_app?useUnicode=true&characterEncoding=utf-8&connectTimeout=3000&socketTimeout=3000";
        String username="root";
        String password="SecretDB";
//        3.连接成功，数据库对象 Connection
        Connection connection = DriverManager.getConnection(url,username,password);
//        4.执行SQL对象Statement，执行SQL的对象
        Statement statement = connection.createStatement();
//        5.执行SQL的对象去执行SQL，返回结果集
        String sql = "SELECT * FROM user;";
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
        connection.close();
    }
}

