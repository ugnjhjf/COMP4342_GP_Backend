package legacy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUserInfoTest {

    private static final String BASE_URL = "http://localhost:8500/api";

    public static void main(String[] args) {
        try {
            testCheckUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
    private static void testCheckUser() throws Exception {
        System.out.println("Testing Check User Info API...");

        // 构建 URL 对象，指定 API 的路径
        URL url = new URL(BASE_URL + "/checkUserInfo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置 HTTP 请求类型为 POST，并设置必要的请求头
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // 创建 JSON 格式的请求体（包含 user_id）
        String jsonInputString = "{\"uid\":1}";
        System.out.println("Sending JSON: " + jsonInputString);  // 打印发送的 JSON 数据

        // 发送请求体
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 获取响应码并打印
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // 读取响应内容
        try (InputStream is = connection.getInputStream()) {
            byte[] responseBytes = is.readAllBytes();
            String response = new String(responseBytes, "utf-8");
            System.out.println("Received JSON: " + response);  // 打印接收到的 JSON 数据
        }
    }
}
