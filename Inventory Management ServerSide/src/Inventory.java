import java.io.Serializable;

public class Inventory implements Serializable {
	private String _ID;
    private String _ItemName;
    private Double _StartingQty;
    private Double _QtyMinRestock;
    private Double _QtySold;
    private Double _QtyRestocked;
    private Double _UnitPrice;
    private Double _QtyHand;
    private Double _Sales;

    public Inventory(String ID,String name,String StartingQty,String QtyMinRestock,String QtySold,String QtyRestocked,String unitPrice)
    {
        _ID = ID;
        _ItemName = name;
        _StartingQty = Double.parseDouble(StartingQty);
        _QtyMinRestock = Double.parseDouble(QtyMinRestock);
        _QtySold = Double.parseDouble(QtySold);
        _QtyRestocked = Double.parseDouble(QtyRestocked);
        _UnitPrice = Double.parseDouble(unitPrice);
        _QtyHand = _StartingQty-_QtySold + _QtyRestocked;
        _Sales = _UnitPrice * _QtySold;
    }

	public String get_ID() {
		return _ID;
	}

	public void set_ID(String _ID) {
		this._ID = _ID;
	}

	public String get_ItemName() {
		return _ItemName;
	}

	public void set_ItemName(String _ItemName) {
		this._ItemName = _ItemName;
	}

	public double get_StartingQty() {
		return _StartingQty;
	}

	public void set_StartingQty(double _StartingQty) {
		this._StartingQty = _StartingQty;
	}

	public double get_QtyMinRestock() {
		return _QtyMinRestock;
	}

	public void set_QtyMinRestock(double _QtyMinRestock) {
		this._QtyMinRestock = _QtyMinRestock;
	}

	public double get_QtySold() {
		return _QtySold;
	}

	public void set_QtySold(double _QtySold) {
		this._QtySold = _QtySold;
	}

	public double get_QtyRestocked() {
		return _QtyRestocked;
	}

	public void set_QtyRestocked(double _QtyRestocked) {
		this._QtyRestocked = _QtyRestocked;
	}

	public double get_UnitPrice() {
		return _UnitPrice;
	}

	public void set_UnitPrice(double _UnitPrice) {
		this._UnitPrice = _UnitPrice;
	}

	public double get_QtyHand() {
		return _QtyHand;
	}

	public void set_QtyHand(double _QtyHand) {
		this._QtyHand = _QtyHand;
	}

	public double get_Sales() {
		return _Sales;
	}

	public void set_Sales(double _Sales) {
		this._Sales = _Sales;
	}
	
	@Override
	public String toString() 
	{
		String fmt="%-15s%-25s%-15s%-18s%-13s%-16s%-15s%-13s%-11s";
		return String.format(fmt, this.get_ID(),this.get_ItemName(),Double.toString(this.get_StartingQty()),
				Double.toString(this.get_QtyMinRestock()),Double.toString(get_QtySold()),Double.toString(get_QtyRestocked()),Double.toString(get_UnitPrice()),
						Double.toString(get_QtyHand()),Double.toString(get_Sales()));
	}
}
