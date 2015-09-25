
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
//这个类也是实现了一个多线程的类，这个类是实现服务器的类。
public class GroupOwnerSocketHandler extends Thread {

    ServerSocket socket = null;
    private final int THREAD_COUNT = 10;
    private Handler handler;
    private static final String TAG = "GroupOwnerSocketHandler";

    
    //这个类的构造函数
    public GroupOwnerSocketHandler(Handler handler) throws IOException {
        try 
        {
        	//创建服务器，并开放4545端口。
            socket = new ServerSocket(4545);
            this.handler = handler;
            Log.d("GroupOwnerSocketHandler", "Socket Started");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            //可以关闭 ThreadPoolExecutor，这将导致其拒绝新任务
            pool.shutdownNow();
            throw e;
        }

    }

    /**
     * A ThreadPool for client sockets.
     *客户端的线程池(也就是说所有的客户端的信息都放在这个线程池中)
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
            	 * 当有一个新的连接的时候，初始化ChatManager类。这是一个块状操作。
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
