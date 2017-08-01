package com.brandon.itunessearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.brandon.itunessearch.Song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by brandoncole on 7/27/17.
 */

public class ItunesSongSource {

    private static ItunesSongSource mSource;
    private RequestQueue mRequestQueue;
    private Context mContext;
    private ImageLoader mImageLoader;
    private final static int IMAGE_CACHE_COUNT = 100;

    public interface ItunesResultsListener {
        void onSongResponse(List<Song> songList);
    }


    public static ItunesSongSource get(Context context) {
        if (mSource == null) {
            mSource = new ItunesSongSource(context);
        }
        return mSource;
    }

    private ItunesSongSource(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public void getItunesResults(String query, ItunesResultsListener resultsListener) {
        final ItunesResultsListener songListenerInternal = resultsListener;
        String url = "https:itunes.apple.com/search?term=" + query + "&entity=musicTrack";

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Song> songList = new ArrayList<Song>();
                            JSONArray jSongs = response.getJSONArray("results");
                            for (int i = 0; i < jSongs.length(); i++){
                                songList.add(new Song(jSongs.getJSONObject(i)));
                            }
                            songListenerInternal.onSongResponse(songList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            songListenerInternal.onSongResponse(null);
                            Toast.makeText(mContext, R.string.err_mess_json_req, Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        songListenerInternal.onSongResponse(null);
                        Toast.makeText(mContext, R.string.err_mess_json_req, Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

        public ImageLoader getImageLoader() {
            return mImageLoader;
        }
}
