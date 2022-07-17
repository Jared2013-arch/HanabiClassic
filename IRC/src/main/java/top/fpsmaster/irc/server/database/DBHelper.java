package top.fpsmaster.irc.server.database;

import top.fpsmaster.irc.server.ServerMain;
import top.fpsmaster.irc.utils.MD5Utils;

import java.sql.*;

public class DBHelper {
    static Connection connection;

    public static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("jdbc:mysql://" + ServerMain.dbAddress + ":" + ServerMain.dbPort + "/" + ServerMain.dbName);
            connection = DriverManager.getConnection("jdbc:mysql://" + ServerMain.dbAddress + ":" + ServerMain.dbPort + "/" + ServerMain.dbName + "?serverTimezone=UTC", ServerMain.dbUserName, ServerMain.dbPWD);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String login(String username, String password) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM `users` WHERE `username` = '" + username + "' AND `password_md5` = '" + password + "'");
            while (rs.next()) {
                return rs.getString(3);
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Failed to login";
    }

    public static String register(String username, String password, String key) {
        password = MD5Utils.getMD5(password);
        try {
            // 检查用户是否已存在
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `username` = ?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return "User already exists";
            }
            // 检查key是否可用
            String rank = "User";
            if (!key.isEmpty()) {
                preparedStatement = connection.prepareStatement("SELECT * FROM `activekeys` WHERE `activekey` = ?");
                preparedStatement.setString(1, key);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    rank = rs.getString(2);
                }
                if (rank.equals("User")) {
                    return "wrong key";
                }
            }

            //注册用户
            preparedStatement = connection.prepareStatement("INSERT INTO `users` (`username`, `password_md5`, `rank`) VALUES ( ? , ? , ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, rank);
            boolean reg = preparedStatement.execute();

            //删除key
            if (!key.isEmpty()) {
                preparedStatement = connection.prepareStatement("DELETE FROM activekeys WHERE activekey = ?");
                preparedStatement.setString(1, key);
                if (preparedStatement.execute()) {
                    System.out.println("WTF Failed to remove key:" + key);
                }
            }

            preparedStatement.close();
            rs.close();
            if (!reg) {
                return "Register successfully";
            } else {
                return "failed to register";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown error";
    }


    public static void execute(String sql) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet executeQuery(String sql) {
        Statement stmt = null;
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
