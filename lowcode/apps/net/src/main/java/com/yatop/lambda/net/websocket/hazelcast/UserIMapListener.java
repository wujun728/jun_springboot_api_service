package com.yatop.lambda.net.websocket.hazelcast;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.impl.MapListenerAdapter;

public class UserIMapListener extends MapListenerAdapter<String, Object> {
    @Override
    public void onEntryEvent(EntryEvent<String, Object> event) {
    }
}