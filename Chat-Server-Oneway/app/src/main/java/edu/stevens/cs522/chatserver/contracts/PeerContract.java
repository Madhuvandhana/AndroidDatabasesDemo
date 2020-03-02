package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.net.InetAddress;
import java.util.zip.Adler32;

/**
 * Created by dduggan.
 */

public class PeerContract implements BaseColumns {


    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String TIMESTAMP = "timestamp";
    public static final String ADDRESS = "address";

    private static int IDColumn = -1;
    private static int timeStampColumn = -1;
    private static int nameColumn = -1;
    private static int addressColumn = -1;


    public static int getIDColumn(Cursor cursor) {
        if (IDColumn < 0) {
            IDColumn = cursor.getColumnIndexOrThrow(ID);
        }
        return cursor.getInt(IDColumn);
    }

    public static void putIDStamp(ContentValues out, long id) {
        out.put(ID, id);
    }

    public static long getTimeStamp(Cursor cursor) {
        if (timeStampColumn < 0) {
            timeStampColumn = cursor.getColumnIndexOrThrow(TIMESTAMP);
        }
        return cursor.getLong(timeStampColumn);
    }

    public static void putTimeStamp(ContentValues out, long timeStamp) {
        out.put(TIMESTAMP, timeStamp);
    }

    public static String getName(Cursor cursor) {
        if (nameColumn < 0) {
            nameColumn = cursor.getColumnIndexOrThrow(NAME);
        }
        return cursor.getString(nameColumn);
    }

    public static void putName(ContentValues out, String name) {
        out.put(NAME, name);
    }

    public static String getAddress(Cursor cursor) {
        if (addressColumn < 0) {
            addressColumn = cursor.getColumnIndexOrThrow(ADDRESS);
        }
        return cursor.getString(addressColumn);
    }

    public static void putAddress(ContentValues out, String address) {
        out.put(ADDRESS, address);
    }

}
