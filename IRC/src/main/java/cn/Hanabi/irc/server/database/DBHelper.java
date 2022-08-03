package cn.hanabi.irc.server.database;

import cn.hanabi.irc.server.ServerMain;
import cn.hanabi.irc.utils.MD5Utils;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DBHelper {
    static Connection connection;

    public static String address;
    public static String port;
    public static String name;
    public static String userName;
    public static String pwd;

    public static void init(String address, String port, String dbName, String user, String pwd) {
        DBHelper.address = address;
        DBHelper.port = port;
        DBHelper.name = dbName;
        DBHelper.userName = user;
        DBHelper.pwd = pwd;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("jdbc:mysql://" + address + ":" + port + "/" + dbName);
            connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + dbName + "?serverTimezone=UTC&autoReconnect=true&autoReconnectForPools=true", user, pwd);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reconnect(){
        try {
            connection.close();
            connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + name + "?serverTimezone=UTC&autoReconnect=true&autoReconnectForPools=true", userName, pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String login(String username, String password, String hwid) {
        reconnect();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE `username` = ? AND `password_md5` = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getString(4).equals(hwid)) {
                    return rs.getString(3);
                } else {
                    if (System.currentTimeMillis() - rs.getInt(5) >= 3600 * 24 * 7) {//判断HWID更新时间
                        PreparedStatement st = connection.prepareStatement("UPDATE `users` SET `hwid`=?,`updatetime`=? WHERE username=?");
                        st.setString(1, hwid);
                        st.setInt(2, (int) System.currentTimeMillis());
                        st.setString(3, username);
                        st.execute();
                        return rs.getString(3);
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        return "HWID is unverified, and you can't update your HWID until " + sdf.format(new Date(rs.getInt(5)));
                    }
                }
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Failed to login, Please check your username and password";
    }

    public static String register(String username, String password, String key) {
        reconnect();
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
            String rank = "WTF";
            if (!key.isEmpty()) {
                preparedStatement = connection.prepareStatement("SELECT * FROM `activekeys` WHERE `activekey` = ?");
                preparedStatement.setString(1, key);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    rank = rs.getString(2);
                }
                if (rank.equals("WTF")) {
                    return "wrong key";
                }
            }

            //注册用户
            preparedStatement = connection.prepareStatement("INSERT INTO `users` (`username`, `password_md5`, `rank`, `hwid`, `updatetime`) VALUES ( ? , ? , ?, ?, ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, rank);
            preparedStatement.setString(4, "");
            preparedStatement.setLong(5, 0);
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


    public static void record(String username, String ip, String time) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO `ip_record` (`username`, `ip`, `time`) VALUES ( ? , ? , ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, ip);
            preparedStatement.setString(3, time);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
