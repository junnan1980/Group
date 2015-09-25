package wifi_p2p;

import group.MyDatabaseHelper;
import group.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wifi_p2p.WiFiChatFragment.MessageTarget;
import wifi_p2p.WiFiDirectServicesList.DeviceClickListener;
import wifi_p2p.WiFiDirectServicesList.WiFiDevicesAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group.R;

public class WiFiServiceDiscoveryActivity extends Activity implements
		DeviceClickListener, Handler.Callback, MessageTarget,
		ConnectionInfoListener {

	public static final String TAG = "wifidirectdemo";
	// TXT RECORD properties
	public static final String TXTRECORD_PROP_AVAILABLE = "available";
	public static final String SERVICE_INSTANCE = "_wifidemotest";
	public static final String SERVICE_REG_TYPE = "_presence._tcp";
	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	private WifiP2pManager manager;
	static final int SERVER_PORT = 4545;
	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private BroadcastReceiver receiver = null;
	private WifiP2pDnsSdServiceRequest serviceRequest;
	private Handler handler = new Handler(this);
	private WiFiChatFragment chatFragment;
	private WiFiDirectServicesList servicesList;
	private TextView statusTxtView;
	//定义一个WifiManager对象  
    private WifiManager mWifiManager; 
    public static String hostmac;       //本机MAC
    private boolean isWifiP2pEnabled = false;
    private ChatManager chatManager;
    //声明数据库
    public MyDatabaseHelper dbHelper;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
  //学生到课信息汇报老师
  	private SharedPreferences preferencesToteacher;
    private SharedPreferences.Editor editorToteacher;
    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/** Called when the activity is first created. */
	// 当程序第一次启动的时候调用这个。
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		openWifi();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//建立一个数据库，使每次学生签到情况都记录下来；
		dbHelper=new MyDatabaseHelper(WiFiServiceDiscoveryActivity.this,"class.db3",1);
		 hostmac = getLocalMacAddress();//获取本机MAC
		
		statusTxtView = (TextView) findViewById(R.id.status_text);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);
		startRegistrationAndDiscovery();

		servicesList = new WiFiDirectServicesList();
		getFragmentManager().beginTransaction()
				.add(R.id.container_root, servicesList, "services").commit();

	}
    //当程序运行时，打开wifi
	 private void openWifi() {  
	       if (!mWifiManager.isWifiEnabled()) {  
	    	   mWifiManager.setWifiEnabled(true);  
	       }    
	    } 
	 //当程序运行时，关闭wifi
	 public void closeWifi() {  
	       if (mWifiManager.isWifiEnabled()) {  
	    	   mWifiManager.setWifiEnabled(false);  
	       }    
	    }  
	//获取本机的MAC地址
		 public String getLocalMacAddress()
		    {
		        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		        WifiInfo info = wifi.getConnectionInfo();
		        return info.getMacAddress();
		    }
	
	@Override
	protected void onRestart() {
		Fragment frag = getFragmentManager().findFragmentByTag("services");
		if (frag != null) {
			getFragmentManager().beginTransaction().remove(frag).commit();
		}
		super.onRestart();
	}

	@Override
	public void onStop() {
		if (manager != null && channel != null) {
			manager.removeGroup(channel, new ActionListener() {

				@Override
				public void onFailure(int reasonCode) {
					Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
				}

				@Override
				public void onSuccess() {
					Toast.makeText(WiFiServiceDiscoveryActivity.this,"断开连接",1000).show();
				}

			});
		}
		super.onStop();
	}
   //初始化菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case R.id.atn_direct_enable:
            if (manager != null && channel != null) {

                // Since this is the system wireless settings activity, it's
                // not going to send us a result. We will be notified by
                // WiFiDeviceBroadcastReceiver instead.

                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            } else {
                Log.e(TAG, "channel or manager is null");
            }
            return true;

        case R.id.atn_direct_discover:
        	//servicesList=null;
            startRegistrationAndDiscovery();
        	servicesList = new WiFiDirectServicesList();
        	getFragmentManager().beginTransaction()
        				.add(R.id.container_root, servicesList, "services").commit();
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	/**
	 * Registers a local service and then initiates a service discovery
	 * 注册一个本地的服务，开始一个服务发现功能
	 */
	public void startRegistrationAndDiscovery() {
		Map<String, String> record = new HashMap<String, String>();
		record.put(TXTRECORD_PROP_AVAILABLE, "visible");

		WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
				SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
		manager.addLocalService(channel, service, new ActionListener() {

			@Override
			public void onSuccess() {
				appendStatus("Added Local Service");
			}

			@Override
			public void onFailure(int error) {
				appendStatus("Failed to add a service");
			}
		});

		discoverService();

	}

	private void discoverService() {

		/*
		 * Register listeners for DNS-SD services. These are callbacks invoked
		 * by the system when a service is actually discovered.
		 */

		manager.setDnsSdResponseListeners(channel,
				new DnsSdServiceResponseListener() {

					@Override
					public void onDnsSdServiceAvailable(String instanceName,
							String registrationType, WifiP2pDevice srcDevice) {

						// A service has been discovered. Is this our app?

						if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {

							// update the UI and add the item the discovered
							// device.
							WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager()
									.findFragmentByTag("services");
							if (fragment != null) {
								WiFiDevicesAdapter adapter = ((WiFiDevicesAdapter) fragment
										.getListAdapter());
								WiFiP2pService service = new WiFiP2pService();
								service.device = srcDevice;
								service.instanceName = instanceName;
								service.serviceRegistrationType = registrationType;
								adapter.add(service);
								adapter.notifyDataSetChanged();
								Log.d(TAG, "onBonjourServiceAvailable "
										+ instanceName);
							}
						}

					}
				}, new DnsSdTxtRecordListener() {

					/**
					 * A new TXT record is available. Pick up the advertised
					 * buddy name.
					 */
					@Override
					public void onDnsSdTxtRecordAvailable(
							String fullDomainName, Map<String, String> record,
							WifiP2pDevice device) {
						Log.d(TAG,
								device.deviceName + " is "
										+ record.get(TXTRECORD_PROP_AVAILABLE));
					}
				});

		// After attaching listeners, create a service request and initiate
		// discovery.
		serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		manager.addServiceRequest(channel, serviceRequest,
				new ActionListener() {

					@Override
					public void onSuccess() {
						appendStatus("Added service discovery request");
					}

					@Override
					public void onFailure(int arg0) {
						appendStatus("Failed adding service discovery request");
					}
				});
		manager.discoverServices(channel, new ActionListener() {

			@Override
			public void onSuccess() {
				appendStatus("Service discovery initiated");
			}

			@Override
			public void onFailure(int arg0) {
				appendStatus("Service discovery failed");

			}
		});
	}
	@Override
	public void connectP2p(WiFiP2pService service) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = service.device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		if (serviceRequest != null)
			manager.removeServiceRequest(channel, serviceRequest,
					new ActionListener() {
						@Override
						public void onSuccess() {
						}

						@Override
						public void onFailure(int arg0) {
						}
					});

		manager.connect(channel, config, new ActionListener() {

			@Override
			public void onSuccess() {
				appendStatus("Connecting to service");
			}

			@Override
			public void onFailure(int errorCode) {
				appendStatus("Failed connecting to service");
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_READ:
			byte[] readBuf = (byte[]) msg.obj;
			// construct a string from the valid bytes in the buffer
			String readMessage = new String(readBuf, 0, msg.arg1);
			String[] studentInfo=readMessage.split(",");
			String studentId=studentInfo[0];
			String studentName=studentInfo[1];
			String groupId=studentInfo[2];
			String data=studentInfo[3];
			String mac=studentInfo[4];
			preferences=getSharedPreferences("tag",0);
			int tag=preferences.getInt("Tag",0);
			if(tag==1)
			{
				Log.v("TAG","我要去数据库啦！！！");
				Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select * from studentAll where student_id='"+studentId+"'", null);
				int count=cursor.getCount();
				if(count==0)
				{
					dbHelper.getWritableDatabase().execSQL("insert into studentAll values(NULL,'"+studentId+"','"+studentName+"','"+groupId+"',null,null,null,'"+mac+"');");
					
				}
				else
				{
					Toast.makeText(this,studentName+"已经在点名册里！", 100).show();
				}
				preferences=getSharedPreferences("tag",0);
				editor=preferences.edit();
				editor.putInt("Tag",0);
				editor.commit();
			}
			dbHelper.getWritableDatabase().execSQL("insert into student values(NULL,'"+studentId+"','"+studentName+"','"+groupId+"','"+data+"','"+mac+"');");
			Log.d(TAG, readMessage);
			//消息传送成功后，自动刷新列表，重新连接
			onStop();
			servicesList=null;
			startRegistrationAndDiscovery();
			servicesList = new WiFiDirectServicesList();
			getFragmentManager().beginTransaction()
					.replace(R.id.container_root, servicesList, "services").commit();
			//(chatFragment).pushMessage("Buddy: " + readMessage);
			break;

		case MY_HANDLE:
			Object obj = msg.obj;
			(chatFragment).setChatManager((ChatManager) obj);
			this.chatManager=(ChatManager) obj;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;
		/*
		 * The group owner accepts connections using a server socket and then
		 * spawns a client socket for every client. This is handled by {@code
		 * GroupOwnerSocketHandler}
		 */

		if (p2pInfo.isGroupOwner) {
			Log.d(TAG, "Connected as group owner");
			//Toast.makeText(this,"Connected as group owner", 100).show();
			try {
				handler = new GroupOwnerSocketHandler(
						((MessageTarget) this).getHandler());
				handler.start();
			} catch (IOException e) {
				Log.d(TAG,
						"Failed to create a server thread - " + e.getMessage());
				return;
			}
		} else {
			Toast.makeText(this,"isGroupOwner", 100).show();
			Log.d(TAG, "Connected as peer");
			handler = new ClientSocketHandler(
					((MessageTarget) this).getHandler(),
					p2pInfo.groupOwnerAddress);
			handler.start();
			chatManager=((ClientSocketHandler) handler).getChat();
		}
		chatFragment = new WiFiChatFragment();
		if(chatManager==null)
		{
			Toast.makeText(this,"连接成功！", 100).show();
		}
		preferencesToteacher=getSharedPreferences("teacher",0);
		int tagReport=preferencesToteacher.getInt("reportInfo", 0);
		if(tagReport==1)
		{
			editorToteacher=preferencesToteacher.edit();
			   editorToteacher.putInt("reportInfo",0);
			   editorToteacher.commit();
		  getFragmentManager().beginTransaction()
				 .replace(R.id.container_root, chatFragment).commit();
		}
		statusTxtView.setVisibility(View.GONE);
	}
	public void appendStatus(String status) {
		String current = statusTxtView.getText().toString();
		statusTxtView.setText(current + "\n" + status);
	}
	
}
