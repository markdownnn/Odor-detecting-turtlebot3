package com.github.rosjava.android.ros_hello_world_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

@SuppressLint("AppCompatCustomView")
public class Camera<T> extends ImageView implements NodeMain {

    private MessageCallable<Bitmap, T> callable;

    public Camera(Context context) {
        super(context);
    }

    public Camera(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Camera(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Camera(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMessageToBitmapCallable(MessageCallable<Bitmap, T> callable) {
        this.callable = callable;
    }


    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("camera");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<T> subscriber = connectedNode.newSubscriber("image_publisher_1616176014131791132/image_raw/compressed", sensor_msgs.CompressedImage._TYPE);
        subscriber.addMessageListener(new MessageListener<T>() {

            @Override
            public void onNewMessage(final T message) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setImageBitmap(callable.call(message));
                    }
                });
                postInvalidate();
            }
        });
    }

    @Override
    public void onShutdown(Node node) {

    }

    @Override
    public void onShutdownComplete(Node node) {

    }

    @Override
    public void onError(Node node, Throwable throwable) {

    }
}
