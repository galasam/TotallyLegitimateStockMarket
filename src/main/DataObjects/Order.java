package main.DataObjects;

import java.util.Optional;
import main.DataStructures.MarketState;

public abstract class Order {

    public abstract Optional<Trade> process(MarketState marketState);

}
