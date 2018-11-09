package com.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dao.FriendLinkDao;

/**
 * 大数据导航爬虫
 * @author cwf
 *
 */
public class Snatchit199 {

	public static Document getDocument (String url){
        try {
       	 //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static List<Map> getEveryOtherUrl(){
		List<Map> urlList = new ArrayList<>(); 
		String host = "http://hao.199it.com";
		String url = "http://hao.199it.com/";
		Document document = getDocument(url);
		Elements elements1 = document.select(".box-sort_title");
		Elements elements2 = elements1.select("a[href]");
		for(Element element : elements2){
			Map map = new HashMap<>();
			String otherUrl = host+element.attr("href");
			String channelName = element.text();
			map.put("otherUrl",otherUrl);
			map.put("channelName",channelName);
			urlList.add(map);
		}
		return urlList;
	}
	
	public static List<Map> getDetailUrl(List<Map> list){
		List <Map> mapList = new ArrayList<>();
		FriendLinkDao friendDao = new FriendLinkDao();
		for(Map map:list){
			String channelName = map.get("channelName").toString();
			String otherUrl = map.get("otherUrl").toString();
			Document document = getDocument(otherUrl);
			Elements elements1 = document.select(".text-con");
			Elements elements2 = elements1.select("a[href]");
			for(Element element : elements2){
				Map map1 = new HashMap<>();
				String linkUrl = element.attr("href");
				String name =  element.text();
				map1.put("linkUrl",linkUrl);
				map1.put("channelName",channelName);
				map1.put("name", name);
				mapList.add(map1);
			}
		}
		return mapList;
	}
	
	public static void main(String[] args) {
		List<Map> list = getDetailUrl(getEveryOtherUrl());
		System.out.println(list);
		FriendLinkDao friendLinkDao = new FriendLinkDao();
		for(Map map:list){
			String channelName = map.get("channelName").toString();
			Integer channelId = friendLinkDao.getChannelId(channelName,2);
			if(channelId != -1){
				System.out.println("channelId: " + channelId);
				map.put("channelId", channelId);
				map.put("stat", "1");
				friendLinkDao.insertFriendLink(map);
			}else {
				friendLinkDao.insertChannelName(channelName, 2);
				channelId = friendLinkDao.getChannelId(channelName,2);
				System.out.println("channelId: " + channelId);
				map.put("channelId", channelId);
				map.put("stat", "1");
				friendLinkDao.insertFriendLink(map);
			}
		}
	}
}
