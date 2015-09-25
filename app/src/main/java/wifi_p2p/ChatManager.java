
package wifi_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Handles reading and writing of messages with socket buffers. Uses a Handler
 * to post messages to UI thread for UI updates(����UI).
 * Handle��Ҫ�Ƕ�дSocket�е���Ϣ����һ��Handler��������Ϣ��UI���߳�ȥ����UI��
 */
//����һ���߳��࣬�̳���Runnable�ӿ�
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
        	//��ȡSocket������ֽ�������(�ͻ�������������͵�����)
            iStream = socket.getInputStream();
            //��ȡSocket������ֽ����������������ͻ��˷������ݣ�
            oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            //�����̴߳�������
            handler.obtainMessage(WiFiServiceDiscoveryActivity.MY_HANDLE, this)
                    .sendToTarget();

            while (true) 
            {
                try
                {
                    // Read from the InputStream����ȡ�ͻ��˷����������ݣ�
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    // Send the obtained bytes to the UI Activity�����ͻ�õ����ݸ�UI���ڣ�
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
     * ����������ͻ��˷�����Ϣ
     */
    public void write(byte[] buffer) {
        try {
            oStream.write(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

}
