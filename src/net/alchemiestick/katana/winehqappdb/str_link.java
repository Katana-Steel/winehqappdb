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

public class str_link extends Object
{
    public final String str;
    public final String addr;
    public str_link(String p1, String p2)
    {
        this.str = p1;
        this.addr = p2;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if((other instanceof str_link)) {
            str_link lhs = (str_link)other;
            return (this.str == lhs.str);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() 
    {
        return this.str.hashCode();
    }
}