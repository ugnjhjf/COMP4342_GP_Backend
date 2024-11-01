package comp4342.frontend.test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseTest2 {

    private static final String BASE_URL = "http://localhost:8500/api";

    public static void main(String[] args) {
        try {
            testInsertUser();
//            testSendMessage();
//            testFriendList();
//            testCheckUserStatus();
            testCheckUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testCheckUser() throws Exception {
        System.out.println("Testing Check User Info API...");
        URL url = new URL(BASE_URL + "/checkUser");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"user_id\":1}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        System.out.println("Response Code: " + code);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
        }
    }
    private static void testInsertUser() throws Exception {
        System.out.println("Testing Insert User API...");

        // 定义请求的 URL
        URL url = new URL(BASE_URL + "/insertUser");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");  // 设置请求方法为 POST
        connection.setRequestProperty("Content-Type", "application/json; utf-8");  // 请求的内容类型为 JSON
        connection.setRequestProperty("Accept", "application/json");  // 期望返回 JSON
        connection.setDoOutput(true);  // 允许写入请求体

        // 构建 JSON 格式的请求体
        String jsonInputString = "{\"uname\":\"testUser\",\"email\":\"test@example.com\",\"password\":\"password123\"}";

        // 将 JSON 数据写入到请求体中
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 获取 HTTP 响应码
        int code = connection.getResponseCode();
        System.out.println("Response Code: " + code);

        // 读取并打印响应内容
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
        }
    }


//    private static void testInsertUser() throws Exception {
//        System.out.println("Testing Insert User API...");
//        URL url = new URL(BASE_URL + "/insertUser");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json; utf-8");
//        connection.setRequestProperty("Accept", "application/json");
//        connection.setDoOutput(true);
//
//        String jsonInputString = "{\"uname\":\"testUser\",\"email\":\"test@example.com\",\"password\":\"password123\"}";
//
//        try (OutputStream os = connection.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int code = connection.getResponseCode();
//        System.out.println("Response Code: " + code);
//
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//            StringBuilder response = new StringBuilder();
//            String responseLine;
//            while ((responseLine = br.readLine()) != null) {
//                response.append(responseLine.trim());
//            }
//            System.out.println("Response: " + response.toString());
//        }
//    }

    private static void testSendMessage() throws Exception {
        System.out.println("Testing Send Message API...");
        URL url = new URL(BASE_URL + "/sendMessage");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"senderId\":1,\"receiverId\":2,\"content\":\"Hello, this is a test message!\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        System.out.println("Response Code: " + code);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
        }
    }

    private static void testFriendList() throws Exception {
        System.out.println("Testing Friend List API...");
        URL url = new URL(BASE_URL + "/friendList");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"userId\":1}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        System.out.println("Response Code: " + code);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
        }
    }

    private static void testCheckUserStatus() throws Exception {
        System.out.println("Testing Check User Status API...");
        URL url = new URL(BASE_URL + "/checkUserStatus");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"userId\":1}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        System.out.println("Response Code: " + code);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
        }
    }
}
