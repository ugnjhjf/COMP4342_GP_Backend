package com.comp4342.backend.api.Legacy;

import com.comp4342.backend.database.DatabaseOperator;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BackendAPIProvider2 {
    private final DatabaseOperator dbOperator;
    private ResultSet resultSet;

    public BackendAPIProvider2() throws ClassNotFoundException {
        this.dbOperator = new DatabaseOperator();

    }

    public void startServer() throws IOException {
        //在8500端口启动一个http服务器
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        //定义API路由，绑定checkUserInfo到这个路由(/api/checkUserInfo)上
        server.createContext("/api/checkUserInfo", new checkUserInfo());
        //单线程执行器
        server.setExecutor(null);
        //启动服务器
        server.start();
        System.out.println("Server started on port 8500");
    }

    private class checkUserInfo implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    String uid = requestBody.getString("uid");

                    JSONObject userInfo = dbOperator.checkUserInfoByUID(uid);  // 获取用户信息

                    JSONObject response = new JSONObject();
                    if (userInfo != null) {
                        response.put("isExecuteSuccess", true);
                        response.put("result", userInfo);  // 把用户信息作为结果返回
                    } else {
                        response.put("isExecuteSuccess", false);
                        response.put("error", "User does not exist");
                    }

                    sendResponse(exchange, response.toString());  // 发送 JSON 响应
                } catch (Exception e) {
                    e.printStackTrace();
                    sendErrorResponse(exchange, "Error processing request");
                }
            } else {
                sendErrorResponse(exchange, "Invalid request method");
            }
        }
    }

//    private class checkUserInfo implements HttpHandler {
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//
//            //检查请求方法是否为POST，不是POST则返回错误响应
//            if ("POST".equals(exchange.getRequestMethod())) {
//                try {
//                    //将请求的内容（即请求体）读取为字节数组，并转换为字符串，
//                    //然后用 JSONObject 解析为 JSON 对象。
//                    //这意味着客户端发送的请求体应该是 JSON 格式。
//                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
//
//                    // 获取 user_id，假设它在JSON中是一个整数
//                    int user_id = requestBody.getInt("user_id");
//
//                    // 检查用户是否存在
//                    JSONObject execute_result = dbOperator.checkUserInfo(user_id);
//
//                    if (execute_result != null) {
//                        System.out.println("User exists");
//                    } else {
//                        System.out.println("User does not exist");
//                    }
//                    JSONObject response = new JSONObject();
//
//                    //把执行结果和查询结果放入响应中(服务器回复：是否成功+结果）
//                    response.put("isExecuteSuccess", execute_result);
//                    response.put("result", dbOperator.getResultSet());
//
//                    //回复前端请求的客户端
//                    sendResponse(exchange, response.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    sendErrorResponse(exchange, "Error processing request");
//                }
//            } else {
//                sendErrorResponse(exchange, "Invalid request method");
//            }
//        }
//    }
//
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
        BackendAPIProvider2 apiProvider = new BackendAPIProvider2();
        apiProvider.startServer();
    }
}

