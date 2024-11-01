package comp4342.backend.api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BackendAPIProvider {
    private final DatabaseOperator dbOperator;
    private ResultSet resultSet;

    public BackendAPIProvider() throws ClassNotFoundException {
        this.dbOperator = new DatabaseOperator();
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        server.createContext("/api/insertUser", new InsertUserHandler());
        server.createContext("/api/sendMessage", new SendMessageHandler());
        server.createContext("/api/friendList", new FriendListHandler());
        server.createContext("/api/checkUserStatus", new CheckUserStatusHandler());
        server.createContext("/api/checkUser", new checkUser());
        server.setExecutor(null); // 使用默认的 executor
        server.start();
        System.out.println("Server started on port 8500");
    }


    private class checkUser implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));

                    // 获取 user_id，假设它在JSON中是一个整数
                    int user_id = requestBody.getInt("user_id");

                    // 检查用户是否存在
                    boolean execute_result = dbOperator.checkuser(user_id);

                    JSONObject response = new JSONObject();
                    response.put("success", execute_result);
                    response.put("result", dbOperator.getResultSet());

                    sendResponse(exchange, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendErrorResponse(exchange, "Error processing request");
                }
            } else {
                sendErrorResponse(exchange, "Invalid request method");
            }
        }
    }
    private class InsertUserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    String uname = requestBody.getString("uname");
                    String email = requestBody.getString("email");
                    String password = requestBody.getString("password");

                    boolean result = dbOperator.insertNewUser(uname, email, password);
                    JSONObject response = new JSONObject();
                    response.put("success", result);

                    sendResponse(exchange, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendErrorResponse(exchange, "Error processing request");
                }
            } else {
                sendErrorResponse(exchange, "Invalid request method");
            }
        }
    }

    // 发送消息的处理程序
    private class SendMessageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    int senderId = requestBody.getInt("senderId");
                    int receiverId = requestBody.getInt("receiverId");
                    String content = requestBody.getString("content");

                    boolean result = dbOperator.insertNewMessage(senderId, receiverId, content);
                    JSONObject response = new JSONObject();
                    response.put("success", result);

                    sendResponse(exchange, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendErrorResponse(exchange, "Error processing request");
                }
            } else {
                sendErrorResponse(exchange, "Invalid request method");
            }
        }
    }

    // 获取好友列表的处理程序
    private class FriendListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                try {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    int userId = requestBody.getInt("userId");

                    JSONObject response = new JSONObject();
                    response.put("friends", dbOperator.checkFriendList(userId)); // 假设返回的 JSON 结果格式

                    sendResponse(exchange, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendErrorResponse(exchange, "Error processing request");
                }
            } else {
                sendErrorResponse(exchange, "Invalid request method");
            }
        }
    }

    // 检查用户在线状态的处理程序
    private class CheckUserStatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    int userId = requestBody.getInt("userId");

                    boolean isOnline = dbOperator.checkUserIsOnline(userId);
                    JSONObject response = new JSONObject();
                    response.put("online", isOnline);

                    sendResponse(exchange, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendErrorResponse(exchange, "Error processing request");
                }
            } else {
                sendErrorResponse(exchange, "Invalid request method");
            }
        }
    }

    // 发送成功响应
    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // 发送错误响应
    private void sendErrorResponse(HttpExchange exchange, String message) throws IOException {
        JSONObject response = new JSONObject();
        response.put("error", message);
        sendResponse(exchange, response.toString());
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
//        DatabaseConnector dbConnector = new DatabaseConnector();
//        DatabaseOperator dbOperator = new DatabaseOperator(dbConnector);
        BackendAPIProvider apiProvider = new BackendAPIProvider();
        apiProvider.startServer();
    }
}

