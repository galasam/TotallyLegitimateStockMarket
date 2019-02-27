package main.DataObjects;

public class Trade {

    public Trade(int buyOrder, int sellOrder, int matchQuantity, float matchPrice) {
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.matchQuantity = matchQuantity;
        this.matchPrice = matchPrice;
    }

    private final int buyOrder;
    private final int sellOrder;
    private final int matchQuantity;
    private final float matchPrice;

    public int getBuyOrder() {
        return buyOrder;
    }

    public int getSellOrder() {
        return sellOrder;
    }

    public int getMatchQuantity() {
        return matchQuantity;
    }

    public float getMatchPrice() {
        return matchPrice;
    }


}
