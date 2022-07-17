package cn.Hanabi.irc.server.utils;

public class SQLUtils {
    public static boolean checkInject(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "select|update|and|or|delete|insert|truncate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|table";
        String[] badStrs = badStr.split("|");
        for (int i = 0; i < badStrs.length; i++) {
            //循环检测，判断在请求参数当中是否包含SQL关键字
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
}
