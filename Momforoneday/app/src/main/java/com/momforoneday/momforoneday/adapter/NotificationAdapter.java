package com.momforoneday.momforoneday.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.momforoneday.momforoneday.R;
import com.momforoneday.momforoneday.holder.NotificationHolder;
import com.momforoneday.momforoneday.model.Notification;
import com.momforoneday.momforoneday.service.ExifUtil;
import com.momforoneday.momforoneday.service.ImageProvider;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gabrielguimo on 22/03/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    private List<Notification> notificationList;
    private Context context;
    private AppCompatActivity activity;
    private View view;
    private LayoutInflater inflater;
    public NotificationAdapter(List<Notification> notificationList, Context context, AppCompatActivity activity) {
        this.notificationList = notificationList;
        this.activity = activity;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        NotificationHolder holder = new NotificationHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationHolder viewHolder, int position) {



        final Notification currentNotification = notificationList.get(position);

        String text = "";
        String status = currentNotification.getText();

        if (status.equals("comendo")) {
            text = getEmojiByUnicode(0x1F37C) + "  Mamãe, estou comendo! Nham nham";
        } else if (status.equals("chorando")) {
            text = getEmojiByUnicode(0x1F62D) + "  Mamãeeee, estou chorando! Buáááá";
        } else if (status.equals("brincando")) {
            text = getEmojiByUnicode(0x1F61D) + "  Olha mamãe, estou brincando! Hihi";
        } else if (status.equals("remedio")) {
            text = getEmojiByUnicode(0x1F48A) + "  Mamãe, estou tomando o remédio! Argh";
        } else if (status.equals("dormindo")) {
            text = getEmojiByUnicode(0x1F634) + "  Mamãe, estou indo dormir! Zzzzz";
        }

        viewHolder.notificationText.setText(text);
        viewHolder.notificationDate.setText(currentNotification.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                LayoutInflater inflater = activity.getLayoutInflater();
                final View view = inflater.inflate(R.layout.image_dialog, null);
                ImageView photoImage = (ImageView) view.findViewById(R.id.photo_baby);

                Glide.with(context).load(currentNotification.getImage()).into(photoImage);
                builder.setView(view);
                builder.show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setMessage(currentNotification.getText());
//                builder.show();
            }
        });

    }

    private String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    public int getItemCount() {
        return this.notificationList.size();
    }

    private Uri convertImageToUri(String base64Image){
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap inImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }
}


