12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/proto/model/ContractMode.java
package ru.bgcrm.plugin.bgbilling.proto.model;

public class ContractMode
	extends UserTime
{
	public static final int MODE_CREDIT = 0;
	public static final int MODE_DEBET = 1;
	
	private String mode;

	public String getMode()
	{
		return mode;
	}

	public void setMode( String mode )
	{
		this.mode = mode;
	}
}
