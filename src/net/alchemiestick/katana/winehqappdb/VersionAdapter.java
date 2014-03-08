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

import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VersionAdapter extends ArrayAdapter<String> 
{
    public Context cx;
    
    public VersionAdapter(Context cx)
    {
        super(cx, R.layout.verlist_item);
        this.cx = cx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String[] ver = this.getItem(position).split(";",-4);
        LayoutInflater inflater = (LayoutInflater) this.cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.verlist_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tversion);
        textView.setText(ver[0]);
        textView = (TextView) rowView.findViewById(R.id.wine_version);
        textView.setText(ver[1]);
        textView = (TextView) rowView.findViewById(R.id.tdate);
        textView.setText(ver[2]);
        textView = (TextView) rowView.findViewById(R.id.tcls);
        textView.setText(ver[3]);
        return rowView;
    }
}
