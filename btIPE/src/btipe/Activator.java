package btipe;

import java.io.IOException;

import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private Monitor monitor; // monitor lay thong tin tu thiet bi bluetooth
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		bundleContext.registerService(InterworkingService.class.getName(), 
				new Controller(), null); // controller nhan thong tin tra ve tu om2m

		Activator.context = bundleContext;

		System.out.println("=====Pluggin Bluetooth start=====");

		new ServiceTracker<Object,Object>(bundleContext,
				CseService.class.getName(),null) {
			public Object addingService(org.osgi.framework.ServiceReference<Object> reference) {
				final CseService cse=(CseService) this.context.getService(reference);

				if(cse!=null){
					RequestSender.CSE=cse; // requestsender gui thong tin len om2m


					new Thread(){
						public void run(){
							if (monitor==null){
								monitor = new Monitor(cse);
								try {
									monitor.start();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}.start();
				}
				return cse;
			}

			public void removedService(org.osgi.framework.ServiceReference<Object> reference, Object service) {
				if (monitor!=null){
					monitor.stop();
					monitor=null;
				}

			};
		}.open(); 
	}


	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		
		System.out.println("======Stop IPE bluetooth======");
		if (monitor!=null){
			monitor.stop();
			monitor=null;
		}
	}

}
