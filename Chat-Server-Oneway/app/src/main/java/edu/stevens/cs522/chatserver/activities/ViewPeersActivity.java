package edu.stevens.cs522.chatserver.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.databases.ChatDbAdapter;
import edu.stevens.cs522.chatserver.entities.Peer;


public class ViewPeersActivity extends Activity implements AdapterView.OnItemClickListener {

    /*
     * TODO See ChatServer for example of what to do, query peers database instead of messages database.
     */

    private ChatDbAdapter chatDbAdapter;

    private SimpleCursorAdapter peerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);
        setTitle("Madhu Vandhana Vijay Kumar");

        // TODO initialize peerAdapter with result of DB query
        ListView lv = findViewById(R.id.peer_list);
        chatDbAdapter = new ChatDbAdapter(this);
        chatDbAdapter.open();
        Cursor cursor = chatDbAdapter.fetchAllPeers();
        peerAdapter = new SimpleCursorAdapter(this, R.layout.message, cursor,  new String[] { "name"},
                new int[] { R.id.text });
        lv.setAdapter(peerAdapter);
        lv.setOnItemClickListener(this);
        chatDbAdapter.close();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Clicking on a peer brings up details
         */
        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_ID_KEY, id);
        startActivity(intent);
    }
}
