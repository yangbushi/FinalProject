package com.example.finalproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Xml;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NewsFeedGet {
    public static final String BOOKTAG = "book";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    public static final String PRICE = "price";

    public static final String FINISH = "finished";

    private static final String strURL = "https://www.w3schools.com/xml/books.xml";
    private static String ns = null;

    private static NewsFeed bookFromXML = null;
    private static NewsFeedMain bookExamplethis = null;
    private static XmlPullParser parser;
    private static InputStream in;



    public static void setBookExample(NewsFeedMain b){
        NewsFeedGet.bookExamplethis = b;
    }

    public static void setBookFromXML(NewsFeed bookFromXML){
        NewsFeedGet.bookFromXML = bookFromXML;
    }

    public static NewsFeed getBookFromXML(){
        return NewsFeedGet.bookFromXML;
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

    public static boolean openStream(){
        try{
            in = GetHttp.downloadUrl(strURL);
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

    public static NewsFeed readBookTag()throws XmlPullParserException, IOException, InterruptedException, JSONException {
        NewsFeed NewsFeed = null;
        boolean finished = false;

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first book tag
            if (name.equals(NewsFeedGet.BOOKTAG)) {
                NewsFeed = new NewsFeed();
            }
            if (name.equals(NewsFeedGet.TITLE)) {
                parser.next();
                NewsFeed.setTitle(parser.getText());
            }
            if (name.equals(NewsFeedGet.AUTHOR)) {
                parser.next();
                NewsFeed.addAuthor(parser.getText());
            }
            if (name.equals(NewsFeedGet.YEAR)) {
                parser.next();
                NewsFeed.setYear(parser.getText());
            }
            if (name.equals(NewsFeedGet.PRICE)) {
                parser.next();
                NewsFeed.setPrice(parser.getText());
                finished = true;
            }
            if(finished)
                break;
        }
        return NewsFeed;
    }

    public static NewsFeed readBookAttr()throws XmlPullParserException, IOException, InterruptedException, JSONException {
        NewsFeed NewsFeed = null;

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first book tag
            if (name.equals(NewsFeedGet.BOOKTAG)) {
                NewsFeed = new NewsFeed();
                NewsFeed.setAuthor(parser.getAttributeValue(ns, NewsFeedGet.AUTHOR));
                NewsFeed.setTitle(parser.getAttributeValue(ns, NewsFeedGet.TITLE));
            }
        }
        return NewsFeed;
    }

    private static boolean fileExistance(String fname){
        File file = bookExamplethis.getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    private static void saveBMP(String iconName, Bitmap image) throws IOException {
        FileOutputStream outputStream = bookExamplethis.openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
