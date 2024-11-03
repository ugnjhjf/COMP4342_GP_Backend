package comp4342.backend.legacy;

import comp4342.backend.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperator2 {
    private DatabaseConnector dbConnector;
    //        4.执行SQL对象Statement，执行SQL的对象
    private ResultSet resultSet;
    public DatabaseOperator2() throws ClassNotFoundException {
        this.dbConnector = new DatabaseConnector();
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public boolean checkuser(int user_id) {
        String sql = "select * from user where uid = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
   ;        stmt.setInt(1, user_id);
            stmt.executeUpdate();
            resultSet = stmt.executeQuery(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertNewUser(String user_name, String email, String password) {
        String sql = "INSERT INTO user (user_id, user_name, email, password, regtime) VALUES (DEFAULT, ?, ?, ?, DEFAULT)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user_name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean insertNewMessage(int senderId, int receiverId, String content) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, content);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertNewConservation(int userId1, int userId2) {
        String sql = "INSERT INTO conservations (user1_id, user2_id) VALUES (?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertNewFriend(int userId, int friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertApprovedFriendRequest(int requestId) {
        String sql = "UPDATE friend_requests SET status = 'approved' WHERE request_id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertDeclineFriendRequest(int requestId) {
        String sql = "UPDATE friend_requests SET status = 'declined' WHERE request_id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE uid = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeName(int userId, String newName) {
        String sql = "UPDATE user SET uname = ? WHERE uid = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet checkFriendList(int userId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkUserIsOnline(int userId) {
        String sql = "SELECT online_status FROM user WHERE uid = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("online_status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
