package main;


import dbService.DBConnection;
import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

import java.sql.Connection;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {
    public static void main(String[] args) {

        Connection connection = DBConnection.getH2Connection();
        DBService dbService = new DBService(connection);

        dbService.printConnectInfo();

        try {
            long userId = dbService.addUser("tully");
            System.out.println("Added user id: " + userId);

            UsersDataSet dataSet = dbService.getUser(userId);
            System.out.println("User data set: " + dataSet);

            dbService.cleanUp();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
