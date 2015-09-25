
package wifi_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

//这个类是实现一个客户端的多线程（继承了Thread类）
public class ClientSocketHandler extends Thread {

    private static final String TAG = "ClientSocketHandler";
    private Handler handler;
    private ChatManager chat;
    //InetAddress的实例对象包含以数字形式保存的IP地址
    private InetAddress mAddress;

    public ClientSocketHandler(Handler handler, InetAddress groupOwnerAddress) {
        this.handler = handler;
        this.mAddress = groupOwnerAddress;
    }

    @Override
    public void run()
    {
        Socket socket = new Socket();
        try 
        {
        	//Socket没有连接
            socket.bind(null);
            //InetSocketAddress是SocketAddress的实现子类。此类实现 IP 套接字地址（IP 地址 + 端口号），不依赖任何协议。
            socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
                    WiFiServiceDiscoveryActivity.SERVER_PORT), 5000);
            Log.d(TAG, "Launching the I/O handler");
            chat = new ChatManager(socket, handler);
            new Thread(chat).start();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
            try
            {
                socket.close();
            } 
            catch (IOException e1) 
            {
                e1.printStackTrace();
            }
            return;
        }
    }

    public ChatManager getChat() {
        return chat;
    }

}
