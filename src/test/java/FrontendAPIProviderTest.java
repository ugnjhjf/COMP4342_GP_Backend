import com.comp4342.frontend.api.FrontendAPIProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Thread.sleep;

public class FrontendAPIProviderTest {
    private FrontendAPIProvider client;

    // 提供的用户 ID
    private String uid_rokidna2 = "3964a988-8b32-42f3-9d11-14b75eb1b925";

    private String email_Tekon2 = "Tekon@exdample.com";


    private String uid_Tevck = "267c693e-0e89-4a7d-bcdf-f1cc953b0027";

    //Not Friend
    private String uid_Tekon = "78dfad2c-1405-44f9-81b2-978ac29e8e86";
    //IsFriend
    private String uid_Tekon2 = "184bc12a-2b5e-41a4-8342-d997ca0e7666";
    private String uid_newName = "3964a988-8b32-42f3-9d11-14b75eb1b925";

    public FrontendAPIProviderTest() throws URISyntaxException, InterruptedException {
        // 初始化 WebSocket 客户端
        URI serverURI = new URI("ws://localhost:8080/backend-api");
        client = new FrontendAPIProvider(serverURI);
        client.connectBlocking(); // 阻塞直到连接成功
    }


    public void runTests() throws InterruptedException {
//        testRegister();
//        testLogin();



        testGetConversationID();
//        testAddNewFriend();
//        testAddNewFriend2();
//        sleep(1000);
//        testIsFriendRequestAccept();
//        testIsFriendRequestAccept2();


//        testDeleteFriend();
//        testChangePassword();
//        testChangeName();
//        testGetUserInfoByUID();
//        testGetUserInfoByEmail();
//        testGetUserFriendList();
        //
//        testIsUserOnline();
//        testIsFriendByEmail();
////        testIsFriendByEmail2();
//        testIsFriendByUID();
//        testIsFriendByUID2();
        //Pass Test↑



//        testSendNewMessage();
//        testGetLatestMessage();
//        testGetAllMessages();
    }

    private void testRegister() throws InterruptedException {
        client.register("TTTT", "TTTTT@example.com", "password123");
        sleep(1000);
        System.out.println("Register Test - Success: " + client.success);
    }

    private void testLogin() throws InterruptedException {
        client.login("TTTTT@example.com", "password123");
        sleep(1000);
        System.out.println("UID: " + client.uid);
        System.out.println("Login Test - Success: " + client.success);
    }

    private void testGetConversationID() throws InterruptedException {
        client.getConversationID(uid_Tekon2, "rokidna2@gnetwork.com");
        sleep(1000);
        System.out.println("Get Conversation ID Test - CID: " + client.cid);
    }
    private void testGetConversationID2() throws InterruptedException {
        client.getConversationID(uid_Tekon2, "rokidna2@gnetwork.com");
        sleep(1000);
        System.out.println("Get Conversation ID Test - CID: " + client.cid);
    }
    private void testAddNewFriend() throws InterruptedException {
        client.addNewFriend(uid_rokidna2, email_Tekon2);
        sleep(1000);
        System.out.println("Add New Friend Test - Success: " + client.success);
    }

    private void testAddNewFriend2() throws InterruptedException {
        client.addNewFriend("78dfad2c-1405-44f9-81b2-978ac29e8e86", "Tekon@exdample.com");
        sleep(1000);
        System.out.println("Add New Friend Test - Success: " + client.success);
    }

    private void testIsFriendRequestAccept() throws InterruptedException {
        client.isFriendRequestAccept(uid_Tekon2, uid_rokidna2, "accepted");
        sleep(1000);
        System.out.println("Is Friend Request Accept Test - Success: " + client.success);
    }

    private void testIsFriendRequestAccept2() throws InterruptedException {
        client.isFriendRequestAccept("45361cfc-b7ef-4e2b-860d-02fd1515e2d7 ","78dfad2c-1405-44f9-81b2-978ac29e8e86" , "accepted");
        sleep(1000);
        System.out.println("Is Friend Request Accept Test - Success: " + client.success);
    }

    private void testDeleteFriend() throws InterruptedException {
        client.deleteFriend(uid_rokidna2, uid_Tekon2);
        sleep(1000);
        System.out.println("Delete Friend Test - Success: " + client.success);
    }

    private void testChangePassword() throws InterruptedException {
        client.changePassword(uid_rokidna2, "newPassword");
        sleep(1000);
        System.out.println("Change Password Test - Success: " + client.success);
    }

    private void testChangeName() throws InterruptedException {
        client.changeName(uid_rokidna2, "newName");
        sleep(1000);
        System.out.println("Change Name Test - Success: " + client.success);
    }

    private void testGetUserInfoByUID() throws InterruptedException {
        client.getUserInfoByUID(uid_rokidna2);
        sleep(1000);
        System.out.println("Get User Info By UID Test - Name: " + client.uname + ", Email: " + client.email);
    }

    private void testGetUserInfoByEmail() throws InterruptedException {
        client.getUserInfoByEmail("testUser@example.com");
        sleep(1000);
        System.out.println("Get User Info By Email Test - UID: " + client.uid + ", Name: " + client.uname);
    }

    private void testGetUserFriendList() throws InterruptedException {
        client.getUserFriendList(uid_Tekon2);
        sleep(1000);
        System.out.println("Get User Friend List Test - Friend List: " + client.friend_list);
    }



    private void testIsUserOnline() throws InterruptedException {
        client.isUserOnline(uid_rokidna2);
        sleep(1000);
        System.out.println("Is User Online Test - Success: " + client.success);
    }

    private void testIsFriendByEmail() throws InterruptedException {
        client.isFriendByEmail("3964a988-8b32-42f3-9d11-14b75eb1b925", email_Tekon2);
        sleep(1000);
        System.out.println("Is Friend Test - Success: " + client.success);
    }

    private void testIsFriendByEmail2() throws InterruptedException {
        client.isFriendByEmail("3964a988-8b32-42f3-9d11-14b75eb1b925", "Tekon");
        sleep(1000);
        System.out.println("Is Friend Test - Success: " + client.success);
    }

    private void testIsFriendByUID() throws InterruptedException {
        client.isFriendByUID(uid_Tekon,uid_Tekon2 ); //Expect False
        sleep(1000);
        System.out.println("Is Friend Test - Success: " + client.success);
    }

    private void testIsFriendByUID2() throws InterruptedException {
        client.isFriendByUID(uid_newName, uid_Tekon2);
        sleep(1000);
        System.out.println("Is Friend Test - Success: " + client.success);
    }

    private void testSendNewMessage() throws InterruptedException {
        client.sendNewMessage(uid_Tekon2, uid_newName, "Hello!");
        sleep(1000);
        System.out.println("Send New Message Test - Success: " + client.success);
    }

    private void testGetLatestMessage() throws InterruptedException {
        client.getLatestMessage(uid_rokidna2, uid_Tekon2);
        sleep(1000);
        System.out.println("Get Latest Message Test - Latest Message: " + client.latest_message);
    }

    private void testGetAllMessages() throws InterruptedException {
        client.getAllMessage(uid_rokidna2, uid_Tekon2);
        sleep(1000);
        System.out.println("Get All Messages Test - All Messages: " + client.all_message);
    }

    public static void main(String[] args) {
        try {
            FrontendAPIProviderTest testClient = new FrontendAPIProviderTest();
            testClient.runTests();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
