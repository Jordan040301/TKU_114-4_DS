interface DiscountPolicy {
    int apply(int originalPrice);
}

class NoDiscount implements DiscountPolicy {
    @Override
    public int apply(int originalPrice) {
        return originalPrice;
    }
}

class PercentageDiscount implements DiscountPolicy {
    private int percent;

    PercentageDiscount(int percent) {
        this.percent = Math.max(0, Math.min(100, percent));
    }

    @Override
    public int apply(int originalPrice) {
        return originalPrice * (100 - percent) / 100;
    }
}

class PriceCalculator {
    private DiscountPolicy policy;

    PriceCalculator(DiscountPolicy policy) {
        this.policy = policy;
    }

    int calculate(int originalPrice) {
        return policy.apply(Math.max(0, originalPrice));
    }
}

public class StrategyCompositionDemo {
    public static void main(String[] args) {
        PriceCalculator regular = new PriceCalculator(new NoDiscount());
        PriceCalculator sale =
            new PriceCalculator(new PercentageDiscount(20));

        System.out.println("原價：" + regular.calculate(1000));
        System.out.println("八折：" + sale.calculate(1000));
    }
}