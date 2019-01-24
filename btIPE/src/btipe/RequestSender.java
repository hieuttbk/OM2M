package btipe;

import java.math.BigInteger;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.Operation;
import org.eclipse.om2m.commons.constants.ResourceType;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.Resource;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.commons.resource.Subscription;
import org.eclipse.om2m.core.service.CseService;

public class RequestSender {

	public static CseService CSE;
	
	
	private RequestSender() {}
	public static ResponsePrimitive createResource(String targetId, Resource resource, int resourceType){
		
		
		
		RequestPrimitive request = new RequestPrimitive();
		request.setFrom(Constants.ADMIN_REQUESTING_ENTITY); // fr: admin:admin
//		targetId=targetId + "/" + name; // name is not using because targetID has added "+/name" before
		request.setTo(targetId);
		request.setResourceType(BigInteger.valueOf(resourceType)); // ty 2|3|4= ResourceType.AE|CONTAINER|CONTENT_INSTANCE
		request.setRequestContentType(MimeMediaType.OBJ); // maybe: application/json/xml/obj
	//	request.setReturnContentType(MimeMediaType.OBJ);
		request.setContent(resource); // content = resource is MimeMediaType.OBJ
		request.setOperation(Operation.CREATE); // op = CRUD+N
		request.setReturnContentType(MimeMediaType.JSON); // ban tin subscription can cai nay
		
		System.out.println("\n\n\n=====BAN TIN GUI DI: " + request.getContent().toString() );
		
		
		/**
		 * subscriptionEntity.setNotificationPayloadContentType(request.getReturnContentType());
		*/
		
		
		/**
		 * SEND request to OM2M
		 * Sends a RequestPrimitive object to the CSE and returns back the ResponsePrimitive object
		 */		
		return CSE.doRequest(request); // sent to OM2M
	}

	public static ResponsePrimitive createAE(AE ae) {
		return createResource("/" + Constants.CSE_ID, ae, ResourceType.AE);
	}
	public static ResponsePrimitive createrContainer(String targetId, Container cnt) {
		return createResource(targetId, cnt, ResourceType.CONTAINER);
		
	}
	public static ResponsePrimitive createContentInstance(String targetId,
			ContentInstance cin) {
		return createResource(targetId, cin, ResourceType.CONTENT_INSTANCE);
		
	}
	public static ResponsePrimitive createSub(String targetId, Subscription sub) {
		return createResource(targetId, sub, ResourceType.SUBSCRIPTION);

	}

}
