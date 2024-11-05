//import comp4342.backend.database.DatabaseOperator;
//import org.json.JSONObject;
//import java.util.UUID;
//
//
//import java.sql.SQLException;
//
//public class DatabaseOperatorTester {
//    private static DatabaseOperator databaseOperator;
//    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        DatabaseOperatorTester tester =  new DatabaseOperatorTester();
//        tester.testinsertRegisterUser();
//    }
//
//// 用户查询测试：uid=1，echidna
////    public static void testCheckUserInfo() throws SQLException {
////        try  {
////            JSONObject resultJSON = this.databaseOperator.checkUserInfo(1);
////            if (resultJSON != null) {
////                System.out.println(resultJSON.toString());
//////        改名测试：uid=2，megumi -> rokishi
////            }
////        }catch (NullPointerException e) {
////            System.out.println("User not found");
////        }
////    }
//    public void testinsertRegisterUser() {
//        String uname = "test";
//        String email = "tester@gmail.com";
//        String password = "123456";
//        boolean result =  this.databaseOperator.insertRegister(uname, email, password);
//        System.out.println("Insert result: " + result);
//    }
//    public void testChangeName() {
//            boolean result = databaseOperator.changeName(2, "rokishi");
//            System.out.println("Change result: " + result);
//        }
//
//}
//
import com.comp4342.backend.database.DatabaseOperator;
import org.json.JSONObject;

import java.sql.SQLException;

public class DatabaseOperatorTester {
    public static DatabaseOperator databaseOperator;
    private static String uid_rokidna = "d96f962d-f8c0-4c8f-b986-b87e9c877462"; //rokidna
    private static String uid_echidna = "1ac162a4-5a24-4058-a3de-5eb0d639a3fb";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        databaseOperator = new DatabaseOperator();
//        testinsertRegisterUser();
//        testlogin();
//        testcheckUserInfoByUID();
        testinsertStartNewConversation();
        testSelectExistConversation();
//        testChangeName();
    }
    public static void testcheckUserInfoByUID() throws SQLException {

// 用户查询测试：uid=1，echidna
        JSONObject resultJSON = databaseOperator.checkUserInfoByUID(uid_rokidna);
        if (resultJSON != null) {
            System.out.println(resultJSON.toString());
        }
//        改名测试：uid=2，megumi -> rokishi
    }
    public static void testinsertRegisterUser() {
            String uname = "rokidna" ;
            String email = "rokidna@gmail.com";
            String password = "123456";
            boolean result = databaseOperator.insertRegister(uname, email, password);
            System.out.println("Insert result: " + result);

             uname = "kurumi" ;
             email = "kurumi@gmail.com";
             password = "123456";
             result = databaseOperator.insertRegister(uname, email, password);
            System.out.println("Insert result: " + result);

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
        String uid2 = uid_rokidna;
        String result = databaseOperator.insertStartNewConversation(uid1, uid2);
        System.out.println("New Conversation ID: " + result);
    }

    public static void testSelectExistConversation() throws SQLException {
        String uid1 = uid_echidna;
        String uid2 = uid_rokidna;
        String result = databaseOperator.selectExistConversation(uid1,uid2);

        System.out.println("Conversation ID: " + result);

    }
}

