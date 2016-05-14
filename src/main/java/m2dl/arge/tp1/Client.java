package m2dl.arge.tp1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class Client {
	protected static int nbRequest, n;
	private String ip;
	private int port;
	private XmlRpcClient client;


	public Client(int nbRequest, String ip, int port) {
		Client.nbRequest = nbRequest;
		this.ip = ip;
		this.port = port;
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		Client.n = Integer.parseInt(bundle.getString("n"));
		

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

		try {
			config.setServerURL(new URL("http://" + this.ip + ":" + this.port + "/xmlrpc"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config.setEnabledForExtensions(true);  
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		this.client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(
				new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);
		
		new ThreadExecute(client).start();
	}

	private void launchClient() throws IOException, XmlRpcException {
		WebServer webServer = new WebServer(13000);

		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

		PropertyHandlerMapping phm = new PropertyHandlerMapping();

		phm.load(Thread.currentThread().getContextClassLoader(),
				"XmlRpcServlet.properties");
		phm.addHandler("Update",
				Update.class);

		xmlRpcServer.setHandlerMapping(phm);

		XmlRpcServerConfigImpl serverConfig =
				(XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
		serverConfig.setEnabledForExtensions(true);
		serverConfig.setContentLengthOptional(false);

		webServer.start();
	}


	public static void main(String[] args) {
		int nbReq = Integer.parseInt(args[0]);
		int port = Integer.parseInt(args[2]);
		Client c = new Client(nbReq, args[1], port);
		try {
			c.launchClient();
			System.out.println("Sucess to launch client on port " + 13000);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (XmlRpcException e1) {
			e1.printStackTrace();
		}
	}


}
