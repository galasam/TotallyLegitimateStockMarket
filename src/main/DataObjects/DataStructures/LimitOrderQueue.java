package main.DataObjects.DataStructures;

import java.util.Comparator;
import java.util.TreeSet;
import main.DataObjects.LimitOrder;

public class LimitOrderQueue extends TreeSet<LimitOrder> {

    public enum SORTING_METHOD {PRICE_ASC, PRICE_DECS};

    private static final Comparator<LimitOrder> price_asc = (a, b) -> {
        float comp = a.getLimit() - b.getLimit();
        if(comp == 0) {
            return 0;
        } else if(comp < 0) {
            return -1;
        } else {
            return 1;
        }
    };

    private static final Comparator<LimitOrder> price_desc = price_asc.reversed();

    public LimitOrderQueue(SORTING_METHOD method) {
        super(getComparator(method));
    }

    private static Comparator<LimitOrder> getComparator(SORTING_METHOD method) {
        switch (method) {
            case PRICE_ASC:
                return price_asc;
            case PRICE_DECS:
                return price_desc;
            default:
                throw new UnsupportedOperationException("Sorting method not supported");
        }
    }

}
