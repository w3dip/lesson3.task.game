package ru.sberbank.lesson3.task.game;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Random;

import static ru.sberbank.lesson3.task.game.Bucket.of;

public class MainActivity extends Activity {

    private static final String GAME_IS_STARTED = "isStarted";
    private static final String BUCKETS_STATE = "buckets";
    private static final String BALL_STATE = "ballPosition";

    private int ballPosition;
    private boolean isStarted;

    private HashMap<Integer, Bucket> buckets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            isStarted = savedInstanceState.getBoolean(GAME_IS_STARTED);
            ballPosition = savedInstanceState.getInt(BALL_STATE);
            buckets = (HashMap<Integer, Bucket>) savedInstanceState.getSerializable(BUCKETS_STATE);
        } else {
            buckets = Maps.newHashMap();
            buckets.put(1, of(R.id.bucket1));
            buckets.put(2, of(R.id.bucket2));
            buckets.put(3, of(R.id.bucket3));
        }

        final Button button = findViewById(R.id.newGame);
        button.setVisibility(isStarted ? View.INVISIBLE : View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ballPosition = new Random().nextInt(3) + 1;
                isStarted = true;
                button.setVisibility(View.INVISIBLE);
                for(Bucket bucket : buckets.values()) {
                    ImageView bucketImageView = getBucket(bucket.getBucketId());
                    bucketImageView.setImageDrawable(getResources().getDrawable(R.mipmap.bucket));
                    bucket.setVisible(true);
                    bucketImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        for(Bucket bucket : buckets.values()) {
            ImageView bucketImageView = getBucket(bucket.getBucketId());
            bucketImageView.setVisibility(bucket.isVisible() ? View.VISIBLE : View.INVISIBLE);
            bucketImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = getResources();
                    Bucket correctBucket = buckets.get(ballPosition);
                    if (correctBucket != null) {
                        ImageView correctBucketImageView = getBucket(correctBucket.getBucketId());
                        if (correctBucketImageView != null) {
                            correctBucketImageView.setImageDrawable(res.getDrawable(R.mipmap.ball));
                        }
                    }
                    String result = isStarted ? Integer.parseInt(v.getTag().toString()) == ballPosition
                            ? res.getString(R.string.win)
                            : res.getString(R.string.looose)
                            : res.getString(R.string.isOver);
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    isStarted = false;
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(GAME_IS_STARTED, isStarted);
        outState.putSerializable(BUCKETS_STATE, buckets);
        outState.putSerializable(BALL_STATE, ballPosition);
        super.onSaveInstanceState(outState);
    }

    private ImageView getBucket(int id) {
        return (ImageView)findViewById(id);
    }
}
