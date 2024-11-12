import com.comp4342.backend.database.DatabaseOperator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class DatabaseOperatorFucntionDirectTester {
    public static DatabaseOperator databaseOperator;
    private static String uid_rokidna = "d96f962d-f8c0-4c8f-b986-b87e9c877462"; //rokidna
    private static String uid_echidna = "1ac162a4-5a24-4058-a3de-5eb0d639a3fb";
    private static String uid_mojicaa = "63346ffe-0ace-4b8a-8594-ac17057082c2";

    private static String result;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        databaseOperator = new DatabaseOperator();
//        testinsertRegisterUser();
//        testlogin();
//        testcheckUserInfoByUID();
//        testcheckUserInfoByEmail();
//        testCheckUserIsOnline();
//        testInsertNewFriend();
        testCheckUserFriendlist();
//        testinsertStartNewConversation();
//        try {
////            testInsertNewFriend();
//            testUpdateFriendRequest();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        testinsertStartNewConversation();
//        testSelectExistConversation();
//        testChangeName();
    }
    public static void testcheckUserInfoByEmail() throws SQLException {
        JSONObject resultJSON = databaseOperator.checkUserInfoByEmail("monika@uvuv.com");
        if (resultJSON != null) {
            System.out.println(resultJSON.toString());
        }
//        改名测试：uid=2，megumi -> rokishi
    }
    public static void testcheckUserInfoByUID() throws SQLException {
        JSONObject resultJSON = databaseOperator.checkUserInfoByUID(uid_rokidna);
        if (resultJSON != null) {
            System.out.println(resultJSON.toString());
        }
    }
    public static void testInsertNewFriend() throws SQLException {
        String uid = databaseOperator.checkUserInfoByUID(uid_mojicaa).getString("uid"); ;
        String fid = databaseOperator.checkUserInfoByUID(uid_rokidna).getString("uid");
        boolean result = databaseOperator.insertNewFriend(uid, fid, "requested");
        System.out.println("Insert result: " + result);
    }

    public static void testUpdateFriendRequest() throws SQLException {
        String uid = databaseOperator.checkUserInfoByUID(uid_mojicaa).getString("uid"); ;
        String fid = databaseOperator.checkUserInfoByUID(uid_rokidna).getString("uid");
        boolean result = databaseOperator.updateFriendRequest(uid, fid, "accepted");
        System.out.println("Update result: " + result);
    }

    public static void testinsertRegisterUser() {
            String uname = "mojicaa" ;
            String email = "mojicaaa@gmail.com";
            String password = "123456a";
            boolean result = databaseOperator.insertRegister(uname, email, password);
            System.out.println("Insert result: " + result);

    }

    public static void testCheckUserIsOnline() throws SQLException {
       boolean result = databaseOperator.checkUserIsOnline(uid_rokidna);
        System.out.println("Check result: " + result);
    }

    public static void testCheckUserFriendlist() throws SQLException {
        JSONArray resultJSON = databaseOperator.checkUserFriendlist(uid_rokidna);
        if (resultJSON != null) {
            System.out.println(resultJSON.toString());
        }
    }

    
//    public static void testChangeName() throws SQLException {
//        JSONObject resultJSON = databaseOperator.checkUserInfo();
//        if (resultJSON!=null) {System.out.println(resultJSON.toString());}
//
//        boolean resultJSON2 =  databaseOperator.changeName(2,"rokishi");
//        System.out.println("Change result: "+resultJSON2);
//    }
    public static void testlogin() throws SQLException{
        JSONObject resultJSON = databaseOperator.login("rokidna@gmail.com","123456");
        if (resultJSON.getBoolean("isLogonSucessful")){
            System.out.println("Hello!"+resultJSON.getString("uname"));
        }else{
            System.out.println("Incorrect password / User not exist");
        }
    }


    public static void testinsertStartNewConversation() throws SQLException {
        String uid1 = uid_echidna;
        String uid2 = uid_mojicaa;
        String result = databaseOperator.checkConversationID(uid1, uid2);
        System.out.println("New Conversation ID: " + result);
    }

}

