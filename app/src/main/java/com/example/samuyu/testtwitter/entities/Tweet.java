package com.example.samuyu.testtwitter.entities;

import android.content.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

/**
 * Created by toyamaosamuyu on 2014/11/08.
 */
public class Tweet implements Parcelable{

    private static final String TAG = Tweet.class.getSimpleName();

    public long tweetId;
    public String nameId;
    public String nickname;
    public String text;
    public String profileImageUrl;
    public Date created;
    public List<String> imageThumbnailUrlList = new ArrayList<String>();

    public Tweet(long tweetId, String nameId, String nickname,
                 String text, String  profileImageUrl, Date created) {

            new Tweet(tweetId, nameId, nickname, text, profileImageUrl, created, new ArrayList<String>());
    }

    public Tweet(long tweetId, String nameId, String nickname,
                 String text, String  profileImageUrl, Date created, List<String> imageUrls) {

        this.tweetId = tweetId;
        this.nameId = nameId;
        this.nickname = nickname;
        this.text = text;
        this.profileImageUrl = profileImageUrl;
        this.created = created;
        this.imageThumbnailUrlList = imageUrls;
    }


    public static Tweet fromStatus(Status status) {

        User user = status.getUser();
        MediaEntity[] entities = status.getMediaEntities();
        List<String> imageUrls = new ArrayList<String>();

        for (MediaEntity entity : entities) {
            imageUrls.add(entity.getMediaURL());
        }

        return new Tweet(
                status.getId(),
                user.getScreenName(),
                user.getName(),
                status.getText(),
                user.getProfileImageURL(),
                status.getCreatedAt(),
                imageUrls
        );

    }


    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    private Tweet(Parcel in) {
        tweetId = in.readLong();
        nameId = in.readString();
        nickname = in.readString();
        text = in.readString();
        profileImageUrl = in.readString();
        created = new Date(in.readLong());
        in.readList(imageThumbnailUrlList, Tweet.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tweetId);
        dest.writeString(nameId);
        dest.writeString(nickname);
        dest.writeString(text);
        dest.writeString(profileImageUrl);
        dest.writeLong(created.getTime());
        dest.writeList(imageThumbnailUrlList);
    }
}
