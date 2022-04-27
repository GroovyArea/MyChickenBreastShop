package me.daniel.dbTest;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnectionTest {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/?user=namyoung?characterEncoding=utf8";
    private static final String USER = "namyoung";
    private static final String PASSWORD = "1234";

    @Test
    public void testConnection() throws Exception {
        Class.forName(DRIVER);
        try {
            Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println(connection);
        } catch (Exception e){
            System.out.println("연결 실패!");
        }
    }
}
