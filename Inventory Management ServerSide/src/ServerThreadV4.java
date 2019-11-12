import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerThreadV4 implements Runnable
{

	private Socket cSocket; 
	public boolean listen = true,found=false;
	private String[] id,update,updatedItem;
	boolean firstTime=true;
	private DLinkedList<Inventory> dataList;
	private Inventory tempItem;
	private StringBuffer loadData;
	private int indexToChange,qtySold,qtyRestocked;
	
	public ServerThreadV4(Socket cSocket,DLinkedList<Inventory> dataList) 
	{
		this.cSocket = cSocket;
		this.dataList=dataList;
	}
	
	@Override
	public void run() {
		BufferedReader in;
		try 
		{
			//System.out.println("Sending message to client - " + (new Date()).toString());
			PrintWriter out = new PrintWriter(cSocket.getOutputStream(), true);
			//out.println("Welcome! I'm here to listen only");
			in = new BufferedReader(new InputStreamReader(cSocket.getInputStream())); 
			String clientMessage;
			clientMessage = in.readLine();
			System.out.println("The client said: " + clientMessage +  " [Recv: " + (new Date()).toString() + "] ");
				 
				 if (clientMessage.startsWith("checkPrice"))
				 {
					 synchronized (this)
					 {
					 try {
					 id=clientMessage.split(" ");
					int result= BinarySearchByID.binarySearch(dataList,id[1]);
			        
					if (result == -1)
			        out.println("Enter correct id");
			         else
			         {
			        	 out.println(String.format("$%.2f",dataList.get(result).get_UnitPrice()));
			         }
					 }
					 catch(Exception e) {
						 out.println("Enter id in Correct Syntax");
					 }
					 }
				 }
				 
				 else if(clientMessage.equalsIgnoreCase("LoadData"))
				 {
					 synchronized (this)
					 {
				 loadData=new StringBuffer();
					 for(int i=0;i<dataList.size();i++)
					 {
						 loadData.append(dataList.get(i).get_ID()+","+dataList.get(i).get_ItemName()+","+dataList.get(i).get_StartingQty()+","+
								 dataList.get(i).get_QtyMinRestock()+","+dataList.get(i).get_QtySold()+","+dataList.get(i).get_QtyRestocked()
								 +","+dataList.get(i).get_UnitPrice()+"<>");
					 }
				 out.println(loadData.toString());
					 }
				 }
				 else if(clientMessage.startsWith("updateQtySold")) 
				 {
					 synchronized (this)
					 {
					 update=clientMessage.split("<");
					 indexToChange=Integer.parseInt(update[1]);
					 qtySold=Integer.parseInt(update[2]);
					 updatedItem=update[3].split(",");
					 
					 tempItem=dataList.get(indexToChange);
					 if(Double.parseDouble(updatedItem[7]) !=tempItem.get_QtyHand()) 
					 {
						 //display unusual changes about this item made by someone else
						 out.println("unsual changes");
					 }
					 else 
					 {
						 tempItem.set_QtySold(tempItem.get_QtySold()+qtySold);
						 tempItem.set_QtyHand(tempItem.get_QtyHand()-qtySold);
						 tempItem.set_Sales(tempItem.get_UnitPrice()*tempItem.get_QtySold()); 
						 out.println(tempItem.get_QtySold()+","+tempItem.get_QtyHand()+","+tempItem.get_Sales());
					 }
					
					 }
				 }
				 else if(clientMessage.startsWith("updateRestocked")) 
				 {
					 synchronized (this)
					 {
					 update=clientMessage.split("<");
					 indexToChange=Integer.parseInt(update[1]);
					 qtyRestocked=Integer.parseInt(update[2]);
					 updatedItem=update[3].split(",");
					 
					 tempItem=dataList.get(indexToChange);
					 if(Double.parseDouble(updatedItem[7]) !=tempItem.get_QtyHand()) 
					 {
						 //display unusual changes about this item made by someone else
						 out.println("unsual changes");
					 }
					 else 
					 {
						 tempItem.set_QtyRestocked(tempItem.get_QtyRestocked()+qtyRestocked);
						 tempItem.set_QtyHand(tempItem.get_QtyHand()+qtyRestocked);
						 out.println(tempItem.get_QtyRestocked()+","+tempItem.get_QtyHand());
					 }
					 }
				 }
				 else if(clientMessage.startsWith("Delete")) 
				 {
					 synchronized (this)
					 {
					 update=clientMessage.split("<");
					 indexToChange=Integer.parseInt(update[1]);
					  
					 tempItem=dataList.get(indexToChange);
					 if(Double.parseDouble(update[2]) !=tempItem.get_QtyHand()) 
					 {
						 //display unusual changes about this item made by someone else
						 out.println("unsual changes");
					 }
					 else 
					 {
						 dataList.remove(indexToChange);
						 out.println("Deleted the Item");
					 }
					 }
				 }
				 else if(clientMessage.startsWith("SaveInventoryToFile")) 
				 {
					 synchronized (this)
					 {
						 FileWriter fwriter = new FileWriter("./inventory_ip123456.csv");
						 BufferedWriter bw = new BufferedWriter (fwriter);
						 bw.write("Id,Item Name,StartingQty,QtyMinRestck,QtySold,QtyRStcked,UnitPrice");
					 for(int i=0;i<dataList.size();i++) 
						 {
							 bw.newLine(); 
							 bw.write(dataList.get(i).get_ID()+","+dataList.get(i).get_ItemName()+","+dataList.get(i).get_StartingQty()
										 +","+dataList.get(i).get_QtyMinRestock()+","+dataList.get(i).get_QtySold()+","+
										 dataList.get(i).get_QtyRestocked()
										 +","+dataList.get(i).get_UnitPrice());
								
						
						 }
						 bw.close();
						 out.println("Saved changes to the file");
						
					 }
				 }
	        in.close();
	        out.close();
	        cSocket.close();
	        System.out.println("Server Thread done");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
        
        
		

		
	}

	

}
