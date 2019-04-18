package com.example.finalproject;

import android.util.Xml;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;

/**
 * search the website according the user's input and set the data to array list
 */
public class NewsFeedGet {
    /**
     * define a String variable:url
     */
    public static final String url = "url";
    /**
     * define a String variable:title
     */
    public static final String TITLE = "title";
    /**
     * define a String variable:content
     */
    public static final String content = "text";
    /**
     * define a String variable:method
     */
    public static final String method = "post";
    /**
     * define a String variable:FINISH
     */
    public static final String FINISH = "finished";
    /**
     * define a String variable:keyword
     */
    private static String keyword ;
    /**
     * define a String variable:strURL
     */
    private static final String strURL = "http://webhose.io/filterWebContent?token=cf1375e3-9fc1-4002-b876-806a616c1b28&format=xml&sort=crawled&q=";
    /**
     * define a variable of NewsFeed
     */
    private static NewsFeed NewsFeedFromXML = null;
    /**
     *  define an object of NewsFeedMain
     */
    private static NewsFeedMain NewsFeedExamplethis = null;
    /**
     *  define an object of XmlPullParser
     */
    private static XmlPullParser parser;
    /**
     *  define an object of InputStream
     */
    private static InputStream in;



    public static void setNewsFeedExample(NewsFeedMain b){
        NewsFeedGet.NewsFeedExamplethis = b;
    }

    public static void setNewsFeedFromXML(NewsFeed NewsFeedFromXML){
        NewsFeedGet.NewsFeedFromXML = NewsFeedFromXML;
    }

    public static NewsFeed getNewsFeedFromXML(){
        return NewsFeedGet.NewsFeedFromXML;
    }

    public static boolean openStream(String keyword){


        try{
            NewsFeedGet.keyword = keyword;
            String strURL1 = strURL+keyword;
            in = GetHttp.downloadUrl(strURL1);
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            return true;
        } catch (IOException  e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<NewsFeed> readNewsFeedTag()throws XmlPullParserException, IOException, InterruptedException, JSONException {
        ArrayList<NewsFeed> NewsFeedList = new ArrayList<>();
        NewsFeed NewsFeed = null;
        boolean finished = false;


        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first record tag
            if (name.equals(NewsFeedGet.method)) {
                NewsFeed = new NewsFeed();
                NewsFeed.setKeyword(keyword);
                continue;
            }
            if (name.equals(NewsFeedGet.url)) {
                parser.next();
                NewsFeed.setURL(parser.getText());
                continue;
            }
            if (name.equals(NewsFeedGet.TITLE)) {
                parser.next();
                NewsFeed.setTitle(parser.getText());
                continue;
            }

            if (name.equals(NewsFeedGet.content)) {
                parser.next();
                NewsFeed.addContent(parser.getText());
                finished = true;
            }

            if(finished) {
                NewsFeedList.add(NewsFeed);
                finished = false;

            }

            }
        return NewsFeedList;
    }

  }

