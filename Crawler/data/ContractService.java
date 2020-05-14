12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/bgbilling/ws/contract/ContractService.java

package ru.bgcrm.plugin.bgbilling.ws.contract;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ContractService", targetNamespace = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/")
@XmlSeeAlso({
    ObjectFactory.class
})
@Deprecated
public interface ContractService {


    /**
     * 
     * @param contractGroupId
     * @param contractId
     * @throws BGException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "contractGroupRemove", targetNamespace = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/", className = "ru.bgcrm.plugin.bgbilling.ws.contract.ContractGroupRemove")
    @ResponseWrapper(localName = "contractGroupRemoveResponse", targetNamespace = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/", className = "ru.bgcrm.plugin.bgbilling.ws.contract.ContractGroupRemoveResponse")
    @Action(input = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/ContractService/contractGroupRemoveRequest", output = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/ContractService/contractGroupRemoveResponse", fault = {
        @FaultAction(className = BGException_Exception.class, value = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/ContractService/contractGroupRemove/Fault/BGException")
    })
    public void contractGroupRemove(
        @WebParam(name = "contractId", targetNamespace = "")
        int contractId,
        @WebParam(name = "contractGroupId", targetNamespace = "")
        int contractGroupId)
        throws BGException_Exception
    ;

    /**
     * 
     * @param contractGroupId
     * @param contractId
     * @throws BGException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "contractGroupAdd", targetNamespace = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/", className = "ru.bgcrm.plugin.bgbilling.ws.contract.ContractGroupAdd")
    @ResponseWrapper(localName = "contractGroupAddResponse", targetNamespace = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/", className = "ru.bgcrm.plugin.bgbilling.ws.contract.ContractGroupAddResponse")
    @Action(input = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/ContractService/contractGroupAddRequest", output = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/ContractService/contractGroupAddResponse", fault = {
        @FaultAction(className = BGException_Exception.class, value = "http://service.common.api.contract.kernel.bgbilling.bitel.ru/ContractService/contractGroupAdd/Fault/BGException")
    })
    public void contractGroupAdd(
        @WebParam(name = "contractId", targetNamespace = "")
        int contractId,
        @WebParam(name = "contractGroupId", targetNamespace = "")
        int contractGroupId)
        throws BGException_Exception
    ;

}
