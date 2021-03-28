package com.github.rosjava.android.ros_hello_world_app;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import std_msgs.UInt16;

public class Monitor extends RelativeLayout implements NodeMain {

    private Context context;

    String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflaterService);
    View view = layoutInflater.inflate(R.layout.monitor, Monitor.this, false);
    TextView ppm = view.findViewById(R.id.display_ppm);

    public Monitor(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Monitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public Monitor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Monitor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        addView(view);
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava/monitor");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<UInt16> subscriber = connectedNode.newSubscriber("ppm", std_msgs.UInt16._TYPE);
        subscriber.addMessageListener(new MessageListener<UInt16>() {
                @Override
                public void onNewMessage(final UInt16 message) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            ppm.setText(String.valueOf(message.getData()));
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
