package comp4342.frontend;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseTest {
    //Backend API的基础URL
    private static final String BASE_URL = "http://localhost:8500/api";

    public static void main(String[] args) {
        try {
            testCheckUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testCheckUser() throws Exception {
        System.out.println("Testing Check User Info API...");

        //BASE_URL + "/checkUser" 拼接出目标 API 的完整 URL，new URL() 将其转换为 URL 对象。
        URL url = new URL(BASE_URL + "/checkUserInfo");
        //url.openConnection() 打开连接，返回一个 HttpURLConnection 对象，用于配置请求信息和发送请求。
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //POST请求
        connection.setRequestMethod("POST");
        //表示发送的数据类型是 JSON 且使用 UTF-8 编码。
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        //Accept 设置为 application/json，表示期望返回的数据格式也是 JSON。
        connection.setRequestProperty("Accept", "application/json");
        //以便写入请求体内容（即发送 JSON 数据）。如果不设置为 true，就不能向服务器发送数据。
        connection.setDoOutput(true);

        //构建要发送的 JSON 字符串，user_id 设置为 1。
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
}
