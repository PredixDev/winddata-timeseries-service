package com.ge.predix.solsvc.boot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * This project uses a SpringBoot HelloWorld as a starting point. Then it adds in the capability to cf push a 
 * CXF Rest Service simply by extending PredixSpringBootInitializer.
 * 
 * The idea is that you'll use this project as a starting point for creating your own Rest service.  You can change
 * "predix-microservice-cf" to "my-rest-service" or a name that suits you.
 * 
 * We provide 2 types of Rest APIs/Impls and tests that invoke them
 * 1. DefaultService - example using a Spring Bean registered with CXF and standard java annotations
 * 2. DynamicService - example using the original Predix way of registering a Rest Service with CXF
 *  
 * The project also provides a point of view around Property file management and Spring Profiles. Here is the hierarchy
 * such that each lower number overrides the one's after it
 * http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
 * 
 * 1. Command line arguments.
 * 2. JNDI attributes from java:comp/env.
 * 3. Java System properties (System.getProperties()).
 * 4. OS environment variables.
 * 5. A RandomValuePropertySource that only has properties in random.*.
 * 6. Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants)
 * 7. Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants)
 * 8. Application properties outside of your packaged jar (application.properties and YAML variants).
 * 9. Application properties packaged inside your jar (application.properties and YAML variants).
 * 10. @PropertySource annotations on your @Configuration classes. 
 * 11. Default properties (specified using SpringApplication.setDefaultProperties)
 * 
 * Note that Spring Beans in other packages will not be automatically scanned.  You'll need to add 
 * this command if you have other packages this command will do it
 * \@ComponentScan(basePackages = {"com.ge.package1", "com.ge.package2"})
 * 
 * Or if you want to import Spring XMLs from other dependent projects you can use the @ImportResource, e.g. 
 * \@ImportResource(
 * {
 *         "classpath*:META-INF/spring/predix-rest-client-scan-context.xml"
 * })
 * 
 * For other Spring Features see: http://docs.spring.io/spring-boot/docs/current/reference/html/
 * 
 * @author predix
 */
@EnableAutoConfiguration(exclude =
{
        //Add any configuration loading call you want to exclude
        
})
@PropertySource("classpath:application-default.properties")
@ImportResource(
{
    "classpath*:META-INF/spring/winddata-cxf-context.xml",
    "classpath*:META-INF/spring/winddata-scan-context.xml",
    "classpath*:META-INF/spring/ext-util-scan-context.xml",
    "classpath*:META-INF/spring/predix-rest-client-scan-context.xml",
    "classpath*:META-INF/spring/predix-websocket-client-scan-context.xml",
    "classpath*:META-INF/spring/timeseries-bootstrap-scan-context.xml"
       
})
@Controller
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @SuppressWarnings("javadoc")
    @Value("${spring.profiles.active:local}")
    String profile ;
    
    @SuppressWarnings("javadoc")
    @Value("${java.docs.url:null}")
    String docsUrl ;


    /**
     * @param args -
     */
    @SuppressWarnings(
    {
            "nls", "resource"
    })
    public static void main(String[] args)
    {
        SpringApplication springApplication = new SpringApplication(Application.class);
        ApplicationContext ctx = springApplication.run(args);

        log.debug("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames)
        {
            log.debug(beanName);
        }
        
        log.debug("Let's inspect the profiles provided by Spring Boot:");
        String profiles[] = ctx.getEnvironment().getActiveProfiles();
        for (int i = 0; i < profiles.length; i++)
            log.debug("profile=" + profiles[i]);

        log.debug("Let's inspect the properties provided by Spring Boot:");
        MutablePropertySources propertySources = ((StandardServletEnvironment) ctx.getEnvironment())
                .getPropertySources();
        Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources.iterator();
        while (iterator.hasNext()) {
            Object propertySourceObject = iterator.next();
            if ( propertySourceObject instanceof org.springframework.core.env.PropertySource ) {
                org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
                log.debug("propertySource=" + propertySource.getName() + " values=" + propertySource.getSource() + "class=" + propertySource.getClass());           
            }         
        }
    }

    /**
     * Ensure the Tomcat container comes up, not the Jetty one.
     * @return - the factory
     */
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory()
    {
        return new TomcatEmbeddedServletContainerFactory();
    }
    
    
    
    /**
     * Spin up a CXFServlet and register the url beyond which CXF will parse and direct traffic to
     * Predix in CF uses "services" plural as the standard URL.  
     * 
     * @return -
     */
    @SuppressWarnings("nls")
    @Bean
    public ServletRegistrationBean servletRegistrationBean()
    {
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");
    }
    
    
    /**
     * @param request -
     * @param name -
     * @param model -
     * @return -
     */
    @RequestMapping("/")
    public String greetings(HttpServletRequest request ,@RequestParam(value = "name", required = false, defaultValue = "Predix") String name,
            Model model)
    {   StringBuffer requesturi = request.getRequestURL();
        String applicationURl = requesturi.toString().replaceAll("http", "https");//$NON-NLS-1$ //$NON-NLS-2$ 
        if("local".equalsIgnoreCase(this.profile)) { //$NON-NLS-1$
            applicationURl = requesturi.toString(); // localhost support for http
        }
        model.addAttribute("api",applicationURl.toString()+"api");//$NON-NLS-1$ //$NON-NLS-2$ 
        model.addAttribute("health",applicationURl.toString()+"health");//$NON-NLS-1$ //$NON-NLS-2$ 
        model.addAttribute("docs",this.docsUrl);//$NON-NLS-1$ 
        model.addAttribute("compressionratio",applicationURl.toString()+"services/windservices/yearly_data/sensor_id/Compressor-2015:CompressionRatio");//$NON-NLS-1$ 
         
        return "index"; //$NON-NLS-1$
    }
    
    /**
     * @param request -
     * @param response -
     * @throws IOException -
     */
    @RequestMapping("/api")
    public @ResponseBody void api(HttpServletRequest request ,HttpServletResponse response ) throws IOException
    {   String applicationURl = getApplicationUrl(request);
    response.sendRedirect(applicationURl.replace("/api", "/swagger-api/index.html?url=/services/swagger.json")); //$NON-NLS-1$//$NON-NLS-2$

    }
   
    
    /**
     * @param request -
     * @param response -
     * @throws IOException -
     */
    @RequestMapping("/health")
    public @ResponseBody void health(HttpServletRequest request ,HttpServletResponse response ) throws IOException
    {  
        
        String applicationURl = getApplicationUrl(request);
        response.sendRedirect(applicationURl.replace("/health", "/services/health")); //$NON-NLS-1$ //$NON-NLS-2$

    }
    
    /**
     * 
     * @param request
     * @return - Application URL
     */
    private String getApplicationUrl (final HttpServletRequest request){
   
        String applicationURl = request.getRequestURL().toString().replaceAll("http", "https");//$NON-NLS-1$ //$NON-NLS-2$ 
        if("local".equalsIgnoreCase(this.profile)) { //$NON-NLS-1$
            applicationURl = request.getRequestURL().toString(); // localhost support for http
        }
        return applicationURl;
    }
    
}
