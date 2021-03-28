package com.github.rosjava.android.ros_hello_world_app;

import android.os.Bundle;
import android.view.View;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class RosHelloWorldApp extends RosActivity
{
    public RosHelloWorldApp() {
        this("Odor Tracking Turtlebot3", "Odor Tracking App is running...");
    }

    private Joystick JoystickNode;
    private Camera<sensor_msgs.CompressedImage> CameraNode;
    private Monitor MonitorNode;

    protected RosHelloWorldApp(String notificationTicker, String notificationTitle) {
        super(notificationTicker, notificationTitle);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        JoystickNode = (Joystick) findViewById(R.id.joystick);
        CameraNode = (Camera<sensor_msgs.CompressedImage>) findViewById(R.id.camera);
        MonitorNode = (Monitor) findViewById(R.id.monitor);
    }

    public void moveForward(View v)
    {
        JoystickNode.setLinearX(2);
    }

    public void moveBackward(View v)
    {
        JoystickNode.setLinearX(-2);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname(), getMasterUri());

        nodeConfiguration.setNodeName("joystick");
        nodeMainExecutor.execute(JoystickNode, nodeConfiguration);

        nodeConfiguration.setNodeName("camera");
        CameraNode.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        nodeMainExecutor.execute(CameraNode, nodeConfiguration);

        nodeConfiguration.setNodeName("monitor");
        nodeMainExecutor.execute(MonitorNode, nodeConfiguration);
    }
}
