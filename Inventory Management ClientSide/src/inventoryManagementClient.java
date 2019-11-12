import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.DataTruncation;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class inventoryManagementClient extends JFrame  
{

	private JPanel getPricePanel,LoadData,listView,updateQtySoldPanel,updateQtyRestockPanel,deletePanel,saveDataPanel;
	private JTextField itemIdtf,itemPricetf,updateQtySoldtf,updateQtyRestockedtf;
	private JList<String> list;
	private JScrollPane listScroller;
	private DefaultListModel<Inventory> listModel;
	private JLabel itemId,itemPrice,Name,qtySoldLabel,qtyRestockLabel,DisplayMessage;
	private JButton checkPrice,btnLoadData,updateQtySold,updateQtyRestocked,deleteSelectedInventoryItem,saveInventoryData,saveSalesReport,saveRestockReport,saveServerFile;
	final int PORT = 8888;
	String localHost="10.1.104.84";
	String [] DataToLoadtoInventory,DataToLoad;
	private BufferedReader serverInput = null ;
	private PrintWriter pw = null;
	private Socket connectionSock = null;
	int selectedIndex;
	Inventory tempSelectedItem;
	String updateQty,updateRestockedQty,saveDirectory;
	
	public static void main(String[] args) 
	{
		new inventoryManagementClient();
	}
	
	public inventoryManagementClient() 
	{
		this.setTitle("Arrow Inventory Management Systems");
		this.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//Getting the screen Size
		double width = screenSize.getWidth() * .8;//Width of the app to be .6 times the screen
		double height = screenSize.getHeight() * .64;//Height of the app to be .6 times the screen
		this.setBounds((int)screenSize.getWidth()/2-(int)width/2,((int)screenSize.getHeight()/2)-(int)height/2,(int)width,(int)height);//To display the frame in the centre of the screen 
		this.setLayout(null);
		
		//Check Price Panel
		getPricePanel=new JPanel();
		getPricePanel.setBorder(BorderFactory.createTitledBorder("Get Item Price"));
		attach(getPricePanel, 10, 10, 310, 120);
		
		itemIdtf=new JTextField(20);
		itemId=new JLabel("Item Id       ");
		
		itemPricetf=new JTextField(20);
		itemPricetf.setText("$0.00");
		itemPrice=new JLabel("Item Price");
		itemPricetf.setEditable(false);
		
		checkPrice=new JButton("Check Price");
		
		getPricePanel.add(itemId);
		getPricePanel.add(itemIdtf);
		getPricePanel.add(itemPrice);
		getPricePanel.add(itemPricetf);
		getPricePanel.add(checkPrice);
		
		getPricePanel.validate();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		checkPrice.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{

				try 
				{
					if(itemIdtf.getText().equalsIgnoreCase(""))
					{
						DisplayMessage.setText("Please input the ID for the item you want to check price for");
					}
					else 
					{
						String k;
					connectionSock = new Socket(localHost,PORT);
					serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
					pw = new PrintWriter(connectionSock.getOutputStream(),true);
					pw.println("checkPrice "+itemIdtf.getText().toString());
					itemPricetf.setText(k=serverInput.readLine());
					DisplayMessage.setText(k);
					}
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				} 			
			}
		});
		
		//Load Data Panel
		LoadData=new JPanel(new GridBagLayout());
		LoadData.setBorder(BorderFactory.createTitledBorder("Load Data"));
		attach(LoadData, 330, 10, 150, 120);
		GridBagConstraints gbc = new GridBagConstraints();
		btnLoadData=new JButton("Load Data");
		LoadData.add(btnLoadData,gbc);
		LoadData.validate();
		
		//List of Inventory Items Panel
		listView=new JPanel(new FlowLayout(FlowLayout.LEFT));
		listView.setBorder(BorderFactory.createTitledBorder("List Of Inventory Items"));
		attach(listView, 10, 140, 1300, 455);
		Name=new JLabel(String.format("%-15s%-25s%-15s%-18s%-13s%-16s%-15s%-13s%-11s", "ID","Item Name","Starting Qty",
			"Qty Min Restock","Qty Sold","Qty Restocked","Unit Price","Qty Hand","Sales"));
		Name.setFont(new Font("monospaced", Font.PLAIN, 15));
		
		listView.add(Name);
		  	listModel = new DefaultListModel<>();
	        list=new JList(listModel);
	        list.setCellRenderer(new WhiteYellowCellRenderer());
	        list.setFont(new Font("monospaced", Font.PLAIN, 15));
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			listScroller=new JScrollPane(list);
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			listScroller.setPreferredSize(new Dimension(1280,400));
			listView.add(listScroller);
			listView.validate();
		btnLoadData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
				listModel.removeAllElements();
				connectionSock = new Socket(localHost,PORT);
				serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
				pw = new PrintWriter(connectionSock.getOutputStream(),true);
				pw.println("LoadData");
				DataToLoad=serverInput.readLine().split("<>");
				for(String s:DataToLoad) 
				{
					DataToLoadtoInventory=s.split(",");
					listModel.addElement(new Inventory(DataToLoadtoInventory[0], DataToLoadtoInventory[1], DataToLoadtoInventory[2], DataToLoadtoInventory[3], DataToLoadtoInventory[4], DataToLoadtoInventory[5], DataToLoadtoInventory[6]));
				}
				DisplayMessage.setText("Loaded the Items in the Inventory");
				}
				catch(Exception e1)
				{
					DisplayMessage.setText(e1.getMessage());
				}
			}
		});
		
		//DisplayLabel
		DisplayMessage=new JLabel("Welcome");
		DisplayMessage.setHorizontalAlignment(JLabel.CENTER);
		DisplayMessage.setOpaque(true);
		DisplayMessage.setBackground(Color.yellow);
		DisplayMessage.setFont(new Font(DisplayMessage.getName(), Font.PLAIN, 30));
		attach(DisplayMessage, 10, 600, 1300, 35);
		
		//UpdateQtySold panel
		updateQtySoldPanel=new JPanel();
		updateQtySoldPanel.setBorder(BorderFactory.createTitledBorder("Update Qty Sold"));
		attach(updateQtySoldPanel, 490, 10, 350, 120);
		
		qtySoldLabel=new JLabel("Quantity Sold");
		updateQtySoldPanel.add(qtySoldLabel);
		
		updateQtySoldtf=new JTextField(20);
		updateQtySoldPanel.add(updateQtySoldtf);
		
		updateQtySold=new JButton("Update Qty sold for the Selected Item");
		updateQtySoldPanel.add(updateQtySold);
		
		
		updateQtySold.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				selectedIndex=list.getSelectedIndex();
		            if ((selectedIndex == -1))
		            {
		            	DisplayMessage.setText("Please select an inventory item to increment sold qty");
		            }
		            else
		            {
		            	tempSelectedItem=listModel.getElementAt(selectedIndex);
		            	try 
		            	{
		            		
		            	if (Integer.parseInt(updateQtySoldtf.getText()) <= 0)
		                {
		            		DisplayMessage.setText("Enter number greater than 0");
		                }
		                else if (Integer.parseInt(updateQtySoldtf.getText()) > tempSelectedItem.get_QtyHand())
		                {
		                    DisplayMessage.setText("Enter number less than or equal to quantity on hand");
		                }
		                else
		                {
		                    //Saying server to update the file
		                    connectionSock = new Socket(localHost,PORT);
		                    serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
							pw = new PrintWriter(connectionSock.getOutputStream(),true);
							
							pw.println("updateQtySold<"+selectedIndex+"<"+updateQtySoldtf.getText()+"<"+tempSelectedItem.get_ID()+","+tempSelectedItem.get_ItemName()+","+
							tempSelectedItem.get_StartingQty()+","+tempSelectedItem.get_QtyMinRestock()+","+tempSelectedItem.get_QtySold()+","
							+tempSelectedItem.get_QtyRestocked()+","+tempSelectedItem.get_UnitPrice()+","+tempSelectedItem.get_QtyHand()+","+
							tempSelectedItem.get_Sales());
							
							if((updateQty=serverInput.readLine()).equals("unsual changes"))
							{
								DisplayMessage.setText("Unsual changes about this item. Refresh by loading data again");
							}
							else
							{
								listModel.remove(selectedIndex);
			                    tempSelectedItem.set_QtySold(Double.parseDouble((updateQty.split(",")[0])));
			                    tempSelectedItem.set_QtyHand(Double.parseDouble((updateQty.split(",")[1])));
			                    tempSelectedItem.set_Sales(Double.parseDouble((updateQty.split(",")[2])));
			                    listModel.add(selectedIndex, tempSelectedItem);
			                    DisplayMessage.setText("Incremented Qty Sold for item with Item code " + tempSelectedItem.get_ID());
							}
		                }
		                
		                
		            	}
		            	catch(Exception ex) 
		            	{
		            		DisplayMessage.setText("Enter Valid Integer");
		            	}
			}
			}
		});
		updateQtySoldPanel.validate();
		
		//updateQtyRestock Panel
		updateQtyRestockPanel=new JPanel();
		updateQtyRestockPanel.setBorder(BorderFactory.createTitledBorder("Restock Quantity"));
		attach(updateQtyRestockPanel, 850, 10, 370, 120);
		
		qtyRestockLabel=new JLabel("Quantity Restocked");
		updateQtyRestockPanel.add(qtyRestockLabel);
		
		updateQtyRestockedtf=new JTextField(20);
		updateQtyRestockPanel.add(updateQtyRestockedtf);
		
		updateQtyRestocked=new JButton("Update Qty Restocked for the Selected Item");
		updateQtyRestockPanel.add(updateQtyRestocked);
		
		updateQtyRestocked.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedIndex=list.getSelectedIndex();
	            if ((selectedIndex == -1))
	            {
	            	   DisplayMessage.setText("Please select an inventory item to increment Restocked qty"); 
	            }
	            else
	            {
	            	tempSelectedItem=listModel.getElementAt(selectedIndex);
	            	try 
	            	{
	            		
	            	if (Integer.parseInt(updateQtyRestockedtf.getText()) <= 0)
	                {
	            		DisplayMessage.setText("Enter number greater than 0");
	                }
	                else
	                {
	                    //Saying server to update the file
	                    connectionSock = new Socket(localHost,PORT);
	                    serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
						pw = new PrintWriter(connectionSock.getOutputStream(),true);
						
						pw.println("updateRestocked<"+selectedIndex+"<"+updateQtyRestockedtf.getText()+"<"+tempSelectedItem.get_ID()+","+tempSelectedItem.get_ItemName()+","+
						tempSelectedItem.get_StartingQty()+","+tempSelectedItem.get_QtyMinRestock()+","+tempSelectedItem.get_QtySold()+","
						+tempSelectedItem.get_QtyRestocked()+","+tempSelectedItem.get_UnitPrice()+","+tempSelectedItem.get_QtyHand()+","+
						tempSelectedItem.get_Sales());
						
						if((updateRestockedQty=serverInput.readLine()).equals("unsual changes"))
						{
							DisplayMessage.setText("Unsual changes about this item. Refresh by loading data again");
						}
						else
						{
							System.out.println(updateRestockedQty);
		                    listModel.remove(selectedIndex);
		                    tempSelectedItem.set_QtyRestocked(Double.parseDouble((updateRestockedQty.split(",")[0])));
		                    tempSelectedItem.set_QtyHand(Double.parseDouble((updateRestockedQty.split(",")[1])));
		                    listModel.add(selectedIndex, tempSelectedItem);
		                    DisplayMessage.setText("Incremented Qty Restocked for item with Item code " + tempSelectedItem.get_ID());
						}
	                }
	                
	            	}
	            	catch(Exception ex) 
	            	{
	            		DisplayMessage.setText("Enter Valid Integer in Restocking TextField");
	            	}
		}
				
			}
		});
		
		
		updateQtyRestockPanel.validate();
		
		//Delete Panel
		deletePanel=new JPanel(new GridBagLayout());
		deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Item"));
		attach(deletePanel, 1230, 10, 200, 120);
		GridBagConstraints gbc1 = new GridBagConstraints();
		deleteSelectedInventoryItem=new JButton("Delete Selected Item");
		deletePanel.add(deleteSelectedInventoryItem,gbc1);
		deleteSelectedInventoryItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				selectedIndex=list.getSelectedIndex();
				try {
	            if ((selectedIndex == -1))
	            {
	                DisplayMessage.setText("Please select an inventory item to Delete");
	            }
	            else 
	            {
	            	
	            	 connectionSock = new Socket(localHost,PORT);
	                    serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
						pw = new PrintWriter(connectionSock.getOutputStream(),true);
						
						pw.println("Delete<"+selectedIndex+"<"+listModel.elementAt(selectedIndex).get_QtyHand());
						
						if((serverInput.readLine()).equals("unsual changes"))
						{
							DisplayMessage.setText("Unsual changes about this item. Refresh by loading data again");
						}
						else
						{
							DisplayMessage.setText("Item Deleted with ID "+ listModel.elementAt(selectedIndex).get_ID());
							listModel.removeElementAt(selectedIndex);
						}
	            }
				}
				catch (Exception e3) 
				{
				DisplayMessage.setText(e3.getMessage());	
				}
				
			}
		});
		deletePanel.validate();
		
		saveDataPanel=new JPanel();
		saveDataPanel.setBorder(BorderFactory.createTitledBorder("Save Data"));
		attach(saveDataPanel, 1310, 140, 200, 160);
		saveInventoryData=new JButton("Save Inventory Data");
		saveSalesReport=new JButton("Save Sales Report");
		saveRestockReport=new JButton("Save Restock Report");
		saveServerFile=new JButton("Save ServerSide File");
		saveDataPanel.add(saveInventoryData);
		saveDataPanel.add(saveSalesReport);
		saveDataPanel.add(saveRestockReport);
		saveDataPanel.add(saveServerFile);
		saveInventoryData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listModel.size()==0) 
				{
					DisplayMessage.setText("Please load the Inventory Items First");
				}
				else 
				{
					saveDirectory=ChooseSaveFile();
					if(!saveDirectory.equals("")) {
					try
					{
						FileWriter fwriter = new FileWriter(saveDirectory);
						 BufferedWriter bf = new BufferedWriter (fwriter);
						 bf.write(String.format("%-15s%-25s%-15s%-18s%-13s%-16s%-15s%-13s%-11s", "ID","Item Name","Starting Qty",
									"Qty Min Restock","Qty Sold","Qty Restocked","Unit Price","Qty Hand","Sales"));
						 bf.newLine();
						 for(int i=0;i<listModel.size();i++) 
						 {
							 bf.write(listModel.getElementAt(i).toString());
							 bf.newLine();
						 }
				         DisplayMessage.setText("Saved Inventory data file on "+saveDirectory);
				         bf.close();
					}
					catch (IOException e4) 
					{
						e4.printStackTrace();
					}
				}
				}
			}
		});
		saveRestockReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(listModel.size()==0) 
				{
					DisplayMessage.setText("Please load the Inventory Items First");
				}
				else 
				{
					saveDirectory=ChooseSaveFile();
					if(!saveDirectory.equals("")) {
					try
					{
						FileWriter fwriter = new FileWriter(saveDirectory);
						 BufferedWriter bf = new BufferedWriter (fwriter);
						 bf.write(String.format("%-15s%-25s%-18s%-13s", "ID","Item Name",
									"Qty Min Restock","Qty Hand"));
						 bf.newLine();
						 for(int i=0;i<listModel.size();i++) 
						 {
							 if(listModel.getElementAt(i).get_QtyHand()<listModel.getElementAt(i).get_QtyMinRestock())
							 {
								 bf.write(String.format("%-15s%-25s%-18s%-13s",listModel.getElementAt(i).get_ID(),listModel.getElementAt(i).get_ItemName(),
										 listModel.getElementAt(i).get_QtyMinRestock(),listModel.getElementAt(i).get_QtyHand()));
								 bf.newLine(); 
							 }
						 }
				         DisplayMessage.setText("Restock needed items were written to file on "+saveDirectory);
				         bf.close();
					}
					catch (IOException e4) 
					{
						e4.printStackTrace();
					}
				}				
			}
			}
		});
		saveSalesReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listModel.size()==0) 
				{
					DisplayMessage.setText("Please load the Inventory Items First");
				}
				else 
				{
					saveDirectory=ChooseSaveFile();
					if(!saveDirectory.equals("")) {
					try
					{
						FileWriter fwriter = new FileWriter(saveDirectory);
						 BufferedWriter bf = new BufferedWriter (fwriter);
						 bf.write(String.format("%-15s%-25s%-13s%-15s%-11s", "ID","Item Name","Qty Sold","Unit Price","Sales"));
						 bf.newLine();
						 for(int i=0;i<listModel.size();i++) 
						 {
							 if(listModel.getElementAt(i).get_QtySold()>0)
							 {
								 bf.write(String.format("%-15s%-25s%-13s%-15s%-11s", listModel.getElementAt(i).get_ID(),listModel.getElementAt(i).get_ItemName()
										 ,listModel.getElementAt(i).get_QtySold(),listModel.getElementAt(i).get_UnitPrice(),listModel.getElementAt(i).get_Sales()));
								 bf.newLine(); 
							 }
						 }
				         DisplayMessage.setText("The sales report file was saved on "+saveDirectory);
				         bf.close();
					}
					catch (IOException e4) 
					{
						e4.printStackTrace();
					}
				}	
				}
			}
		});
		saveServerFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listModel.size()==0) 
				{
					DisplayMessage.setText("Please load the Inventory Items First");
				}
				else {
				try 
				{
				connectionSock = new Socket(localHost,PORT);
                serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
				pw = new PrintWriter(connectionSock.getOutputStream(),true);
				pw.println("SaveInventoryToFile");
				DisplayMessage.setText(serverInput.readLine());
				}
				catch(Exception c) 
				{
					
				}
			}
			}
		});
		saveDataPanel.validate();
	}
	 private static class WhiteYellowCellRenderer extends DefaultListCellRenderer
	 {
	        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) 
	        {
	            Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
	            if (((Inventory) list.getModel().getElementAt(index)).get_QtyHand() < ((Inventory) list.getModel().getElementAt(index)).get_QtyMinRestock()) {
	                c.setBackground( Color.green );
	            }
	            else {
	                c.setBackground( Color.white );
	            }
	            return c;
	   }
	 }
	public void attach(Component c, int x, int y, int w, int h) 
	{
		this.add(c);
		c.setBounds(x,y,w,h);
	}
	public String ChooseSaveFile() {
		 JFileChooser c = new JFileChooser();
	      // Demonstrate "Save" dialog:
	      int rVal = c.showSaveDialog(inventoryManagementClient.this);
	      if (rVal == JFileChooser.APPROVE_OPTION) 
	      {
	        return c.getCurrentDirectory().toString()+"\\" +c.getSelectedFile().getName()+ ".txt";
		  }
	      if (rVal == JFileChooser.CANCEL_OPTION) 
	      {
	        DisplayMessage.setText("Please Click Save after Selecting the file");
	        return "";
	      }
	      return "";
	}
	

}


