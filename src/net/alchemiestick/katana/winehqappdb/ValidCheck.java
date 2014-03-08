/*
    Copyright 2012 Rene Kjellerup
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.alchemiestick.katana.winehqappdb;

import net.alchemiestick.katana.winehqappdb.*;

import com.google.android.vending.licensing.*;

import java.io.*;
import java.util.*;
import android.accounts.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.os.AsyncTask;
import android.net.*;
import android.net.http.*;
import android.provider.Settings.Secure;
import android.webkit.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.message.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;

public class ValidCheck implements LicenseCheckerCallback
{
    private SearchView mainAct;
    private Handler uiHnd;
    public Boolean done;
    public Boolean Allow;
    
    public ValidCheck(SearchView act, Handler hnd)
    {
        mainAct = act;
        uiHnd = hnd;
        Allow = false;
        done = false;
    }
    
    private void retry()
    {
        uiHnd.post(new Runnable() {
            public void run() {
                mainAct.makeCheck();
            }
        });
    }
    
    private void unlicensed()
    {
        uiHnd.post(new Runnable() {
            public void run() {
                mainAct.showDialog(SearchView.UNLICENSED);
            }
        });
    }
    
    private void setInput(final String msg) 
    {
        uiHnd.post(new Runnable() {
            public void run() {
                mainAct.input.setText(msg);
            }
        });
    }
    
    public void allow(int reason)
    {
        if(mainAct.isFinishing()) {
            return;
        }
        done  = true; 
        Allow = true;
//        setInput("Allow: " + new Integer(reason).toString());
    }
    public void dontAllow(int reason)
    {
        if(mainAct.isFinishing()) {
            return;
        }
        if(reason == Policy.RETRY)
            retry();
        else {
            unlicensed();
           done = true; 
        }
        setInput("!Allow: " + new Integer(reason).toString());
    }
    public void applicationError(int reason)
    {
        if(mainAct.isFinishing()) {
            return;
        }
        done = true; 
//        setInput("Error: " + new Integer(reason).toString());
    }
};
