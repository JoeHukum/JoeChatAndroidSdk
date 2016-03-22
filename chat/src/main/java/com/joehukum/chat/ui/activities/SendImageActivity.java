package com.joehukum.chat.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.joehukum.chat.R;
import com.joehukum.chat.ui.utils.UiUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class SendImageActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_GALLERY = 2;
    private static final String TAG = SendImageActivity.class.getName();
    private static final String TYPE = "type";

    private int mType;
    private String mPath;

    private Uri mImageUri;
    private Bitmap mPhoto;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.black);

        mType = getIntent().getExtras().getInt(TYPE);

        setContentView(R.layout.activity_image_display);
        setUpToolbar();
        imageView = (ImageView) findViewById(R.id.image);
        final Button cancel = (Button) findViewById(R.id.cancel);
        final Button send = (Button) findViewById(R.id.send);

        cancel.setOnClickListener(this);
        send.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            UiUtils.requestWritePermission(SendImageActivity.this, findViewById(R.id.main));
        } else
        {
            openCameraOrGallery();
        }
    }

    private void openCameraOrGallery()
    {
        if (mType == TYPE_CAMERA)
        {
            //File file = Factory.getImageService().createExternalStoragePublicPicture(this);
//            if (file != null)
//            {
//                mImageUri = Uri.fromFile(file);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageUri);
//                startActivityForResult(intent, TYPE_CAMERA);
//            } else
//            {
//                finish();
//            }
        } else if (mType == TYPE_GALLERY)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, TYPE_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            openCameraOrGallery();
        } else
        {
            finish();
        }
        return;
    }


    private void setUpToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == TYPE_CAMERA)
            {
                mPath = mImageUri.getPath();
                FileOutputStream out;
                try
                {
                    File file = new File(mPath);
                    //mPhoto = Factory.getImageService().loadFromUri(Uri.fromFile(file), 1080, 1080);
                    out = new FileOutputStream(file);
                    mPhoto.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.close();
                    Glide.with(this).load(mPath).fitCenter()
                            .placeholder(R.drawable.ic_image_placeholder).into(imageView);
                } catch (FileNotFoundException e)
                {
                    Log.wtf(TAG, e);
                } catch (IOException e)
                {
                    Log.wtf(TAG, e);
                }

            } else if (requestCode == TYPE_GALLERY)
            {
                Uri imageUri = data.getData();
                mPath = getPath(imageUri);
                Glide.with(this).load(mPath).fitCenter()
                        .placeholder(R.drawable.ic_image_placeholder).into(imageView);
            } else
            {
                finish();
            }
        } else
        {
            finish();
        }
    }

    private String getPath(Uri uri)
    {
        Cursor cursor = null;
        try
        {
            cursor = getContentResolver().query(uri, null, null, null, null);

            cursor.moveToFirst();
            String documentId = cursor.getString(0);
            if (documentId != null)
            {
                documentId = documentId.substring(documentId.lastIndexOf(':') + 1);
                cursor.close();
                cursor = getContentResolver().
                        query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                null, BaseColumns._ID + " = ? ", new String[]{documentId}, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                String path = cursor.getString(columnIndex);

                return path;
            } else
            {
                return null;
            }
        } catch (CursorIndexOutOfBoundsException e)
        {
            Toast.makeText(this, "Please select images from gallery only, Dropbox/ Drive is not supported", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
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

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.cancel)
        {
            setResult(RESULT_CANCELED);
            finish();
        } else if (v.getId() == R.id.send)
        {
            if (TextUtils.isEmpty(mPath))
            {
                Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
            //final ListenableFuture<String> future = Factory.getImageService().sendImage(mPath);
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(true);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    setResult(RESULT_CANCELED);
                    //future.cancel(false);
                    finish();
                }
            });
            pd.setMessage("Sending Image");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
//            Futures.addCallback(future, new FutureCallback<String>()
//            {
//                @Override
//                public void onSuccess(String result)
//                {
//                    try
//                    {
//                        pd.dismiss();
//                    }
//                    catch (Exception e)
//                    {
//
//                    }
//                    if (!TextUtils.isEmpty(result))
//                    {
//                        Intent intent = new Intent();
//                        intent.putExtra(Constants.URL, result);
//                        intent.putExtra(Constants.PATH, mPath);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                    else
//                    {
//                        setResult(RESULT_CANCELED);
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable t)
//                {
//                    try
//                    {
//                        pd.dismiss();
//                        Toast.makeText(SendImageActivity.this, "Could not upload image, Please try later", Toast.LENGTH_SHORT).show();
//                    }
//                    catch (Exception e)
//                    {
//
//                    }
//                }
//            }, ExecutorUtils.onUIThread());
        }
    }

    @Override
    protected void onDestroy()
    {
        if (mPhoto != null && !mPhoto.isRecycled())
        {
            mPhoto.recycle();
        }
        super.onDestroy();
    }

    public static Intent getCameraIntent(Context context)
    {
        Intent intent = new Intent(context, SendImageActivity.class);
        intent.putExtra(TYPE, TYPE_CAMERA);
        return intent;
    }

    public static Intent getGalleryIntent(Context context)
    {
        Intent intent = new Intent(context, SendImageActivity.class);
        intent.putExtra(TYPE, TYPE_GALLERY);
        return intent;
    }
}
