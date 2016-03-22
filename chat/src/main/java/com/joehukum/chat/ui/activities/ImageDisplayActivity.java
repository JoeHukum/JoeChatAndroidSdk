package com.joehukum.chat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joehukum.chat.R;
import com.joehukum.chat.ui.views.TouchImageView;

/**
 * Created by pulkitkumar on 18/03/16.
 */
public class ImageDisplayActivity extends AppCompatActivity
{

    private static final String URL = "url";

    public static Intent getIntent(Context context, String url)
    {
        Intent intent = new Intent(context, ImageDisplayActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.black);
        setContentView(R.layout.activity_image_display);
        setUpToolbar();

        final ImageView imageView = (TouchImageView) findViewById(R.id.image);
        final View actions = findViewById(R.id.actions);
        actions.setVisibility(View.GONE);

        String url = getIntent().getExtras().getString(URL);
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_image_placeholder).into(imageView);

    }

    private void setUpToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
