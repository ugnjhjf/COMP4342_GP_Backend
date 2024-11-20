import com.comp4342.frontend.api.FrontendAPIProvider;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 使用同一个实例避免多次 WebSocket 连接
public class FrontendAPIProviderJUnitTest {
    private FrontendAPIProvider client;

    @BeforeAll
    public void setUp() throws URISyntaxException, InterruptedException {
        URI serverURI = new URI("ws://localhost:8080/backend-api");
        client = new FrontendAPIProvider(serverURI);
        client.connectBlocking();
    }

    @AfterAll
    public void tearDown() {
        client.close();
    }

    @Test
    @DisplayName("Test Register User")
    public void testRegister() throws Exception {
        client.register("TestUser5", "testuser5@example.com", "password123");
        Thread.sleep(1000);
        Assertions.assertTrue(client.success, "Register should succeed");
        client.register("TestUser5", "testuser5@example.com", "password123");
        Thread.sleep(1000);
        Assertions.assertFalse(client.success, "Register should succeed");
    }

    @Test
    @DisplayName("Test Login User")
    public void testLogin() throws Exception {
        client.login("Rokidna@gnetwork.com", "password123");
        Thread.sleep(1000);
        Assertions.assertNotNull(client.uid, "UID should not be null after login");
        Assertions.assertTrue(client.success, "Login should succeed");
    }

    @Test
    @DisplayName("Test Add New Friend")
    public void testAddNewFriend() throws Exception {
        client.addNewFriend("fbc62d89-31fe-47d0-a1aa-20b1fad88ca8", "asdewxflilx@gmail.com");
        Thread.sleep(1000);
        Assertions.assertTrue(client.success, "Adding a friend should succeed");
        client.addNewFriend("fbc62d89-31fe-47d0-a1aa-20b1fad88ca8", "asdewxflilx@gmail.com");
        Thread.sleep(1000);
        Assertions.assertFalse(client.success, "Adding a friend should succeed");
    }

    @Test
    @DisplayName("Test Get User Friend List")
    public void testGetUserFriendList() throws Exception {
        client.getUserFriendList("13365bd6-f13e-4572-8e3b-68bfb15172e5");
        Thread.sleep(1000);
        Assertions.assertFalse(client.success);
    }

    @Test
    @DisplayName("Test Get All Messages")
    public void testGetAllMessages() throws Exception {
        client.getAllMessage("13365bd6-f13e-4572-8e3b-68bfb15172e5", "19a8d2c1-9ad4-41c9-b6e7-23e43beee909");
        Thread.sleep(1000);
        Assertions.assertFalse(client.success);
    }

    @Test
    @DisplayName("Test Send New Message")
    public void testSendNewMessage() throws Exception {
        client.sendNewMessage("7b2442e6-ae95-45f4-a2bf-c5a5b8051d6c", "13365bd6-f13e-4572-8e3b-68bfb15172e5", "Hello, Test!");
        Thread.sleep(1000);
        Assertions.assertTrue(client.success, "Sending a message should succeed");
    }

    @Test
    @DisplayName("Test Get Latest Message")
    public void testGetLatestMessage() throws Exception {
        client.getLatestMessage("7b2442e6-ae95-45f4-a2bf-c5a5b8051d6c", "13365bd6-f13e-4572-8e3b-68bfb15172e5");
        Thread.sleep(1000);
        Assertions.assertNotNull(client.latest_message, "Latest message should not be null");
    }

    @Test
    @DisplayName("Test Get Friend Request List")
    public void testGetFriendRequestList() throws Exception {
        client.getFriendRequestList("7b2442e6-ae95-45f4-a2bf-c5a5b8051d6c");
        Thread.sleep(1000);
        Assertions.assertNotNull(client.request_friendList, "Friend request list should not be null");
    }
}
