
package wifi_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Handles reading and writing of messages with socket buffers. Uses a Handler
 * to post messages to UI thread for UI updates(更新UI).
 * Handle主要是读写Socket中的信息，用一个Handler对象传送信息给UI主线程去更新UI。
 */
//这是一个线程类，继承了Runnable接口
public class ChatManager implements Runnable {

    private Socket socket = null;
    private Handler handler;

    public ChatManager(Socket socket, Handler handler){
        this.socket = socket;
        this.handler = handler;
    }

    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = "ChatHandler";

    @Override
    public void run() {
        try {
        	//获取Socket对象的字节输入流(客户端向服务器发送的数据)
            iStream = socket.getInputStream();
            //获取Socket对象的字节输出流（服务器向客户端发送数据）
            oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            //向子线程传送数据
            handler.obtainMessage(WiFiServiceDiscoveryActivity.MY_HANDLE, this)
                    .sendToTarget();

            while (true) 
            {
                try
                {
                    // Read from the InputStream（读取客户端发过来的数据）
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    // Send the obtained bytes to the UI Activity（发送获得的数据给UI窗口）
                    Log.d(TAG, "Rec:" + String.valueOf(buffer));
                    Log.d(TAG,"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                    handler.obtainMessage(WiFiServiceDiscoveryActivity.MESSAGE_READ,
                            bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 
     * 服务器端向客户端发送消息
     */
    public void write(byte[] buffer) {
        try {
            oStream.write(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

}
