package org.redcross.sar.mso.data;

import org.redcross.sar.util.mso.Position;

public interface IPOIIf extends IMsoObjectIf
{
    public void setPosition(Position aPosition);

    public Position getPosition();
    
    public void setType(String aType);

    public String getType();

}