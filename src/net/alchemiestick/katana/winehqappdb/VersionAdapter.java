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

class VersionAdapter extends ArrayAdapter<Version>
{
    private LayoutInflater inflater;

    public VersionAdapter(Context cx)
    {
        super(cx, R.layout.verlist_item);
        this.inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        this.getItem(position).setRowView(this.inflater,parent);
        return this.getItem(position).getRowView();
    }
}
