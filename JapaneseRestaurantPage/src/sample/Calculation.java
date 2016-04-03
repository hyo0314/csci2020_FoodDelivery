package sample;

/**
 * Created by mobile on 03/04/16.
 */
public class Calculation {
    private double subTotal;
    private double hstTotal;
    private double deliveryTip;
    private double eachAmount;

    public Calculation(double eachAmount)
    {
        this.eachAmount = eachAmount;
        this.subTotal = 0.0;
        this.hstTotal = 0.0;
        this.deliveryTip = 0.0;
    }

    public double getEachAmount()
    {
        return this.eachAmount;
    }

    public void setEachAmount(double s)
    {
        this.eachAmount = s;
    }
}
