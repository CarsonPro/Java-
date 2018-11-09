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
 * 数据局爬虫
 * @author cwf
 *
 */
public class SnatchSHUJUJU {

	public static Document getDocument (String url){
        try {
       	 //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static List<String> getEveryOtherUrl(){
		List<String> urlList = new ArrayList<>(); 
		String host = "http://www.shujuju.cn";
		String url = "http://www.shujuju.cn/navigation/navigationPage";
		Document document = getDocument(url);
		Elements elements1 = document.select("[class=more fr]");
		Elements elements2 = elements1.select("a[href]");
		for(Element element : elements2){
			String string = host+element.attr("href");
			urlList.add(string);
		}
		return urlList;
	}
	
	public static List<Map> getDetailUrl(List<String> list){
		List <Map> mapList = new ArrayList<>();
		for(String url:list){
			Document document = getDocument(url);
			Elements elements1 = document.select("[class=nav-sort-info]");
			String channelName = elements1.get(0).select("h4").text();
			System.out.println("channelName:"+channelName);
			Elements elements2 = elements1.select("[class=nav-sort-body clearfix]").select("a");
			for(Element element : elements2){
				Map<String,String> map = new HashMap<>();
				String linkUrl = element.attr("href");
			    String name =  element.text();
			    System.out.println("linkUrl:"+linkUrl);
			    System.out.println("name:"+name);
			    map.put("channelName", channelName);
			    map.put("linkUrl", linkUrl);
			    map.put("name", name);
			    mapList.add(map);
			}
		}
		return mapList;
	}
	
	public static void main(String[] args) {
		List<Map> list = getDetailUrl(getEveryOtherUrl());
		FriendLinkDao friendLinkDao = new FriendLinkDao();
		for(Map map:list){
			String channelName = map.get("channelName").toString();
			Integer channelId = friendLinkDao.getChannelId(channelName,1);
			if(channelId != -1){
				System.out.println("channelId: " + channelId);
				map.put("channelId", channelId);
				map.put("stat", "1");
				friendLinkDao.insertFriendLink(map);
			}else {
				friendLinkDao.insertChannelName(channelName, 1);
				channelId = friendLinkDao.getChannelId(channelName,1);
				System.out.println("channelId: " + channelId);
				map.put("channelId", channelId);
				map.put("stat", "1");
				friendLinkDao.insertFriendLink(map);
			}
		}
		
	}
}
