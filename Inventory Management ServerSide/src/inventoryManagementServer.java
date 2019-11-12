import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.ServerSocket;

public class inventoryManagementServer {

	public static void main(String[] args) 
	{
		InetAddress ip;
		final int PORT =8888;
		try 
		{
			ip = InetAddress.getLocalHost();
			System.out.println("IP address of the server: " + ip.getHostAddress());
			ServerSocket serverSocket = new ServerSocket(PORT);
            ServerWaitingClient swc = new ServerWaitingClient(serverSocket);
            Thread tr = new Thread(swc);
            tr.start();
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();
		}
        

	}

}
