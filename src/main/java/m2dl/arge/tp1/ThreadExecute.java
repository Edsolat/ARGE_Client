package m2dl.arge.tp1;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

public class ThreadExecute extends Thread {
	XmlRpcClient client;
	
	public ThreadExecute(XmlRpcClient client) {
		this.client = client;
	}
	
	
	public void run() {
		//int waitTime = 1000 / Client.nbRequest;
		
		Integer result = 0;
		Object[] params = null;
		
        // make the a regular call
        params = new Object[] { new Integer(Client.n) };
        
        while(true) {
	        for (int i = 0; i < Client.nbRequest ; i++) {
				try {
					result = (Integer) client.execute("Redirect.div", params);
					System.out.println(Client.nbRequest + " --> " + result);
					
				} catch (XmlRpcException e) {
					e.printStackTrace();
					System.out.println("Error while connecting to calculator server...");
					break;
				}
	        }
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
	}

}
