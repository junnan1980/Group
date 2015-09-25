
package wifi_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The implementation of a ServerSocket handler. This is used by the wifi p2p
 * group owner.
 */
//�����Ҳ��ʵ����һ�����̵߳��࣬�������ʵ�ַ��������ࡣ
public class GroupOwnerSocketHandler extends Thread {

    ServerSocket socket = null;
    private final int THREAD_COUNT = 10;
    private Handler handler;
    private static final String TAG = "GroupOwnerSocketHandler";

    
    //�����Ĺ��캯��
    public GroupOwnerSocketHandler(Handler handler) throws IOException {
        try 
        {
        	//������������������4545�˿ڡ�
            socket = new ServerSocket(4545);
            this.handler = handler;
            Log.d("GroupOwnerSocketHandler", "Socket Started");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            //���Թر� ThreadPoolExecutor���⽫������ܾ�������
            pool.shutdownNow();
            throw e;
        }

    }

    /**
     * A ThreadPool for client sockets.
     *�ͻ��˵��̳߳�(Ҳ����˵���еĿͻ��˵���Ϣ����������̳߳���)
     */
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void run() {
        while (true) {
            try {
                // A blocking operation. Initiate a ChatManager instance when
                // there is a new connection
            	/*
            	 * ����һ���µ����ӵ�ʱ�򣬳�ʼ��ChatManager�ࡣ����һ����״������
            	 */
                pool.execute(new ChatManager(socket.accept(), handler));
                Log.d(TAG, "Launching the I/O handler");

            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                pool.shutdownNow();
                break;
            }
        }
    }

}
