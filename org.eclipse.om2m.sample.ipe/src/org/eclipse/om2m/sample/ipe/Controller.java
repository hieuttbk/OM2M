package org.eclipse.om2m.sample.ipe;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;
 
public class Controller implements InterworkingService{
	private static Log LOGGER = LogFactory.getLog(Controller.class);

	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		ResponsePrimitive response = new ResponsePrimitive(request);

		String[] parts = request.getTo().split("/");
		LOGGER.info("\n>>>>>>>>>>>>>>>>>>>>>NHAN DUOC TU OMM2M>>>>>>>>>>>>>>>>>>>>>>>>\n" 
				+ "request== \n"
				+  request.toString());
		
		LOGGER.info("\n>>>>>>>>>>>>>>>>>>>>>CONTENT CUA BAN TIN NHAN DUOC>>>>>>>>>>>>>>>>>>>>>>>>\n" 
				+ "content== \n"
			+  request.getContent());
		
		data=resquest.getContent()
		
		
		
		response.setResponseStatusCode(ResponseStatusCode.OK);

		
		//LOGGER.info("Xem nhan dc cai gi o day" + request.getTo());
		//LOGGER.info("Xem nhan dc cai gi o day" + request.getTo().split("/"));


		String appId = parts[1]; // get to name=sensor|actuator
	//	LOGGER.info("\n Xem nhan dc cai gi o day" + appId);
		
 
		if(request.getQueryStrings().containsKey("op")){
			String valueOp = request.getQueryStrings().get("op").get(0);
 
			switch(valueOp){
			case "get":
				LOGGER.info("Vao get 1" + appId);

				if(appId.equals(Monitor.sensorId)){
					
					LOGGER.info("Vao get 2 " + appId);

					response.setContent(ObixUtil.getSensorDataRep(Monitor.sensorValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Monitor.actuatorId)){
					response.setContent(ObixUtil.getActuatorDataRep(Monitor.actuatorValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "true": case "false":
				if(appId.equals(Monitor.actuatorId)){
					Monitor.actuatorValue = Boolean.parseBoolean(valueOp);
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
		} else {
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}
 
	@Override
	public String getAPOCPath() {
		return Monitor.ipeId;
	}
 
}