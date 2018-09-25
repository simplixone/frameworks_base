/*
 * Copyright (C) 2018 Simplix Dot Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.systemui.qs.tiles;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.quicksettings.Tile;
import android.view.View;
import android.provider.Settings;
 import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.R;
 /** Quick settings tile: Gamemode **/
public class GamemodeTile extends QSTileImpl<BooleanState> {
	private final Icon mIcon = ResourceIcon.get(R.drawable.ic_gamemode);
	private int mEnabled;
	
	public GamemodeTile(QSHost host) {
        super(host);
        mEnabled = Settings.System.getIntForUser(mContext.getContentResolver(),
                   Settings.System.GAMEMODE_ENABLED, 0,
                   UserHandle.USER_CURRENT);
    }
    
    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SETTINGS;
    }
    
    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }
    
    @Override
    public void handleSetListening(boolean listening) {
    }
    
    @Override
    public void handleClick() {
        switchEnabled();
        refreshState();
    }
    
    @Override
    public Intent getLongClickIntent() {
        return new Intent().setComponent(new ComponentName(
            "com.simplix.center", "com.simplix.center.GameMode"));
    }
    
    private void switchEnabled() {
    	if (mEnabled == 0) {
    		mEnabled = 1;
    	} else {
    		mEnabled = 0;
    	}
    	Settings.System.putIntForUser(mContext.getContentResolver(),
                Settings.System.GAMEMODE_ENABLED, mEnabled,
                UserHandle.USER_CURRENT);
    }
    
    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_gamemode_label);
    }
    
    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.contentDescription =  mContext.getString(
                R.string.quick_settings_gamemode_label);
        state.icon = mIcon;
        state.label = mContext.getString(R.string.quick_settings_gamemode_label);
        if (state.slash == null) {
            state.slash = new SlashState();
        }
        if (mEnabled == 1) {
        	state.slash.isSlashed = false;
        	state.state = Tile.STATE_ACTIVE;
        } else {
        	state.slash.isSlashed = true;
        	state.state = Tile.STATE_INACTIVE;
        }
    }
}
