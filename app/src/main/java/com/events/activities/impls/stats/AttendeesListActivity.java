package com.events.activities.impls.stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.events.R;
import com.events.activities.BaseActivity;
import com.events.adapters.impls.AttendeesAdapter;
import com.events.model.Error;
import com.events.model.impls.Attendee;
import com.events.utils.Auth;
import com.events.utils.Json;
import com.events.utils.Lang;
import com.events.utils.Logger;
import com.events.utils.PopupUtils;
import com.events.utils.ProgressView;
import com.events.workers.NetWorker;
import com.events.workers.callbacks.impls.SimpleNetWorkerCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class AttendeesListActivity extends BaseActivity {
	
	private Map<String, String> headersMap;
	
	private AttendeesAdapter 	adapter;
	
	private SwipeRefreshLayout 	swipeRefreshLayout;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_attendees_list);
		
		adapter = new AttendeesAdapter (null) {
			
			@Override
			protected void onRecordClick (int position, Attendee data) {
				Intent intent = new Intent (AttendeesListActivity.this, AttendeeDetailsActivity.class);
				intent.putExtra (Attendee.class.getSimpleName (), data);
				startActivity (intent);
			}
		};
		RecyclerView rv = findViewById (R.id.attendeesList);
		rv.setAdapter (adapter);
		
		fetch ();
	}
	
	private void fetch () {
		if (headersMap == null) {
			headersMap = new HashMap<> ();
			headersMap.put (NetWorker.Header.Accept, NetWorker.Type.AppJson);
			headersMap.put (NetWorker.Header.ContentType, NetWorker.Type.AppJson);
			headersMap.put (NetWorker.Header.Authorization, Auth.basic (getString (R.string.cloudantUsername), getString (R.string.cloudantPassword)));
		}
		
		netWorker.get (
			getString (R.string.cloudantUrl) + Lang.SLASH + getString (R.string.cloudantDatabase) + "/_all_docs?include_docs=true",
			null,
			headersMap,
			NetWorker.RespType.Json,
			new SimpleNetWorkerCallback (this) {
				
				@Override
				public Object onBGSuccess (Object unprocessedEntity, NetWorker.RespType respType) {
					JsonNode response = null;
					try {
						response = Json.build (((ResponseBody) unprocessedEntity).string ());
					} catch (Exception e) {
						Logger.error (AttendeesListActivity.class.getSimpleName () + " --> queryNetCallback OnBGSucc", e);
					}
					
					if (response == null || response.size () == 0) {
						return null;
					}
					
					ArrayNode rows = (ArrayNode)response.get ("rows");
					if (rows == null || rows.size () == 0) {
						return null;
					}
					
					List<Attendee> attendees = new ArrayList<> ();
					for (JsonNode row : rows) {
						JsonNode doc 		= row.get ("doc");
						Attendee attendee 	= Json.buildObjectFromJson (doc, Attendee.class);
						attendees.add (attendee);
					}
					
					return attendees;
				}
				
				@Override
				public void onUIFailure (Error error) {
					PopupUtils.show (AttendeesListActivity.this, error.getMsg ());
				}
				
				@Override
				public void onUISuccess (Object response) {
					List<Attendee> attendees = (List<Attendee>)response;
					
					if (attendees == null) {
						return;
					}
					
					adapter.setInputData (attendees);
					
					Logger.error (
						AttendeesListActivity.class.getSimpleName () + " --> UiSuccess",
						"Response: " + Arrays.toString (attendees.toArray (new Attendee [attendees.size ()]))
					);
				}
				
				@Override
				public void onUIEnd () {
					if (swipeRefreshLayout.isRefreshing ()) {
						swipeRefreshLayout.setRefreshing (false);
					}
					
					ProgressView.dismiss ();
				}
			}
		);
	}
	
	@Override
	protected void initEvents () {
		swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
			
			@Override
			public void onRefresh () {
				fetch ();
			}
		});
	}
	
	@Override
	protected void initUi () {
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById (R.id.swipeRefreshLayout);
	}
}