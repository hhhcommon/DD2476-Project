12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/proto/dao/BalanceDAO.java
package ru.bgcrm.plugin.bgbilling.proto.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ru.bgcrm.model.BGException;
import ru.bgcrm.model.user.User;
import ru.bgcrm.plugin.bgbilling.Request;
import ru.bgcrm.plugin.bgbilling.dao.BillingDAO;
import ru.bgcrm.plugin.bgbilling.proto.model.balance.ContractAccount;
import ru.bgcrm.plugin.bgbilling.proto.model.balance.ContractBalanceDetail;
import ru.bgcrm.plugin.bgbilling.proto.model.balance.ContractBalanceGeneral;
import ru.bgcrm.plugin.bgbilling.proto.model.balance.ContractCharge;
import ru.bgcrm.plugin.bgbilling.proto.model.balance.ContractPayment;
import ru.bgcrm.util.TimeUtils;
import ru.bgcrm.util.Utils;
import ru.bgcrm.util.XMLUtils;

public class BalanceDAO
	extends BillingDAO
{
	private static final String CONTRACT_MODULE_ID = "contract";

	public BalanceDAO( User user, String billingId )
		throws BGException
	{
		super( user, billingId );
	}

	public BigDecimal getContractPaymentList( int contractId, Date dateFrom, Date dateTo, 
	                                          List<ContractPayment> paymentList, List<ContractPayment> subPaymentList )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "ContractPayments" );
		request.setContractId( String.valueOf( contractId ) );
		request.setAttribute( "date1", TimeUtils.format( dateFrom, TimeUtils.PATTERN_DDMMYYYY ) );
		request.setAttribute( "date2", TimeUtils.format( dateTo, TimeUtils.PATTERN_DDMMYYYY ) );

		BigDecimal summa = BigDecimal.ZERO;
		
		Document doc = transferData.postData( request, user );
		
		if( paymentList != null )
		{
			for( Element rowElement : XMLUtils.selectElements( doc, "/data/table/data/row" ) )
			{
				ContractPayment payment = new ContractPayment();
				payment.setId( Utils.parseInt( rowElement.getAttribute( "f0" ) ) );
				payment.setEditable( Utils.parseBoolean( rowElement.getAttribute( "f1" ) ) );
				payment.setDate( TimeUtils.parse( rowElement.getAttribute( "f2" ), TimeUtils.PATTERN_DDMMYYYY ) );
				payment.setLastChangeTime( TimeUtils.parse( rowElement.getAttribute( "f6" ), TimeUtils.PATTERN_DDMMYYYYHHMMSS ) );
				payment.setSum( Utils.parseBigDecimal( rowElement.getAttribute( "f3" ) ) );
				payment.setType( rowElement.getAttribute( "f4" ) );
				payment.setComment( rowElement.getAttribute( "f5" ) );
				payment.setUser( rowElement.getAttribute( "f7" ) );

				paymentList.add( payment );
				
				summa = summa.add( payment.getSum() );
			}
		}
		
		if( subPaymentList != null )
		{
			for( Element rowElement : XMLUtils.selectElements( doc, "/data/sub_table/data/row" ) )
			{
				ContractPayment payment = new ContractPayment();
				payment.setContract( rowElement.getAttribute( "contract" ) );
				payment.setDate( TimeUtils.parse( rowElement.getAttribute( "date" ), TimeUtils.PATTERN_DDMMYYYY ) );
				payment.setSum( Utils.parseBigDecimal( rowElement.getAttribute( "summa" ) ) );
				payment.setType( rowElement.getAttribute( "type" ) );
				
				subPaymentList.add( payment );
			}
		}

		return summa;
	}

	public BigDecimal getContractChargeList( int contractId, Date dateFrom, Date dateTo,
	                                         List<ContractCharge> chargeList, List<ContractCharge> subChargeList )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "ContractCharges" );
		request.setContractId( String.valueOf( contractId ) );
		request.setAttribute( "date1", TimeUtils.format( dateFrom, TimeUtils.PATTERN_DDMMYYYY ) );
		request.setAttribute( "date2", TimeUtils.format( dateTo, TimeUtils.PATTERN_DDMMYYYY ) );

		Document doc = transferData.postData( request, user );
		
		if( chargeList != null )
		{
			for( Element rowElement : XMLUtils.selectElements( doc, "/data/table/data/row" ) )
			{
				ContractCharge charge = new ContractCharge();
				charge.setId( Utils.parseInt( rowElement.getAttribute( "f0" ) ) );
				charge.setEditable( Utils.parseBoolean( rowElement.getAttribute( "f1" ) ) );
				charge.setDate( TimeUtils.parse( rowElement.getAttribute( "f2" ), TimeUtils.PATTERN_DDMMYYYY ) );
				charge.setLastChangeTime( TimeUtils.parse( rowElement.getAttribute( "f6" ), TimeUtils.PATTERN_DDMMYYYYHHMMSS ) );
				charge.setSum( Utils.parseBigDecimal( rowElement.getAttribute( "f3" ) ) );
				charge.setType( rowElement.getAttribute( "f4" ) );
				charge.setComment( rowElement.getAttribute( "f5" ) );
				charge.setUser( rowElement.getAttribute( "f7" ) );

				chargeList.add( charge );
			}
		}

		//subcontract charges
		if( subChargeList != null )
		{
			for( Element rowElement : XMLUtils.selectElements( doc, "/data/sub_table/data/row" ) )
			{
				ContractCharge charge = new ContractCharge();
				charge.setContract( rowElement.getAttribute( "contract" ) );
				charge.setDate( TimeUtils.parse( rowElement.getAttribute( "date" ), TimeUtils.PATTERN_DDMMYYYY ) );
				charge.setSum( Utils.parseBigDecimal( rowElement.getAttribute( "summa" ) ) );
				charge.setType( rowElement.getAttribute( "type" ) );
				
				subChargeList.add( charge );
			}
		}

		return Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa" ) );
	}
	
	public BigDecimal getContractAccountList( int contractId, Date dateFrom, Date dateTo,
	                                          List<ContractAccount> accountList, List<ContractAccount> subAccountList )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "ContractAccounts" );
		request.setContractId( String.valueOf( contractId ) );
		request.setAttribute( "date1", TimeUtils.format( dateFrom, TimeUtils.PATTERN_DDMMYYYY ) );
		request.setAttribute( "date2", TimeUtils.format( dateTo, TimeUtils.PATTERN_DDMMYYYY ) );

		Document doc = transferData.postData( request, user );
		
		if( accountList != null )
		{
			for( Element rowElement : XMLUtils.selectElements( doc, "/data/table/data/row" ) )
			{
				ContractAccount account = new ContractAccount();
				account.setServiceId( Utils.parseInt( rowElement.getAttribute( "sid" ) ) );
				account.setMonth( rowElement.getAttribute( "date" ) );
				account.setSum( Utils.parseBigDecimal( rowElement.getAttribute( "summa" ) ) );
				account.setTitle( rowElement.getAttribute( "title" ) );

				accountList.add( account );
			}
		}

		//subcontract charges
		if( subAccountList != null )
		{
			for( Element rowElement : XMLUtils.selectElements( doc, "/data/sub_table/data/row" ) )
			{
				ContractAccount account = new ContractAccount();
				account.setContract( rowElement.getAttribute( "contract" ) );
				account.setServiceId( Utils.parseInt( rowElement.getAttribute( "sid" ) ) );
				account.setMonth( rowElement.getAttribute( "date" ) );
				account.setSum( Utils.parseBigDecimal( rowElement.getAttribute( "summa" ) ) );
				account.setTitle( rowElement.getAttribute( "service" ) );

				subAccountList.add( account );
			}
		}

		return Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa" ) );
	}
	
	public BigDecimal[] getContractBalanceList( int contractId, Date dateFrom, Date dateTo, List<ContractBalanceGeneral> list )
    	throws BGException
    {
    	Request request = new Request();
    	request.setModule( CONTRACT_MODULE_ID );
    	request.setAction( "ContractBalanceGeneral" );
    	request.setContractId( String.valueOf( contractId ) );
    	request.setAttribute( "date1", TimeUtils.format( dateFrom, TimeUtils.PATTERN_DDMMYYYY ) );
    	request.setAttribute( "date2", TimeUtils.format( dateTo, TimeUtils.PATTERN_DDMMYYYY ) );
    
    	Document doc = transferData.postData( request, user );
    	
    	for( Element rowElement : XMLUtils.selectElements( doc, "/data/table/data/row" ) )
    	{
    		ContractBalanceGeneral balanceGeneral = new ContractBalanceGeneral();
    		balanceGeneral.setAccount( Utils.parseBigDecimal( rowElement.getAttribute( "account" ) ) );
    		balanceGeneral.setCharge( Utils.parseBigDecimal( rowElement.getAttribute( "charge" ) ) );
    		balanceGeneral.setInputBalance( Utils.parseBigDecimal( rowElement.getAttribute( "input_balance" ) ) );
    		balanceGeneral.setMonth( rowElement.getAttribute( "month" ) );
    		balanceGeneral.setOutputBalance( Utils.parseBigDecimal( rowElement.getAttribute( "output_balance" ) ) );
    		balanceGeneral.setPayment( Utils.parseBigDecimal( rowElement.getAttribute( "payment" ) ) );
    
    		list.add( balanceGeneral );
    	}
    
    	BigDecimal[] summs = new BigDecimal[]
    	{
    		Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa1" ) ),
    		Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa2" ) ),
    		Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa3" ) ),
    		Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa4" ) ),
    		Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa5" ) )
    	};
    	
    	return summs;
    }

	public BigDecimal getContractBalanceDetailList( int contractId, Date dateFrom, Date dateTo, List<ContractBalanceDetail> list )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "ContractBalanceDetail" );
		request.setContractId( String.valueOf( contractId ) );
		request.setAttribute( "date1", TimeUtils.format( dateFrom, TimeUtils.PATTERN_DDMMYYYY ) );
		request.setAttribute( "date2", TimeUtils.format( dateTo, TimeUtils.PATTERN_DDMMYYYY ) );

		Document doc = transferData.postData( request, user );

		if( list != null )
    	{
    		for( Element rowElement : XMLUtils.selectElements( doc, "/data/table/data/row" ) )
    		{
    			ContractBalanceDetail balanceDetail = new ContractBalanceDetail();
    			balanceDetail.setComment( rowElement.getAttribute( "comment" ) );
    			balanceDetail.setDate( rowElement.getAttribute( "date" ) );
    			balanceDetail.setSumma( Utils.parseBigDecimal( rowElement.getAttribute( "summa" ) ) );
    			balanceDetail.setType( rowElement.getAttribute( "type" ) );
    
    			list.add( balanceDetail );
    		}
    	}

		return Utils.parseBigDecimal( XMLUtils.selectText( doc, "/data/table/@summa" ) );
	}

	public int addContractPayment( int contractId, BigDecimal summa, Date date, int typeId, String comment )
		throws BGException
	{
		return updateContractPayment( 0, contractId, summa, date, typeId, comment );
	}

	public int updateContractPayment( int id, int contractId, BigDecimal summa, Date date, int typeId, String comment )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "UpdateContractPayment" );
		request.setContractId( String.valueOf( contractId ) );
		request.setAttribute( "date", TimeUtils.format( date, TimeUtils.PATTERN_DDMMYYYY ) );
		request.setAttribute( "pt", typeId );
		request.setAttribute( "summa", summa );
		request.setAttribute( "comment", comment );
		if( id == 0 )
		{
			request.setAttribute( "id", "new" );
		}
		else
		{
			request.setAttribute( "id", id );
		}

		Document doc = transferData.postDataSync( request, user );

		return Utils.parseInt( XMLUtils.getElement( doc, "data" ).getAttribute( "id" ) );
	}

	public int addContractCharge( int contractId, BigDecimal summa, Date date, int typeId, String comment )
		throws BGException
	{
		return updateContractCharge( 0, contractId, summa, date, typeId, comment );
	}

	public int updateContractCharge( int id, int contractId, BigDecimal summa, Date date, int typeId, String comment )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "UpdateContractCharge" );
		request.setContractId( String.valueOf( contractId ) );
		request.setAttribute( "date", TimeUtils.format( date, TimeUtils.PATTERN_DDMMYYYY ) );
		request.setAttribute( "pt", typeId );
		request.setAttribute( "summa", summa );
		request.setAttribute( "comment", comment );
		if( id == 0 )
		{
			request.setAttribute( "id", "new" );
		}
		else
		{
			request.setAttribute( "id", id );
		}

		Document doc = transferData.postDataSync( request, user );

		return Utils.parseInt( XMLUtils.getElement( doc, "data" ).getAttribute( "id" ) );
	}

	public ContractPayment getContractPayment( int paymentId )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "ContractPayment" );
		request.setAttribute( "id", paymentId );

		Document document = transferData.postData( request, user );

		Element dataElement = document.getDocumentElement();
		NodeList nodeList = dataElement.getElementsByTagName( "payment" );

		if( nodeList.getLength() > 0 )
		{
			ContractPayment payment = new ContractPayment();
			Element element = (Element)nodeList.item( 0 );
			payment.setId( paymentId );
			payment.setDate( TimeUtils.parse( element.getAttribute( "date" ), TimeUtils.PATTERN_DDMMYYYY ) );
			payment.setComment( element.getAttribute( "comment" ) );
			payment.setSum( Utils.parseBigDecimal( element.getAttribute( "summa" ) ) );
			payment.setType( element.getAttribute( "pt" ) );

			return payment;
		}

		return null;
	}

	public ContractCharge getContractCharge( int chargeId )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "ContractCharge" );
		request.setAttribute( "id", chargeId );

		Document document = transferData.postData( request, user );

		Element dataElement = document.getDocumentElement();
		NodeList nodeList = dataElement.getElementsByTagName( "charge" );

		if( nodeList.getLength() > 0 )
		{
			ContractCharge charge = new ContractCharge();
			Element element = (Element)nodeList.item( 0 );

			charge.setId( chargeId );
			charge.setDate( TimeUtils.parse( element.getAttribute( "date" ), TimeUtils.PATTERN_DDMMYYYY ) );
			charge.setComment( element.getAttribute( "comment" ) );
			charge.setSum( Utils.parseBigDecimal( element.getAttribute( "summa" ) ) );
			charge.setType( element.getAttribute( "pt" ) );

			return charge;
		}

		return null;
	}

	public void deleteContractCharge( int chargeId, int contractId )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "DeleteContractCharge" );
		request.setAttribute( "id", chargeId );
		request.setContractId( contractId );

		transferData.postData( request, user );
	}

	public void deleteContractPayment( int paymentId, int contractId )
		throws BGException
	{
		Request request = new Request();
		request.setModule( CONTRACT_MODULE_ID );
		request.setAction( "DeleteContractPayment" );
		request.setAttribute( "id", paymentId );
		request.setContractId( contractId );

		transferData.postData( request, user );
	}
	
	@Deprecated
	public List<ContractPayment> getContractPaymentList( int contractId, Date dateFrom, Date dateTo )
		throws BGException
	{
		List<ContractPayment> result = new ArrayList<ContractPayment>();
		getContractPaymentList( contractId, dateFrom, dateTo, result, null );
		return result;
	}
	
	@Deprecated
	public BigDecimal getContractBalanceSum( int contractId, Date dateFrom, Date dateTo )
		throws BGException
	{
		return new ContractDAO( user, dbInfo ).getContractInfo( contractId ).getBalanceOut();
	}
	
	@Deprecated
	public BigDecimal getContractAccountSum( int contractId, Date dateFrom, Date dateTo )
		throws BGException
	{
		return getContractAccountList( contractId, dateFrom, dateTo, null, null );
	}
}
