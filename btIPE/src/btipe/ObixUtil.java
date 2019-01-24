package btipe;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;

public class ObixUtil {

	public static String getbtRep(int value) {
	//	String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + Monitor.btId;
		Obj obj = new Obj();
		obj.add(new Int("data", value));
		return ObixEncoder.toString(obj);
	}


}
