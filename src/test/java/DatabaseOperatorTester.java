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
import comp4342.backend.database.DatabaseOperator;
import org.json.JSONObject;

import java.sql.SQLException;

public class DatabaseOperatorTester {
    public static DatabaseOperator databaseOperator;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        databaseOperator = new DatabaseOperator();
        testCheckUserInfo();
        testChangeName();
    }
    public static void testCheckUserInfo() throws SQLException {

// 用户查询测试：uid=1，echidna
        JSONObject resultJSON = databaseOperator.checkUserInfo(1);
        if (resultJSON != null) {
            System.out.println(resultJSON.toString());
        }
//        改名测试：uid=2，megumi -> rokishi
    }
    
    public static void testChangeName() throws SQLException {
        JSONObject resultJSON = databaseOperator.checkUserInfo(2);
        if (resultJSON!=null) {System.out.println(resultJSON.toString());}

        boolean resultJSON2 =  databaseOperator.changeName(2,"rokishi");
        System.out.println("Change result: "+resultJSON2);
    }
}

