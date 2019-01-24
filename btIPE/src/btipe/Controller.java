package btipe;

import java.io.IOException;

import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.json.simple.parser.ParseException;




public class Controller implements InterworkingService {
	//	MyServer m = Monitor.mServer;
	int i=0;
	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		ResponsePrimitive response = new ResponsePrimitive(request);

		if (request.getRequestContentType().equals(MimeMediaType.JSON)){
		
			System.out.println("\n=====NHAN DUOC TU OM2M======\n" + request.getContent().toString());
	
					if (Monitor.check==1) {
						try {
							try {
								Monitor.writetophone(request.getContent());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			
		
		

			response.setResponseStatusCode(ResponseStatusCode.OK);
			/*		
			if (Monitor.check==1) {

					String test = con;

				try {
					Monitor.writetophone(test);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 */	




			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		}
		@Override
		public String getAPOCPath() {
			return Monitor.ipeId;
		}
	}
