package com.appzone.dukkan.notifications;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_chat.ChatActivity;
import com.appzone.dukkan.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.dukkan.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;
import com.appzone.dukkan.models.ChatRoom_UserIdModel;
import com.appzone.dukkan.models.MessageModel;
import com.appzone.dukkan.models.PageModel;
import com.appzone.dukkan.models.TypingModel;
import com.appzone.dukkan.models.UserChatModel;
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.preferences.Preferences;
import com.appzone.dukkan.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key :", key);
            Log.e("value :", map.get(key)+"_");
        }

        ManageNotification(map);


    }

    private void ManageNotification(final Map<String, String> map) {

        String session = getSession();
        Log.e("session",session);
        if (session.equals(Tags.session_login)) {
            String notification_type = map.get("type");
            Log.e("type2",notification_type+"_");

                //////////////////////////////chat
                if (notification_type.equals(String.valueOf(Tags.NEW_MESSAGE_NOTIFICATION))) {

                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
                    Log.e("clas_name",className);
                    if (className.equals("com.appzone.dukkan.activities_fragments.activity_chat.ChatActivity")) {
                        Log.e("7878","7878");
                        ChatRoom_UserIdModel model = getChatRoomData();
                        if (model != null) {
                            int room_id = model.getRoomId();

                            if (room_id != Integer.parseInt(map.get("room_id"))) {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        createNotification(map);

                                    }
                                },100);
                                //create notification
                            }else
                                {

                                    int msg_id = Integer.parseInt(map.get("msg_id"));
                                    int room_id2 = Integer.parseInt(map.get("room_id"));
                                    int sender_id = Integer.parseInt(map.get("sender_id"));
                                    int receiver_id = Integer.parseInt(map.get("receiver_id"));
                                    int msg_type = Integer.parseInt(map.get("msg_type"));
                                    String msg = map.get("msg");
                                    long msg_time = Long.parseLong(map.get("receiver_id"))*1000;


                                    MessageModel messageModel = new MessageModel(room_id2,sender_id,receiver_id,msg,msg_type,msg_time);
                                    messageModel.setId(msg_id);

                                    EventBus.getDefault().post(messageModel);

                                }
                        } else {
                            createNotification(map);

                        }
                    } else {
                        Log.e("8899","8899");

                        createNotification(map);

                        //create notification
                    }
                } else {
                    createNotification(map);


                }


        }
    }


    private void createNotification(final Map<String, String> map) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            createNotificationProfessional(map);
                        } else {

                            createNotificationNative(map);
                        }




    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationProfessional(final Map<String, String> map) {
        ///////////////////////////////////

        Log.e("ddd","ddd");

        String notification_type = map.get("type");
        Log.e("type",notification_type+"_");
        String current_user_id = getUserData().getUser().getRole();

        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String CHANNEL_ID = "channel_id_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );

        channel.setShowBadge(true);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(Uri.parse(sound_path));
        builder.setChannelId(CHANNEL_ID);
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /////////////////////////////////////// chat ////////////////////////////////
        if (notification_type.equals(String.valueOf(Tags.NEW_MESSAGE_NOTIFICATION))) {

            Log.e("new_msg","newmsg");

            int msg_id = Integer.parseInt(map.get("msg_id"));
            int room_id = Integer.parseInt(map.get("room_id"));
            int sender_id = Integer.parseInt(map.get("sender_id"));
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            int msg_type = Integer.parseInt(map.get("msg_type"));
            String user_name = map.get("user_name");
            String user_phone = map.get("user_phone");
            double rate = Double.parseDouble(map.get("user_rate"));
            String user_avatar = map.get("user_avatar");
            String msg = map.get("msg");
            long msg_time = Long.parseLong(map.get("receiver_id"))*1000;

            builder.setContentTitle(user_name);
            builder.setContentText(msg);

            final MessageModel messageModel = new MessageModel(room_id,sender_id,receiver_id,msg,msg_type,msg_time);
            messageModel.setId(msg_id);




            if (current_user_id.equals(Tags.user_client)) {


                UserChatModel userChatModel = new UserChatModel(sender_id,room_id,user_name,user_phone,user_avatar,Tags.user_delegate,rate);

                Intent intent = new Intent(this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("user_chat_data",userChatModel);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        builder.setLargeIcon(bitmap);
                        if (manager != null) {
                            manager.createNotificationChannel(channel);
                            manager.notify(1, builder.build());

                            EventBus.getDefault().post(messageModel);
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                if (!map.get("user_avatar").equals("0"))
                {
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).fit().into(target);

                                }
                            },1);

                }
            }else
            {

                UserChatModel userChatModel = new UserChatModel(sender_id,room_id,user_name,user_phone,"",Tags.user_client,0);

                Intent intent = new Intent(this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("",userChatModel);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        builder.setLargeIcon(bitmap);
                        if (manager != null) {
                            manager.createNotificationChannel(channel);
                            manager.notify(1, builder.build());

                            EventBus.getDefault().post(messageModel);

                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(FireBaseMessaging.this).load(R.mipmap.ic_launcher).fit().into(target);

                            }
                        },1);
            }
        }




        /////////////////////////////////////////////client_send_new_order/////////////////////////
        else if (notification_type.equals(String.valueOf(Tags.NEW_ORDER_NOTIFICATION))) {

            builder.setContentTitle(map.get("client_name"));
            builder.setContentText(getString(R.string.new_order));

            Intent intent = new Intent(this, DelegateHomeActivity.class);
            intent.putExtra("status",1);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(FireBaseMessaging.this).load(R.mipmap.ic_launcher).fit().into(target);

                        }
                    },1);


            ///////////////////delegate_accepted_order///////////////////////////
        } else if (notification_type.equals(String.valueOf(Tags.ACCEPTED_ORDER_NOTIFICATION))) {

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.delegate_accept_order));
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status",2);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());

                        EventBus.getDefault().post(new PageModel(0));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (!map.get("user_avatar").equals("0"))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).fit().into(target);

                    }
                },1);

            }

        }




        /////////////////////////////////////collecting order///////////////////////
        else if (notification_type.equals(String.valueOf(Tags.COLLECTING_ORDER_NOTIFICATION))) {

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.collecting_order));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status",2);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (!map.get("user_avatar").equals("0"))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).fit().into(target);

                    }
                },1);

            }
        }

        ////////////////////////order_collected/////////////////////

        else if (notification_type.equals(String.valueOf(Tags.COLLECTED_ORDER_NOTIFICATION))) {

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.order_collected));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status",2);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (!map.get("user_avatar").equals("0"))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).fit().into(target);

                    }
                },1);

            }
        }


        ///////////////////////////////////////delivering/////////////////////
        else if (notification_type.equals(String.valueOf(Tags.DELIVERING_ORDER_NOTIFICATION))) {

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.delivering_order));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status",2);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (!map.get("user_avatar").equals("0"))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).fit().into(target);

                    }
                },1);

            }
        }

        ///////////////////////////////////////delivered order/////////////////////
        else if (notification_type.equals(String.valueOf(Tags.DELIVERED_ORDER_NOTIFICATION))) {

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.order_delivered));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status",3);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(2));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (!map.get("user_avatar").equals("0"))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).fit().into(target);

                    }
                },1);

            }
        }

        ///////////////////////////////////typing///////////////////////////////////////
        else if (notification_type.equals(String.valueOf(Tags.TYPING_MESSAGE_NOTIFICATION))) {

            int room_id = Integer.parseInt(map.get("room_id"));
            int user_id = Integer.parseInt(map.get("user_id"));
            int status  = Integer.parseInt(map.get("status"));

            if (room_id == getChatRoomData().getRoomId())
            {
                TypingModel typingModel = new TypingModel(room_id,user_id,status);
                EventBus.getDefault().post(typingModel);
            }
        }


    }

    private void createNotificationNative(Map<String, String> map) {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        final String current_user_id = getUserData().getUser().getRole();
        final String notification_type = map.get("type");

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(Uri.parse(sound_path));


        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /////////////////////////////////////// chat ////////////////////////////////

        if (manager != null) {
            manager.notify(1, builder.build());
        }
    }

    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }

    private ChatRoom_UserIdModel getChatRoomData() {
        return preferences.getChatUserData(this);
    }

}
