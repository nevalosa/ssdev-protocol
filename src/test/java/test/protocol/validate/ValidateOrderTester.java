package test.protocol.validate;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import ctd.controller.exception.ControllerException;
import ctd.protocol.message.validate.ValidateStatus;
import ctd.protocol.message.xml.XMLMessage;
import ctd.protocol.message.xml.XMLMessageFactory;
import ctd.protocol.schema.controller.SegmentController;
import ctd.protocol.schema.support.container.Message;
import ctd.protocol.schema.support.container.Segment;
import ctd.resource.ResourceCenter;
import ctd.util.context.ContextUtils;
import ctd.util.context.beans.DateBean;
import ctd.util.xml.XMLHelper;
import junit.framework.TestCase;

public class ValidateOrderTester extends TestCase {
	private XMLMessage xmlMsg;
	
	
	public ValidateOrderTester() throws ControllerException{
		Message schema = (Message)SegmentController.instance().get("test.protocol.message.Message3");
		assertNotNull(schema);
		
		ContextUtils.put("date", new DateBean());
		xmlMsg = XMLMessageFactory.createMessage(schema);
		assertNotNull(xmlMsg);
	}
	
	public void testOrderValidate() throws ControllerException, IOException, DocumentException{
		Resource r = ResourceCenter.load("test/protocol/validate/message3Data.xml");
		Document doc = XMLHelper.getDocument(r.getInputStream());
		assertNotNull(doc);
		
		Segment s = (Segment)xmlMsg.getElement();
		System.out.println(s.getOrder());
		
		xmlMsg.addData(doc.getRootElement());
		System.out.println(xmlMsg.getData().asXML());
		
		System.out.println(xmlMsg.child("reqDt").getValue());
		
		assertEquals(3, xmlMsg.child("item").getGroupCount());
		
		ValidateStatus status = xmlMsg.validate();
		System.out.println(status);
		
	}
	
	public void testOrderValidate100000() throws ControllerException, IOException, DocumentException{
		Resource r = ResourceCenter.load("test/protocol/validate/message3Data.xml");
		Document doc = XMLHelper.getDocument(r.getInputStream());
		assertNotNull(doc);

		//xmlMsg.addData(doc.getRootElement());
	
		for(int i = 0; i < 1000000; i++){
			xmlMsg.addData(doc.getRootElement());
			ValidateStatus status = xmlMsg.validate();
			xmlMsg.clear();
		}
		
	}
	
	public void testOrderValidateXSD100000() throws ControllerException, IOException, DocumentException, SAXException{
		Resource r = ResourceCenter.load("test/protocol/validate/message3Data.xml");
		Document doc = XMLHelper.getDocument(r.getInputStream());
		String docStr = doc.asXML();
		
		
		 Source schemaFile = new StreamSource(
				 Thread. currentThread ().getContextClassLoader().getResourceAsStream( "test/protocol/validate/message3.xsd")
		 );
		 SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		 Schema schema = factory.newSchema(schemaFile);
         Validator validator = schema.newValidator();
         
         for(int i = 0 ; i < 1000000; i ++){
        	 validator.validate(new StreamSource(new StringReader(docStr)));
         }
	}
}
