package edu.stevens.cs522.chatserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ChatDbAdapter {

    private static final String DATABASE_NAME = "messages.db";

    private static final String MESSAGE_TABLE = "Messages";

    private static final String PEER_TABLE = "Peers";

    private static final String _ID = "_id";

    private static final String NAME = "name";

    private static final String TIMESTAMP = "timestamp";

    private static final String ADDRESS = "address";

    private static final String MESSAGE_TEXT = "message_text";

    private static final String SENDER = "sender";


    private static final String PEER_FK = "peer_fk";

    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;


    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String PEER_DATABASE_CREATE =
                "CREATE TABLE "+ PEER_TABLE +"(\n" +
                        _ID +" INTEGER PRIMARY KEY,\n" +
                        NAME +" TEXT NOT NULL,\n" +
                        TIMESTAMP +" LONG NOT NULL,\n" +
                        ADDRESS +" TEXT NOT NULL\n" +
                        ");\n" ;
        private static final String MESSAGE_DATABASE_CREATE =  "CREATE TABLE "+ MESSAGE_TABLE +"(\n" +
                _ID +" INTEGER PRIMARY KEY,\n" +
                MESSAGE_TEXT +" TEXT NOT NULL,\n" +
                TIMESTAMP +" LONG NOT NULL,\n" +
                SENDER +" TEXT NOT NULL,\n" +
                PEER_FK +" INTEGER NOT NULL,\n" +
                "FOREIGN KEY (peer_fk) REFERENCES Peers(_id) ON DELETE CASCADE\n" +
                ");\n" ;


        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PEER_DATABASE_CREATE);
            db.execSQL(MESSAGE_DATABASE_CREATE);
            db.execSQL("CREATE INDEX MessagesPeerIndex ON Messages(peer_fk);");
            db.execSQL("CREATE INDEX PeerNameIndex ON Peers(name);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("TaskDBAdapter","Upgrading from version " +  oldVersion    + " to " + newVersion);
            // Upgrade: drop the old table and create a new one.
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
            // Create a new one.
            onCreate(db);
        }
    }


    public ChatDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {

        db = dbHelper.getWritableDatabase();
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public Cursor fetchAllMessages() {
        return db.query(MESSAGE_TABLE, new String[] {_ID, MESSAGE_TEXT, TIMESTAMP, SENDER, PEER_FK },null, null, null, null, null);
    }

    public Cursor fetchAllPeers() {
        return db.query(PEER_TABLE, new String[] {_ID, NAME, TIMESTAMP, ADDRESS },null, null, null, null, null);
    }

    public Peer fetchPeer(long peerId) {
        Peer peer = null;
        Cursor cursor = db.query(PEER_TABLE, new String[]{_ID, NAME, TIMESTAMP, ADDRESS}, _ID+ "=?",new String[]{String.valueOf(peerId)},null, null, null, null);
        if (cursor.moveToNext()){
            peer = new Peer(cursor);
        }
        return peer;
    }

    public Cursor fetchMessagesFromPeer(Peer peer) {
        String where = PEER_FK + " =?";
        return db.query(MESSAGE_TABLE,  new String[]{_ID, MESSAGE_TEXT, TIMESTAMP, SENDER, PEER_FK}, where,new String[]{String.valueOf(peer.id)}, null, null, null, null);
    }

    public long persist(Message message) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_TEXT, message.messageText);
        contentValues.put(TIMESTAMP, message.timestamp.getTime());
        contentValues.put(SENDER, message.sender);
        Cursor cursor = db.query(PEER_TABLE, new String[]{_ID}, NAME+ "=?", new String[] {message.sender}, null, null, null, null);
        if (cursor.moveToNext()) {
            contentValues.put(PEER_FK, cursor.getInt(0));

        }
        cursor.close();
        return db.insert(MESSAGE_TABLE, null, contentValues);
        //throw new IllegalStateException("Unimplemented: persist message");
    }

    /**
     * Add a peer record if it does not already exist; update information if it is already defined.
     */
    public long persist(Peer peer) throws SQLException {
        String where =  NAME + " =?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, peer.name);
        contentValues.put(TIMESTAMP, peer.timestamp.getTime());
        contentValues.put(ADDRESS, peer.address.getHostAddress());
        String Query = "Select * from " + PEER_TABLE +" where "+ where;
        Cursor cursor = db.rawQuery(Query,  new String[] {peer.name});
        if(cursor.getCount() <= 0) {
            cursor.close();
           return db.insertWithOnConflict(PEER_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
        else{
            cursor.close();
            return db.update(PEER_TABLE, contentValues, where,  new String[] {peer.name});
        }
        //throw new IllegalStateException("Unimplemented: persist peer");
    }

    public void close() {
        db.close();
    }
}