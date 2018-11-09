package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.util.ConnectUtil;

public class FriendLinkDao {

	public Connection conn = ConnectUtil.getConn();

	public Integer getChannelId(String channelName,Integer pid) {
		Integer id = -1;
		try {
			String sql = "SELECT id FROM t_zsff_friend_link_channel WHERE channel_name = ? and pid = ?";
			PreparedStatement ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, channelName);
			ptmt.setInt(2, pid);
			ResultSet rs = ptmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return id; // 返回-1，数据库插入异常
		}
	}
	
	
	

	public void insertChannelName(String channelName,Integer pid) {

		String sql = "INSERT INTO t_zsff_friend_link_channel (channel_name, pid) VALUES (?, ?)";
		try {
			PreparedStatement ptmt = conn.prepareStatement(sql);
			ptmt.setObject(1, channelName);
			ptmt.setObject(2, pid);
			ptmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}

	}
	
	
	public void insertFriendLink(Map map) {

		String sql = "INSERT INTO t_zsff_friend_link (name, channel_id, link_url, stat) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement ptmt = conn.prepareStatement(sql);
			ptmt.setObject(1, map.get("name"));
			ptmt.setObject(2, map.get("channelId"));
			ptmt.setObject(3, map.get("linkUrl"));
			ptmt.setObject(4, map.get("stat"));
			ptmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}

	}


}
