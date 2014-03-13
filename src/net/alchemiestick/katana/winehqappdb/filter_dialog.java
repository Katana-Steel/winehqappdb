package net.alchemiestick.katana.winehqappdb;

import net.alchemiestick.katana.winehqappdb.*;

import java.util.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.view.View.*;
import android.widget.*;
import android.widget.TextView.*;
import android.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.message.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;

public class filter_dialog extends Dialog
{
    private Context cx;
    private List<NameValuePair> webData;
    private View.OnClickListener vocl;
    private AdapterView.OnItemSelectedListener rateListener; // rate spinner
    private AdapterView.OnItemSelectedListener licenseListener; // license spinner
    private AdapterView.OnItemSelectedListener placementListener; // placement spinner
    private OnEditorActionListener maxLines;
    
    public filter_dialog(Context cx, List<NameValuePair> data) {
        super(cx);
        this.cx = cx;
        this.webData = data;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vocl = new View.OnClickListener() {
            public void onClick(View v) {
                onCheckboxClick(v);
            }
        };
        
        maxLines = new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent key) {
                boolean handled = false;
                if( action == EditorInfo.IME_ACTION_DONE) {
                    setMaxLines(tv.getText().toString());
                    tv.setText(getNamedData("iPage"));
                    InputMethodManager imm = (InputMethodManager)tv.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        };
        rateListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                rateSelected((String)parent.getItemAtPosition(pos));
            }
            
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        licenseListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                licenseSelected((String)parent.getItemAtPosition(pos));
            }
            
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        placementListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                placementSelected((String)parent.getItemAtPosition(pos));
            }
            
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        
    }
    
    private void setupSpinner(int spId, int arrayId, String cur, AdapterView.OnItemSelectedListener listener) 
    { 
        Spinner sp = (Spinner)findViewById(spId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(cx, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        int pos = adapter.getPosition(cur);
        sp.setSelection(pos);
        sp.setOnItemSelectedListener(listener);
    }
    
    @Override
    protected void onCreate(Bundle savedI)
    {
        super.onCreate(savedI);
        this.setContentView(R.layout.flt_dlg);
        
        CheckBox xb = (CheckBox)findViewById(R.id.ascbx);
        xb.setOnClickListener( vocl );
        if(getNamedData("bAscending").compareTo(new String("true")) == 0) {
            xb.setChecked(true);
        } else {
            xb.setChecked(false);
        }
        
        EditText tv = (EditText)findViewById(R.id.max_items);
        tv.setText(getNamedData("iPage"));
        tv.setOnEditorActionListener(maxLines);
        
        setupSpinner(R.id.rate_sel, R.array.rate_values, getNamedData("sappVersion-ratingData"),rateListener);
        setupSpinner(R.id.lic_sel, R.array.lic_values, getNamedData("sappVersion-licenseData"),licenseListener);
        setupSpinner(R.id.plc_sel, R.array.plc_values, getNamedData("iappFamily-appNameOp"),placementListener);
    }

    private String getNamedData(String name) 
    {
        ListIterator<NameValuePair> itr = webData.listIterator();
        while(itr.hasNext())
        {
            NameValuePair t = itr.next();
            if(t.getName() == name) {
                return t.getValue();
            }
        }
        return new String();
    }

    private void setNamedData(String name, String value) 
    {
        ListIterator<NameValuePair> itr = webData.listIterator();
        while(itr.hasNext())
        {
            NameValuePair t = itr.next();
            if(t.getName() == name) {
                itr.set(new BasicNameValuePair(name, value));
                return;
            }
        }
        webData.add(new BasicNameValuePair(name, value));
    }

    public void setMaxLines(String r)
    {
        try {
            Long num = new Long(r);
            if(num > 100) {
                setNamedData("iPage","100");
            } else  if (num < 1) {
                setNamedData("iPage","1");
            } else {
                setNamedData("iPage",num.toString());
            }
        }
        catch(NumberFormatException e)
        {
                setNamedData("iPage",getNamedData("iPage"));
        }
    }
    
    public void rateSelected(String r)
    {
        if(r.compareTo(new String("All")) == 0) {
            setNamedData("sappVersion-ratingData","");
        } else {
            setNamedData("sappVersion-ratingData",r);
        }
    }

    public void licenseSelected(String r)
    {
        if(r.compareTo(new String("All")) == 0) {
            setNamedData("sappVersion-licenseData","");
        } else {
            setNamedData("sappVersion-licenseData",r);
        }
    }
    
    public void placementSelected(String r)
    {
        if(r.compareTo(new String("contains")) == 0) {
            setNamedData("iappFamily-appNameOp","2");
        } else if (r.compareTo(new String("starts with")) == 0) {
            setNamedData("iappFamily-appNameOp","3");
        } else if (r.compareTo(new String("ends with")) == 0) {
            setNamedData("iappFamily-appNameOp","4");
        } 
    }
    
    public void onCheckboxClick(View v)
    {
        boolean checked = ((CheckBox)v).isChecked();
        switch(v.getId())
        {
        case R.id.ascbx:
            if(checked) {
                this.setNamedData("bAscending","true");
            } else {
                this.setNamedData("bAscending","false");
            }
            break;
        }
    }

};
