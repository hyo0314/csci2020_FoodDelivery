package sample;

public class MenuItems {

    private  String foodName;
    private  int amount;
    private  double dollar;


    public MenuItems(String foodName, int  amount, double dollar) {
        this.foodName = foodName;
        this.amount = amount;
        this.dollar = dollar;
    }

    public String getFoodName(){
        return foodName;
    }

    public int getAmount(){
        return amount;
    }

    public double getDollar(){
        return dollar;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public void setDollar(double dollar){
        this.dollar = dollar;
    }
}
