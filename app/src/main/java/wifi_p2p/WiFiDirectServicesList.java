package wifi_p2p;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.group.R;

/**
 * A simple ListFragment that shows the available services as published by the
 * peers 一个列表框，显示可连接的热点。
 */
public class WiFiDirectServicesList extends ListFragment {

	WiFiDevicesAdapter listAdapter = null;
	ProgressDialog progressDialog = null;

	interface DeviceClickListener {
		public void connectP2p(WiFiP2pService wifiP2pService);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.devices_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listAdapter = new WiFiDevicesAdapter(this.getActivity(),
				android.R.layout.simple_list_item_2, android.R.id.text1,
				new ArrayList<WiFiP2pService>());
		setListAdapter(listAdapter);
	}

	// /***
	// * ListView的点击事件
	// */
	// @Override
	// public void onListItemClick(ListView l, View v, int position, long id) {
	// ((DeviceClickListener) getActivity()).connectP2p((WiFiP2pService) l
	// .getItemAtPosition(position));
	// ((TextView) v.findViewById(android.R.id.text2)).setText("Connecting");
	// }

	public class WiFiDevicesAdapter extends ArrayAdapter<WiFiP2pService> {

		private List<WiFiP2pService> items;

		public WiFiDevicesAdapter(Context context, int resource,
				int textViewResourceId, List<WiFiP2pService> items) {
			super(context, resource, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(android.R.layout.simple_list_item_2, null);
			}
			WiFiP2pService service = items.get(position);
			if (service != null) {
				TextView nameText = (TextView) v
						.findViewById(android.R.id.text1);

				if (nameText != null) {
					String name = service.device.deviceAddress;
					Log.i("AAA", "設備地址:" + name);
					nameText.setText(service.device.deviceName);
				}
				TextView statusText = (TextView) v
						.findViewById(android.R.id.text2);
				statusText.setText(getDeviceStatus(service.device.status));

				// 提示信息
				Log.i("AAA", "设备名字:" + nameText.getText());
				Log.i("AAA", "设备状态:" + statusText.getText());
				
				//自动连接
				((DeviceClickListener) getActivity()).connectP2p(service);
				((TextView) v.findViewById(android.R.id.text2))
						.setText("Connecting");

			}
			return v;
		}

	}

	public static String getDeviceStatus(int statusCode) {
		switch (statusCode) {
		case WifiP2pDevice.CONNECTED:
			return "Connected";
		case WifiP2pDevice.INVITED:
			return "Invited";
		case WifiP2pDevice.FAILED:
			return "Failed";
		case WifiP2pDevice.AVAILABLE:
			return "Available";
		case WifiP2pDevice.UNAVAILABLE:
			return "Unavailable";
		default:
			return "Unknown";

		}
	}

	/**
     * 
     */
    public void onInitiateDiscovery() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "finding peers", true,
                true, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        
                    }
                });
    }
}
