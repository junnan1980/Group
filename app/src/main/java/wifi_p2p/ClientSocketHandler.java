
package wifi_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

//�������ʵ��һ���ͻ��˵Ķ��̣߳��̳���Thread�ࣩ
public class ClientSocketHandler extends Thread {

    private static final String TAG = "ClientSocketHandler";
    private Handler handler;
    private ChatManager chat;
    //InetAddress��ʵ�����������������ʽ�����IP��ַ
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
        	//Socketû������
            socket.bind(null);
            //InetSocketAddress��SocketAddress��ʵ�����ࡣ����ʵ�� IP �׽��ֵ�ַ��IP ��ַ + �˿ںţ����������κ�Э�顣
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
