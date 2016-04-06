package sample;

/**
 * Created by mobile on 06/04/16.
 */
public class TotalCost {
    double subTotal;
    double hst;
    double devlieryTip;
    double total;

    public TotalCost(double subTotal)
    {
        this.subTotal = subTotal;
        this.hst = 0.0;
        this.devlieryTip = 0.0;
        this.total = 0.0;
    }

    public double getSubTotal()
    {
        return this.subTotal;
    }

    public double getHst()
    {
        hst = subTotal * 0.13;
        return hst;
    }

    public double getDevlieryTip()
    {
        devlieryTip = subTotal * 0.1;
        return devlieryTip;
    }

    public double getTotal()
    {
        double tempHst = getHst();
        double tempTip = getDevlieryTip();
        total = tempHst + tempTip + this.subTotal;
        return total;
    }

    public void setSubTotal(double s){this.subTotal = s;}
}
