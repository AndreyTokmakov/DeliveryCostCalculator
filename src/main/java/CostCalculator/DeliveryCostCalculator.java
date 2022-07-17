package CostCalculator;

import Errors.InvalidDistance;

public final class DeliveryCostCalculator
{
    public enum CargoFragility {
        Durable(0),
        Fragile(300);

        private final int price;

        CargoFragility(int price) {
            this.price = price;
        }

        public final int getPrice()  {
            return this.price;
        }
    }

    public enum SizeType {
        Small(100),
        Large(200);

        private final int price;

        SizeType(int price) {
            this.price = price;
        }

        public final int getPrice()  {
            return this.price;
        }
    }

    public enum ServiceWorkload {
        Normal(1),
        AboveNormal(1.2),
        High(1.4),
        Extreme(1.6);

        private final double coefficient;

        ServiceWorkload(double coefficient) {
            this.coefficient = coefficient;
        }

        public final double getCoefficient()  {
            return this.coefficient;
        }
    }

    private final static int MINIMUM_DELIVERY_PRICE = 400;
    private final static int MAX_DIST_FOR_FRAGILE_CARGO = 30;

    private static int getPriceForDistance(final double distance)
            throws InvalidDistance {
        if (0.0 >= distance)
            throw new InvalidDistance("Distance is negative");
        if (2.0 > distance) {
            return 50;
        }  else if (10.0 > distance) {
            return 100;
        }  else if (30.0 > distance) {
            return 200;
        }
        return 300;
    }

    public static int estimatePrice(double distance,
                                    SizeType cargoSize,
                                    CargoFragility fragility,
                                    ServiceWorkload serviceBusy) throws InvalidDistance {
        if (CargoFragility.Fragile == fragility && distance > MAX_DIST_FOR_FRAGILE_CARGO)
            throw new InvalidDistance("Too far for fragile cargo");
        int price = getPriceForDistance(distance) + cargoSize.getPrice() + fragility.getPrice();
        price = (int) Math.round(serviceBusy.getCoefficient() * price);
        return Math.max(MINIMUM_DELIVERY_PRICE, price);
    }
}
