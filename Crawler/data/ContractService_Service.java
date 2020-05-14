12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/ws/contract/ContractService_Service.java

package ru.bgcrm.plugin.bgbilling.ws.contract;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ContractService", targetNamespace = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/", wsdlLocation = "http://billing.office.bitel.ru/executer/ru.bitel.bgbilling.kernel.contract.api/ContractService?wsdl")
public class ContractService_Service
    extends Service
{

    private final static URL CONTRACTSERVICE_WSDL_LOCATION;
    private final static WebServiceException CONTRACTSERVICE_EXCEPTION;
    private final static QName CONTRACTSERVICE_QNAME = new QName("http://service.common.api.contract.kernel.bgbilling.bitel.ru/", "ContractService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://billing.office.bitel.ru/executer/ru.bitel.bgbilling.kernel.contract.api/ContractService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CONTRACTSERVICE_WSDL_LOCATION = url;
        CONTRACTSERVICE_EXCEPTION = e;
    }

    public ContractService_Service() {
        super(__getWsdlLocation(), CONTRACTSERVICE_QNAME);
    }

    public ContractService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), CONTRACTSERVICE_QNAME, features);
    }

    public ContractService_Service(URL wsdlLocation) {
        super(wsdlLocation, CONTRACTSERVICE_QNAME);
    }

    public ContractService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CONTRACTSERVICE_QNAME, features);
    }

    public ContractService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ContractService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ContractService
     */
    @WebEndpoint(name = "ContractService")
    public ContractService getContractService() {
        return super.getPort(new QName("http://service.common.api.contract.kernel.bgbilling.bitel.ru/", "ContractService"), ContractService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ContractService
     */
    @WebEndpoint(name = "ContractService")
    public ContractService getContractService(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.common.api.contract.kernel.bgbilling.bitel.ru/", "ContractService"), ContractService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CONTRACTSERVICE_EXCEPTION!= null) {
            throw CONTRACTSERVICE_EXCEPTION;
        }
        return CONTRACTSERVICE_WSDL_LOCATION;
    }

}
