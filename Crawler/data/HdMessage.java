12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/proto/model/helpdesk/HdMessage.java
package ru.bgcrm.plugin.bgbilling.proto.model.helpdesk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bgcrm.model.FileData;
import ru.bgcrm.model.Id;

public class HdMessage
	extends Id
{
	/** Message.DIRECTION_INCOMING либо Message.DIRECTION_OUTGOING **/
	private int direction = 0;
	private int userIdFrom;
	private Date timeFrom;
	private int userIdTo;
	private Date timeTo;
	private String text;
	private final List<FileData> attachList = new ArrayList<FileData>();

	public int getDirection()
	{
		return direction;
	}

	public void setDirection( int direction )
	{
		this.direction = direction;
	}
	
	public int getUserIdFrom()
	{
		return userIdFrom;
	}

	public void setUserIdFrom( int userIdFrom )
	{
		this.userIdFrom = userIdFrom;
	}

	public int getUserIdTo()
	{
		return userIdTo;
	}

	public void setUserIdTo( int userIdTo )
	{
		this.userIdTo = userIdTo;
	}

	public Date getTimeFrom()
	{
		return timeFrom;
	}

	public void setTimeFrom( Date fromTime )
	{
		this.timeFrom = fromTime;
	}

	public Date getTimeTo()
	{
		return timeTo;
	}

	public void setTimeTo( Date toTime )
	{
		this.timeTo = toTime;
	}

	public String getText()
	{
		return text;
	}

	public void setText( String text )
	{
		this.text = text;		
	}
	
	public void addAttach( FileData file )
	{
		attachList.add( file );
	}

	public List<FileData> getAttachList()
	{
		return attachList;
	}
}