package org.thraex.supplier.db;

import lombok.extern.log4j.Log4j2;
import oracle.jdbc.driver.OracleDriver;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author 鬼王
 * @date 2019/09/23 15:17
 */
@Log4j2
public class TestConnection {

    private final static String URL = "jdbc:oracle:thin:@10.27.213.155:1521:XE";
    private final static String SQL = "SELECT * FROM V$VERSION ORDER BY BANNER";
    private final static String USERNAME = "kygl";
    private final static String PASSWORD = "PTRkygl413";
    private final static String LABEL = "BANNER";

    @Test
    public void oracle1() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info(connection);

            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String banner = resultSet.getString(LABEL);
                log.info(banner);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) { preparedStatement.close(); }
                if (preparedStatement != null) { preparedStatement.close(); }
                if (connection != null) { connection.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void oracle2() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            OracleDriver driver = new OracleDriver();
            DriverManager.deregisterDriver(driver);
            Properties pro = new Properties();
            pro.put("user", USERNAME);
            pro.put("password", PASSWORD);
            connection = driver.connect(URL, pro);
            log.info(connection);

            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String banner = resultSet.getString(LABEL);
                log.info(banner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) { preparedStatement.close(); }
                if (preparedStatement != null) { preparedStatement.close(); }
                if (connection != null) { connection.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
