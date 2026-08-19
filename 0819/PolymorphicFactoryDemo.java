interface FeePolicy {
    int calculate(int amount);
    String name();
}

class RegularFee implements FeePolicy {
    @Override
    public int calculate(int amount) {
        return Math.max(0, amount);
    }

    @Override
    public String name() {
        return "regular";
    }
}

class MemberFee implements FeePolicy {
    @Override
    public int calculate(int amount) {
        return Math.max(0, amount) * 90 / 100;
    }

    @Override
    public String name() {
        return "member";
    }
}

public class PolymorphicFactoryDemo {
    static FeePolicy createPolicy(String type) {
        if ("member".equalsIgnoreCase(type)) {
            return new MemberFee();
        }
        return new RegularFee();
    }

    static void printFee(FeePolicy policy, int amount) {
        System.out.println(policy.name() + " fee=" + policy.calculate(amount));
    }

    public static void main(String[] args) {
        FeePolicy first = createPolicy("member");
        FeePolicy second = createPolicy("unknown");
        printFee(first, 1000);
        printFee(second, 1000);
    }
}