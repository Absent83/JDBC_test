package dbService;

import dbService.dao.UsersDAO;
import dbService.dataSets.UsersDataSet;

import java.sql.Connection;
import java.sql.SQLException;

public class DBService {
    private static volatile DBService instance;
    private final Connection connection;
    private final UsersDAO usersDAO;


    public static DBService getInstance(Connection connection) {
        if (instance == null) {
            synchronized (DBService.class) {
                if (instance == null) {
                    instance = new DBService(connection);
                }
            }
        }
        return instance;
    }

    private DBService(Connection connection) {
        this.connection = connection;
        usersDAO = UsersDAO.getInstance(connection);
    }

    public UsersDataSet getUser(long id) throws DBException {
        try {
            return (usersDAO.get(id));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public long addUser(String name) throws DBException {
        try {
            connection.setAutoCommit(false);
            usersDAO.createTable();
            usersDAO.insertUser(name);
            connection.commit();
            return usersDAO.getUserId(name);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void cleanUp() throws DBException {
        try {
            usersDAO.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
