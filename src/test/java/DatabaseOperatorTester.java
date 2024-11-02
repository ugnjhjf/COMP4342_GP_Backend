import comp4342.backend.database.DatabaseOperator;
import org.json.JSONObject;

import java.sql.SQLException;

public class DatabaseOperatorTester {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DatabaseOperator databaseOperator = new DatabaseOperator();
        JSONObject resultJSON =  databaseOperator.checkUserInfo(1);
        if (resultJSON!=null)
        {
            System.out.println(resultJSON.toString());
        }

    }
}
