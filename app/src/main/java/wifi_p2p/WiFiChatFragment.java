package wifi_p2p;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group.R;

/**
 * This fragment handles chat related UI which includes a list view for messages
 * and a message entry field with send button.
 * 这个Fragment是处理聊天UI的，Fragment包括一个现实信息的列表和一个信息发送按钮。
 */
public class WiFiChatFragment extends Fragment {

	private WiFiChatFragment chatFragment;
	private View view;
	private ChatManager chatManager;
	public ChatManager cm;
	private TextView chatLine;
	private ListView listView;
	ChatMessageAdapter adapter = null;
	private List<String> items = new ArrayList<String>();
	//学生到课信息汇报老师
	private SharedPreferences preferencesToteacher;
	private SharedPreferences.Editor editorToteacher;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("AAA", "OnCreateView");
		view = inflater.inflate(R.layout.fragment_chat, container, false);
		//chatLine = (TextView) view.findViewById(R.id.txtChatLine);
		listView = (ListView) view.findViewById(android.R.id.list);
		adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,
				items);
		listView.setAdapter(adapter);
	
		view.findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (chatManager != null) {
					  preferencesToteacher=getActivity().getSharedPreferences("reportInfo",0);
					  String yiDaoInfo=preferencesToteacher.getString("yidaoInfo", null);
					  String weiDaoInfo=preferencesToteacher.getString("weidaoInfo", null);
					  String messageToteacher=yiDaoInfo+weiDaoInfo;
						chatManager.write(messageToteacher.toString().getBytes());
						/*chatManager.write(chatLine.getText().toString()
								.getBytes());
						pushMessage("Me:" + chatLine.getText().toString());
							chatLine.setText("");*/
							//自动发完一条消息后断开
					     ((WiFiServiceDiscoveryActivity) getActivity()).onStop();
					     ((WiFiServiceDiscoveryActivity) getActivity()).closeWifi();
						}
					}
				});
		return view;
	}

	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// 默认发送一条信息
		if (chatManager != null) {
			//String welcome = "";
			//chatManager.write(welcome.getBytes());
			//pushMessage(welcome);
		} else {
			//Toast.makeText(getActivity(), "ChatManager未初始化", 2000).show();
		}
		Log.i("AAA", "OnActivityCreated");
	}

	public interface MessageTarget {
		public Handler getHandler();
	}

	public void setChatManager(ChatManager obj) {
		chatManager = obj;
	}

	public void pushMessage(String readMessage) {
		adapter.add(readMessage);
		adapter.notifyDataSetChanged();
	}

	/**
	 * ArrayAdapter to manage chat messages.
	 */
	public class ChatMessageAdapter extends ArrayAdapter<String> {

		List<String> messages = null;

		public ChatMessageAdapter(Context context, int textViewResourceId,
				List<String> items) {
			super(context, textViewResourceId, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(android.R.layout.simple_list_item_1, null);
			}
			String message = items.get(position);
			Toast.makeText(getActivity(),message,1000).show();
			if (message != null && !message.isEmpty()) {
				String success="签到成功！";
				chatManager.write(success.getBytes());
				TextView nameText = (TextView) v
						.findViewById(android.R.id.text1);

				if (nameText != null) {
					nameText.setText(message);
					if (message.startsWith("Me:")) {
						nameText.setTextAppearance(getActivity(),
								R.style.normalText);
					} else {
						nameText.setTextAppearance(getActivity(),
								R.style.boldText);
					}
				}
			}
			return v;
		}
	}
}
