12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/proto/model/UserTime.java
package ru.bgcrm.plugin.bgbilling.proto.model;

import java.util.Date;

public class UserTime
{
	private String user;
	private Date time;
	
	public String getUser()
	{
		return user;
	}

	public void setUser( String user )
	{
		this.user = user;
	}

	public Date getTime()
	{
		return time;
	}

	public void setTime( Date time )
	{
		this.time = time;
	}

}
