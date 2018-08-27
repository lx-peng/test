
package cn.itcast.demo.ws.impl;

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
@WebServiceClient(name = "WeatherServiceService", targetNamespace = "http://impl.ws.demo.itcast.cn/", wsdlLocation = "http://localhost:9090/ws/ws/weather?wsdl")
public class WeatherServiceService
    extends Service
{

    private final static URL WEATHERSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException WEATHERSERVICESERVICE_EXCEPTION;
    private final static QName WEATHERSERVICESERVICE_QNAME = new QName("http://impl.ws.demo.itcast.cn/", "WeatherServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:9090/ws/ws/weather?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WEATHERSERVICESERVICE_WSDL_LOCATION = url;
        WEATHERSERVICESERVICE_EXCEPTION = e;
    }

    public WeatherServiceService() {
        super(__getWsdlLocation(), WEATHERSERVICESERVICE_QNAME);
    }

    public WeatherServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), WEATHERSERVICESERVICE_QNAME, features);
    }

    public WeatherServiceService(URL wsdlLocation) {
        super(wsdlLocation, WEATHERSERVICESERVICE_QNAME);
    }

    public WeatherServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, WEATHERSERVICESERVICE_QNAME, features);
    }

    public WeatherServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WeatherServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IWeatherService
     */
    @WebEndpoint(name = "WeatherServicePort")
    public IWeatherService getWeatherServicePort() {
        return super.getPort(new QName("http://impl.ws.demo.itcast.cn/", "WeatherServicePort"), IWeatherService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IWeatherService
     */
    @WebEndpoint(name = "WeatherServicePort")
    public IWeatherService getWeatherServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.ws.demo.itcast.cn/", "WeatherServicePort"), IWeatherService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (WEATHERSERVICESERVICE_EXCEPTION!= null) {
            throw WEATHERSERVICESERVICE_EXCEPTION;
        }
        return WEATHERSERVICESERVICE_WSDL_LOCATION;
    }

}
