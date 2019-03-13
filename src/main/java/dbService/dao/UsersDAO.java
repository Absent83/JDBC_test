package dbService.dao;

import dbService.dataSets.UsersDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;


public class UsersDAO {
    private static volatile UsersDAO instance;
    private final Connection connection;


    public static UsersDAO getInstance(Connection connection) {
        if (instance == null) {
            synchronized (UsersDAO.class) {
                if (instance == null) {
                    instance = new UsersDAO(connection);
                }
            }
        }
        return instance;
    }


    private UsersDAO(Connection connection) {
        this.connection = connection;
    }

    public UsersDataSet get(long id) throws SQLException {
        Executor executor = new Executor(connection);
        return executor.execQuery("select * from users where id=" + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2));
        });
    }

    public long getUserId(String name) throws SQLException {
        Executor executor = new Executor(connection);
        return executor.execQuery("select * from users where user_name='" + name + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void insertUser(String name) throws SQLException {
        Executor executor = new Executor(connection);
        executor.execUpdate("insert into users (user_name) values ('" + name + "')");
    }

    public void createTable() throws SQLException {
        Executor executor = new Executor(connection);
        executor.execUpdate("create table if not exists users (id bigint auto_increment, user_name varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        Executor executor = new Executor(connection);
        executor.execUpdate("drop table users");
    }
}
