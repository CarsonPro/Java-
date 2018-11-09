package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectUtil {
    private static Connection conn;

    public static Connection getConn() {
        
        try {
            //1.加载mysql连接到数据库jar包，数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2.数据库所在位置以及要访问数据库的名字
            String url = "jdbc:mysql://10.1.1.186:3600/iimediareport?characterEncoding=UTF-8";
            //3.数据库的用户名，密码
            String username = "root";
            String password = "iimedia";
            //4.使用驱动管理器连接到数据库
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    public void setConn(Connection conn1) {
    	conn = conn1;
    }
	
}
