import com.comp4342.frontend.api.FrontendAPIProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Thread.sleep;

public class FrontendAPIProviderTest {
    private FrontendAPIProvider client;

    public FrontendAPIProviderTest() throws URISyntaxException, InterruptedException {
        URI serverURI = new URI("ws://localhost:8080/backend-api");
        client = new FrontendAPIProvider(serverURI);
        client.connectBlocking();
    }

    public void runTests() throws InterruptedException {
        // Run tests with desired parameters
//        testRegister("Rokidna", "Rokidna@gnetwork.com", "password123");
//        testRegister("Tekon", "Tekon@tech.com", "password123");
         String uid_rokidna = "7b2442e6-ae95-45f4-a2bf-c5a5b8051d6c";
         String email_rokidna = "Rokidna@gnetwork.com";

         String uid_tekon = "8150ebba-65f4-4792-9e07-71d5e86746fe";
         String email_tekon = "Tekon@tech.com";

         //test
//         testAddNewFriend(uid_rokidna,email_tekon);
//         testGetConversationIDByEmail(uid_rokidna,email_tekon);
//         testGetConversationIDByID(uid_rokidna,uid_tekon);
//         testSendNewMessage(uid_tekon,uid_rokidna,"Hello Rokidna");
//         sleep(1000);
//        testSendNewMessage(uid_rokidna,uid_tekon,"Hello Tekon");
//        testGetAllMessages(uid_rokidna,uid_tekon);
        testGetLatestMessage(uid_rokidna,uid_tekon);
//        testLogin("Rokidna@gnetwork.com", "password123");
    }
//
//    private void testGetConversationIDByID(String uidRokidna, String uidTekon) throws InterruptedException {
//        client.getConversationIDByID(uidRokidna,uidTekon);
//        sleep(1000);
//        System.out.println("Get Conversation ID Test - CID: " + client.cid);
//    }

    private void testRegister(String uname, String email, String password) throws InterruptedException {
        client.register(uname, email, password);
        sleep(1000);
        System.out.println("Register Test - Success: " + client.success);
    }

    private void testLogin(String email, String password) throws InterruptedException {
        client.login(email, password);
        sleep(1000);
        System.out.println("UID: " + client.uid);
        System.out.println("Login Test - Success: " + client.success);
    }

    private void testGetConversationIDByEmail(String uid, String fidEmail) throws InterruptedException {
        client.getConversationIDByEmail(uid, fidEmail);
        sleep(1000);
        System.out.println("Get Conversation ID Test - CID: " + client.cid);
    }

    private void testAddNewFriend(String uid, String friendEmail) throws InterruptedException {
        client.addNewFriend(uid, friendEmail);
        sleep(1000);
        System.out.println("Add New Friend Test - Success: " + client.success);
    }

    private void testIsFriendRequestAccept(String uid, String fid, String status) throws InterruptedException {
        client.isFriendRequestAccept(uid, fid, status);
        sleep(1000);
        System.out.println("Is Friend Request Accept Test - Success: " + client.success);
    }

    private void testDeleteFriend(String uid, String fid) throws InterruptedException {
        client.deleteFriend(uid, fid);
        sleep(1000);
        System.out.println("Delete Friend Test - Success: " + client.success);
    }

    private void testChangePassword(String uid, String newPassword) throws InterruptedException {
        client.changePassword(uid, newPassword);
        sleep(1000);
        System.out.println("Change Password Test - Success: " + client.success);
    }

    private void testChangeName(String uid, String newName) throws InterruptedException {
        client.changeName(uid, newName);
        sleep(1000);
        System.out.println("Change Name Test - Success: " + client.success);
    }

    private void testGetUserInfoByUID(String uid) throws InterruptedException {
        client.getUserInfoByUID(uid);
        sleep(1000);
        System.out.println("Get User Info By UID Test - Name: " + client.uname + ", Email: " + client.email);
    }

    private void testGetUserInfoByEmail(String email) throws InterruptedException {
        client.getUserInfoByEmail(email);
        sleep(1000);
        System.out.println("Get User Info By Email Test - UID: " + client.uid + ", Name: " + client.uname);
    }

    private void testGetUserFriendList(String uid) throws InterruptedException {
        client.getUserFriendList(uid);
        sleep(1000);
        System.out.println("Get User Friend List Test - Friend List: " + client.friend_list);
    }

    private void testIsUserOnline(String uid) throws InterruptedException {
        client.isUserOnline(uid);
        sleep(1000);
        System.out.println("Is User Online Test - Success: " + client.success);
    }

    private void testIsFriendByEmail(String uid, String email) throws InterruptedException {
        client.isFriendByEmail(uid, email);
        sleep(1000);
        System.out.println("Is Friend By Email Test - Success: " + client.success);
    }

    private void testIsFriendByUID(String uid, String fid) throws InterruptedException {
        client.isFriendByUID(uid, fid);
        sleep(1000);
        System.out.println("Is Friend By UID Test - Success: " + client.success);
    }

    private void testSendNewMessage(String uid, String fid, String content) throws InterruptedException {
        client.sendNewMessage(uid, fid, content);
        sleep(1000);
        System.out.println("Send New Message Test - Success: " + client.success);
    }

    private void testGetLatestMessage(String uid, String fid) throws InterruptedException {
        client.getLatestMessage(uid, fid);
        sleep(1000);
        System.out.println("Get Latest Message Test - Latest Message: " + client.latest_message);
    }

    private void testGetAllMessages(String uid, String fid) throws InterruptedException {
        client.getAllMessage(uid, fid);
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
