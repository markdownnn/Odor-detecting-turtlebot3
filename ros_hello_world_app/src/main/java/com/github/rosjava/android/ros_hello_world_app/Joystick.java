/*
 * Copyright (C) 2014 Juan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.rosjava.android.ros_hello_world_app;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */

public class Joystick extends RelativeLayout implements NodeMain {

    private Context context;
    private int linearX = 0;

    public Joystick(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void setLinearX(final int newLinearX) {
        linearX = newLinearX;
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Joystick(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.joystick, Joystick.this, false);
        addView(view);
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava/joystick");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        final Publisher<geometry_msgs.Twist> publisher =
                connectedNode.newPublisher("cmd_vel", geometry_msgs.Twist._TYPE);
        // This CancellableLoop will be canceled automatically when the node shuts down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() { }

            @Override
            protected void loop() throws InterruptedException {
                geometry_msgs.Twist twist = publisher.newMessage();
                twist.getLinear().setX(linearX);
                publisher.publish(twist);
                Thread.sleep(1000);
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
