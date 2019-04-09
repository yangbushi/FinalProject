package com.example.finalproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Xml;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NewsFeedGet {
 //   public static final String NewsFeedTAG = "NewsFeed";
    public static final String url = "url";
    public static final String TITLE = "title";
    public static final String content = "text";
    public static final String method = "post";
 //   public static final String PRICE = "price";

    public static final String FINISH = "finished";
    private static String keyword ;
    private static final String strURL = "http://webhose.io/filterWebContent?token=cf1375e3-9fc1-4002-b876-806a616c1b28&format=xml&sort=crawled&q=";
    private static String ns = null;

    private static NewsFeed NewsFeedFromXML = null;
    private static NewsFeedMain NewsFeedExamplethis = null;
    private static XmlPullParser parser;
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


//    public static String doInBackground(String... strings) {
//
//        try (InputStream in = HTTPUtils.downloadUrl(strURL)) {
//
//            bookFromXML = parse();
//            Log.i("",bookFromXML.toString());
//
//        } catch (IOException | XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (InterruptedException | JSONException e) {
//            e.printStackTrace();
//        }
//        return "Finished";
//    }

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

    public static InputStream getInputStream(){
        return in;
    }

    public static XmlPullParser getParser(){
        return parser;
    }


//    public static Book parse() throws XmlPullParserException, IOException, InterruptedException, JSONException {
//        try {
//            parser = Xml.newPullParser();
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            parser.setInput(in, null);
//            return readBook();
//        } finally {
//            in.close();
//        }
//    }

    public static String getTagValue(String tag){
        return null;
    }

    public static ArrayList<NewsFeed> readNewsFeedTag()throws XmlPullParserException, IOException, InterruptedException, JSONException {
        ArrayList<NewsFeed> NewsFeedList = new ArrayList<>();
        NewsFeed NewsFeed = null;
        boolean finished = false;

        int i = 1;
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first book tag
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
  /*          if (name.equals(NewsFeedGet.PRICE)) {
                parser.next();
                NewsFeed.setPrice(parser.getText());
                finished = true;
            }**/
            if(finished) {
                NewsFeedList.add(NewsFeed);
                finished = false;
                i ++;
            }

            //if(i>1)break;
        }
        return NewsFeedList;
    }

  /*  public static NewsFeed readNewsFeedAttr()throws XmlPullParserException, IOException, InterruptedException, JSONException {
        ArrayList<NewsFeed> NewsFeeds = new ArrayList<>();
        NewsFeed NewsFeed = null;

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first book tag
            if (name.equals(NewsFeedGet.method)) {
                NewsFeed = new NewsFeed();
                NewsFeed.setKeyword(keyword);
                continue;
            }
            if (name.equals(NewsFeedGet.NewsFeedTAG)) {
                NewsFeed = new NewsFeed();
                NewsFeed.setURL(parser.getAttributeValue(ns, NewsFeedGet.url));
                NewsFeed.setTitle(parser.getAttributeValue(ns, NewsFeedGet.TITLE));
            }
        }
        return NewsFeed;
    }
**/
  /*  private static boolean fileExistance(String fname){
        File file = NewsFeedExamplethis.getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    private static void saveBMP(String iconName, Bitmap image) throws IOException {
        FileOutputStream outputStream = NewsFeedExamplethis.openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        outputStream.flush();
        outputStream.close();
    }
**/
}

