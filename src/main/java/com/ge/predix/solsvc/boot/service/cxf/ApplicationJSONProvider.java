package com.ge.predix.solsvc.boot.service.cxf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * * JSONProvider, which unmarshals and marshals Request and Responses from CXF. 
 * 
 * This Provider leverages Jackson and allows for handling of JSON or XML bodies to unmarshal to Objects.
 * If using JAXB to generate classes, it puts an @XmlSeeAlso annotation in order to handle Polymorphic classes such as 
 * DataEvent and SelectionFilter.  For example, if the Rest API has an Animal class and the client passes 
 * Cat, Dog, or Goat, the register subtypes method will register the proper mapping.
 * 
 * In this example, a DMDataSeq extends DMDataEvent extends DataEvent.  Yet, the API allows any kind of DataEvent.
 * {"dataEvent":{"@type":"DMDataSeq","alertStatus":null,"confid":null,"id":0,"sequenceNum":1,"site":null,"time":null,"dataStatus":null,"numAlerts":[],"values":[52.1],"valuesList":[],"valuesBinary":null,"xaxisDeltasList":[],"xaxisDeltasBinary":null,"xaxisStart":1.413933112861E12,"xaxisDeltas":[1.413933112861E12]},"engUnit":{"abbrev":null,"code":0,"dbId":0,"name":null,"refUnit":null,"site":null,"unitConv":null},"selectionFilter":[]}
 * 
 * By adding the "@type":"DMDataSeq" pair to object, this is all that is needed to know what type of DataEvent the Provider 
 * should unmarshal to.
 * 
 * the @type annotation can be automatically placed in the JSON simply by placing this annotation on the Parent class DataEvent.
 * @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type", include = JsonTypeInfo.As.PROPERTY)
 * public class DataEvent
 * 
 * In the cases where you define your contracts in XSD.  The Annotation can be placed on the XSD as follows, by putting on the annox:annotate element:
 * <!-- Add these at the top of the file --> xmlns:annox="http://annox.dev.java.net" xmlns:ja="http://annox.dev.java.net/org.codehaus.jackson.annotate" jaxb:extensionBindingPrefixes="xjc annox" <!-- Add the annox:annotate entry in the parent object --> <xs:complexType name="DataEvent"> <xs:annotation> <xs:appinfo> <annox:annotate> <!-- <ja:JsonTypeInfo use="CLASS" include="PROPERTY" property="@class"/> -->   <ja:JsonTypeInfo use="NAME" include="PROPERTY" property="@type"/>   </annox:annotate> </xs:appinfo> </xs:annotation> <xs:sequence> <xs:element name="alertStatus" type="xs:boolean" minOccurs="0"/> <xs:element name="confid" type="xs:float" minOccurs="0"/> <xs:element name="id" type="xs:unsignedInt"/> <xs:element name="sequenceNum" type="xs:unsignedInt" minOccurs="0"/> <xs:element name="site" type="tns:Site" minOccurs="0"/> <xs:element name="time" type="tns:OsacbmTime" minOccurs="0"/> </xs:sequence> </xs:complexType>
 *   
 * Here is an example of a maven plugin that can autogenerate the Java classes from XSD
 * <plugin> <groupId>org.jvnet.jaxb2.maven2</groupId> <artifactId>maven-jaxb2-plugin</artifactId> <version>${maven-jaxb2-plugin.version}</version> <executions> <execution> <goals> <goal>generate</goal> </goals> </execution> </executions> <configuration> <args> <arg>-Xcollection-setter-injector</arg> <arg>-XtoString</arg> <arg>-Xequals</arg> <arg>-XhashCode</arg> <arg>-Xannotate</arg> </args> <plugins> <plugin> <groupId>net.java.dev.vcc.thirdparty</groupId> <artifactId>collection-setter-injector</artifactId> <version>${collection-setter-injector.version}</version> </plugin> <plugin> <groupId>org.jvnet.jaxb2_commons</groupId> <artifactId>jaxb2-basics</artifactId> <version>${jaxb2-basics-runtime.version}</version> </plugin> <plugin> <groupId>org.jvnet.jaxb2_commons</groupId> <artifactId>jaxb2-basics-annotate</artifactId> <version>0.6.3</version> </plugin> </plugins>     <extension>true</extension> <forceRegenerate>false</forceRegenerate> <schemaDirectory>src/main/resources/META-INF/schemas</schemaDirectory> <generateDirectory>src/main/java/</generateDirectory> <strict>false</strict> <debug>true</debug> <verbose>true</verbose> </configuration> <dependencies> <dependency> <groupId>org.codehaus.jackson</groupId> <artifactId>jackson-xc</artifactId> <version>${jackson-xc.version}</version> <scope>compile</scope> </dependency> </dependencies> </plugin>
 * 
 * @author tturner

 */
/**
 * @author predix
 *
 */
public class ApplicationJSONProvider extends JacksonJsonProvider
{
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 
     */
    public ApplicationJSONProvider()
    {
        super(mapper, BASIC_ANNOTATIONS);
        List<Class<?>> classesToEvaluate = new ArrayList<Class<?>>();
        // fdh
        // classesToEvaluate.add(Identifier.class);
        // classesToEvaluate.add(Data.class);
        // classesToEvaluate.add(SelectionFilter.class);
        // classesToEvaluate.add(GroupBy.class);
        // some OSA objects to be backwards compatible
        // classesToEvaluate.add(Value.class);
        // classesToEvaluate.add(DataEvent.class);
        // classesToEvaluate.add(org.mimosa.osacbmv3_3.SelectionFilter.class);

        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        Class<?>[] classes = getSubtypes(classesToEvaluate);
        mapper.registerSubtypes(classes);
    }

    /**
     * @param classToEvaluate
     * @return
     */
    private Class<?>[] getSubtypes(List<Class<?>> classesToEvaluate)
    {
        List<Class<?>> subtypes = new ArrayList<Class<?>>();
        for (Class<?> classToEvaluate : classesToEvaluate)
        {
            subtypes.addAll(getSubtypes(null, classToEvaluate));
        }
        Class<?>[] classes = new Class<?>[subtypes.size()];
        int i = 0;
        for (Class<?> clz : subtypes)
        {
            classes[i++] = clz;
        }
        return classes;
    }

    /**
     * @return
     * 
     */
    @SuppressWarnings("nls")
    private List<Class<?>> getSubtypes(List<Class<?>> resultArg, Class<?> classToEvaluate)
    {
        {
            List<Class<?>> result = resultArg;
            for (Annotation annotation : classToEvaluate.getAnnotations())
            {
                if ( annotation.annotationType().equals(XmlSeeAlso.class) )
                {
                    if ( result == null ) result = new ArrayList<Class<?>>();
                    XmlSeeAlso xmlSeeAlso = classToEvaluate.getAnnotation(XmlSeeAlso.class);
                    for (Class<?> clz : xmlSeeAlso.value())
                    {
                        result.add(clz);
                        if ( clz.getAnnotation(XmlSeeAlso.class) != null )
                        {
                            result = getSubtypes(result, clz);
                        }
                    }
                }
            }
            if ( result == null )
                throw new UnsupportedOperationException("classToEvaluate=" + classToEvaluate
                        + " has no children so please remove it from the list");
            return result;

        }

    }

    @Override
    public void writeTo(Object arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4,
            MultivaluedMap<String, Object> arg5, OutputStream arg6)
            throws IOException
    {
        super.writeTo(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException
    {
        return super.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

}
