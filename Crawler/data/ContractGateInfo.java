12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/proto/model/ipn/ContractGateInfo.java
package ru.bgcrm.plugin.bgbilling.proto.model.ipn;

import java.util.ArrayList;
import java.util.List;

public class ContractGateInfo
{
	private int statusId;
	private List<ContractGate> gateList = new ArrayList<ContractGate>();
	private List<ContractGateLogItem> statusLog = new ArrayList<ContractGateLogItem>();

	public void setStatusId( int statusId )
	{
		this.statusId = statusId;
	}

	public int getStatusId()
	{
		return statusId;
	}

	public List<ContractGate> getGateList()
	{
		return gateList;
	}

	public List<ContractGateLogItem> getStatusLog()
	{
		return statusLog;
	}
}
