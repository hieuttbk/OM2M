package org.eclipse.om2m.sample.ipe;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	private static Log LOGGER = LogFactory.getLog(Controller.class);

	public static CseService CSE;

	/**
	 * Private constructor to avoid creation of this object
	 */
	private RequestSender(){}

	public static ResponsePrimitive createResource(String targetId, String name, Resource resource, int resourceType){

		RequestPrimitive request = new RequestPrimitive();

		request.setFrom(Constants.ADMIN_REQUESTING_ENTITY); // fr: admin:admin

		targetId=targetId + "/" + name; // name is not using because targetID has added "+/name" before
		request.setTo(targetId);

		request.setResourceType(BigInteger.valueOf(resourceType)); // ty 2|3|4= ResourceType.AE|CONTAINER|CONTENT_INSTANCE

		request.setRequestContentType(MimeMediaType.OBJ); // maybe: application/json/xml/obj

		request.setReturnContentType(MimeMediaType.OBJ);

		request.setContent(resource); // content = resource is MimeMediaType.OBJ
		//	request.setName(name);
		request.setOperation(Operation.CREATE); // op = CRUD+N
		
		request.setReturnContentType(MimeMediaType.JSON); // ban tin subscription can cai nay
		/**
		 * subscriptionEntity.setNotificationPayloadContentType(request.getReturnContentType());
		*/
		
		LOGGER.info("\n========================BAN TIN GUI DI============================\n" 
				+ "ty= " + resourceType 	
				+ "\n setContent(resource) = " + resource
				+ "\n Request== "
				+ request.toString()
		
				);


		/**
		 * SEND request to OM2M
		 * Sends a RequestPrimitive object to the CSE and returns back the ResponsePrimitive object
		 */		
		return CSE.doRequest(request); // sent to OM2M
	}

	public static ResponsePrimitive createAE(AE resource, String name){
		return createResource("/" + Constants.CSE_ID,name, resource, ResourceType.AE);
	}
	
	public static ResponsePrimitive createSub(Subscription sub, String targetID) {
		return createResource(targetID,"", sub, ResourceType.SUBSCRIPTION);
	}
	
	public static ResponsePrimitive createContainer(String targetId, String name, Container resource){
		//	resource.setName(name);
		return createResource(targetId, name, resource, ResourceType.CONTAINER);
	}

	public static ResponsePrimitive createContentInstance(String targetId, ContentInstance resource){
		return createResource(targetId,"", resource, ResourceType.CONTENT_INSTANCE);
	}
	

	//	public static ResponsePrimitive createContentInstance(String targetId, ContentInstance resource){
	//		return createContentInstance(targetId, resource);
	//	}

	public static ResponsePrimitive getRequest(String targetId){
		RequestPrimitive request = new RequestPrimitive();
		request.setFrom(Constants.ADMIN_REQUESTING_ENTITY);
		request.setTargetId(targetId);
		request.setReturnContentType(MimeMediaType.OBJ);
		request.setOperation(Operation.RETRIEVE);
		request.setRequestContentType(MimeMediaType.OBJ);
		return CSE.doRequest(request);
	}

	



}