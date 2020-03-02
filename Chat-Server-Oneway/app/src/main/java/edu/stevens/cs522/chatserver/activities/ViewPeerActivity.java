package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.databases.ChatDbAdapter;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_ID_KEY = "peer-id";

    private ChatDbAdapter chatDbAdapter;
    private SimpleCursorAdapter peerAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);
        setTitle("Madhu Vandhana Vijay Kumar");

        long peerId = getIntent().getLongExtra(PEER_ID_KEY, -1);
        if (peerId < 0) {
            throw new IllegalArgumentException("Expected peer id as intent extra");
        }

        // TODO init the UI
        TextView username = findViewById(R.id.view_user_name);
        TextView time = findViewById(R.id.view_timestamp);
        TextView addr = findViewById(R.id.view_address);
        listView = findViewById(R.id.list_view);
        chatDbAdapter = new ChatDbAdapter(this);
        chatDbAdapter.open();
        Peer peer = chatDbAdapter.fetchPeer(peerId);
        username.setText(peer.name);
        int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_12HOUR |DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                | DateUtils.FORMAT_SHOW_WEEKDAY;
        time.setText(DateUtils.formatDateTime(this,peer.timestamp.getTime(), flags));
        addr.setText(peer.address.getHostAddress());
        initListview(peer);
        chatDbAdapter.close();

    }

    private void initListview(Peer peer){
        Cursor cursor = chatDbAdapter.fetchMessagesFromPeer(peer);
        peerAdapter = new SimpleCursorAdapter(this, R.layout.message, cursor,  new String[] {"message_text"},
                new int[] { R.id.text });
        listView.setAdapter(peerAdapter);
    }

}
