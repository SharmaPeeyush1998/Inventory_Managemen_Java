import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerWaitingClient implements Runnable {

	private ServerSocket sSocket;
	boolean firstTime=true;
	private DLinkedList<Inventory> dataList =new DLinkedList<Inventory>();
	public ServerWaitingClient(ServerSocket sSocket) 
	{
		this.sSocket = sSocket;
		try 
		 {
		 BufferedReader br = new BufferedReader(new FileReader("./inventory_ip123456.csv"));
		 String data;
		 String[] data1;
		 
			while ((data = br.readLine())!= null)
			 {
				 if(firstTime)
				 {
					 firstTime=false;
				 }
				 else 
				 {
				data1=data.split(",");
				dataList.add(new Inventory(data1[0], data1[1], data1[2], data1[3], data1[4], data1[5], data1[6]));
				 }
			 }
			 br.close();
			 
			 
			 
			 //Sorting the double linked list
			 QuickSortByID.quickSort(dataList, 0, dataList.size()-1);
		}
		 catch (Exception e) 
		 {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		Socket clientSocket = null;
		ServerThreadV4 st;
		Thread t;
	
		try {
			while (true) {
				System.out.println("Waiting for client - " + (new Date()).toString());
				clientSocket = sSocket.accept();
				
				System.out.println("Got a client - " + (new Date()).toString());
				    
				st = new ServerThreadV4(clientSocket,dataList) ;
				t = new Thread(st);
				t.start();  // let the thread listen to what the client wants to say
			}
			
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			System.out.println("ServerWaitingClient done");
		}
	}		


}
