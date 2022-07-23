package cn.hanabi.irc.server.database;

import cn.hanabi.irc.server.ServerMain;
import cn.hanabi.irc.utils.MD5Utils;

import java.sql.*;
import java.text.SimpleDateFormat;

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

    public static String login(String username, String password, String hwid) {
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
                        return "Your HWID has updated, please login again";
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
