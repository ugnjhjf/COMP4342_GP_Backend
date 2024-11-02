import comp4342.backend.database.DatabaseOperator;
import org.json.JSONObject;

import java.sql.SQLException;

public class DatabaseOperatorTester {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DatabaseOperator databaseOperator = new DatabaseOperator();

// 用户查询测试：uid=1，echidna
        JSONObject resultJSON =  databaseOperator.checkUserInfo(1);
        if (resultJSON!=null) {System.out.println(resultJSON.toString());}
//        改名测试：uid=2，megumi -> rokishi


        resultJSON =  databaseOperator.checkUserInfo(2);
        if (resultJSON!=null) {System.out.println(resultJSON.toString());}

        JSONObject resultJSON2 =  databaseOperator.changeName(2,"rokishi");

        resultJSON =  databaseOperator.checkUserInfo(2);
        if (resultJSON!=null) {System.out.println(resultJSON.toString());}
    }
}
