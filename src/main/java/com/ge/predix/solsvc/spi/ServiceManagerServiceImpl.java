package com.ge.predix.solsvc.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author predix
 */
@Component
public class ServiceManagerServiceImpl
        implements IServiceManagerService
{

    @Autowired
    private SpringJAXRSServerFactoryBean defaultRestService;

    /**
     * 
     */
    protected Server                     server;

    private List<Object>                 serviceBeans = new ArrayList<Object>();

    /**
     * setup up an in-memory jetty so we can call API in the context of 'this'
     * test case JVM
     * 
     * @param service -
     * @param attributeMap -
     */
    public void setupServer(Object service, Map<String, String> attributeMap)
    {
        if ( service == null ) throw new IllegalStateException("service null"); //$NON-NLS-1$
        this.serviceBeans.add(service);
        this.defaultRestService.setServiceBeans(this.serviceBeans);

        String providerType = null;
        if ( attributeMap != null ) providerType = attributeMap.get(NamedCxfProperties.PREDIX_CUSTOM_CXF_PROVIDERS);
        if ( providerType != null )
        {
            Object provider;
            try
            {
                Class<?> c = Class.forName(providerType);
                provider = c.newInstance();
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            this.defaultRestService.setProvider(provider);
        }
    }

    /**
	 * 
	 */
    public ServiceManagerServiceImpl()
    {
        super();
    }

    /**
	 * 
	 */
    @PostConstruct
    public void init()
    {
        //
    }

    @Override
    public void createRestWebService(Object arg0)
    {
        setupServer(arg0, null);

    }

    @Override
    public void createRestWebService(Object arg0, Map<String, String> arg1)
    {
        setupServer(arg0, arg1);

    }

}
