package ctd.protocol.message.xml;

import ctd.protocol.message.exception.MessageException;
import ctd.protocol.message.xml.support.AnyElement;
import ctd.protocol.message.xml.support.ComplexElement;
import ctd.protocol.message.xml.support.ComplexElementGroup;
import ctd.protocol.message.xml.support.SingleElement;
import ctd.protocol.message.xml.support.SingleElementGroup;
import ctd.protocol.schema.Element;
import ctd.protocol.schema.support.AnyEntry;
import ctd.protocol.schema.support.Entry;
import ctd.protocol.schema.support.container.Segment;

public class XMLMessageFactory {
	
	public static XMLMessage createMessage(Element el){
		if(el instanceof Entry){
			if(el instanceof AnyEntry){
				return new AnyElement(el);
			}
			
			if(el.getRepetition() > 1){
				return new SingleElementGroup(el);
			}
			else{
				return new SingleElement(el);
			}
		}
		
		if(el instanceof Segment){
			if(el.getRepetition() > 1){
				return new ComplexElementGroup(el);
			}
			else{
				return new ComplexElement(el);
			}
		}
		
		throw new MessageException("element class error");
	}
}