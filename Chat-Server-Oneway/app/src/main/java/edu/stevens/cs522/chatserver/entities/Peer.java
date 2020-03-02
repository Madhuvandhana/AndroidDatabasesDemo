package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import edu.stevens.cs522.base.DateUtils;
import edu.stevens.cs522.base.InetAddressUtils;
import edu.stevens.cs522.chatserver.contracts.PeerContract;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable, Persistable {

    // Will be database key
    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    // Where we heard from them
    public InetAddress address;

    public Peer() {
    }

    public Peer(Cursor cursor) {
        this.id = PeerContract.getIDColumn(cursor);
        this.name = PeerContract.getName(cursor);
        this.timestamp = new Date(PeerContract.getTimeStamp(cursor));
        try {
            this.address = InetAddress.getByName(PeerContract.getAddress(cursor));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public Peer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        timestamp = DateUtils.readDate(in);
        address = InetAddressUtils.readAddress(in);
    }

    @Override
    public void writeToProvider(ContentValues out) {
        PeerContract.putIDStamp(out, (int)id);
        PeerContract.putName(out, name);
        PeerContract.putTimeStamp(out, timestamp.getTime());
        PeerContract.putAddress(out, address.getHostAddress());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(name);
        DateUtils.writeDate(out, timestamp);
        InetAddressUtils.writeAddress(out, address);
    }

    public static final Creator<Peer> CREATOR = new Creator<Peer>() {

        @Override
        public Peer createFromParcel(Parcel source) {
            return new Peer(source);
        }

        @Override
        public Peer[] newArray(int size) {
            return new Peer[size];
        }

    };
}
