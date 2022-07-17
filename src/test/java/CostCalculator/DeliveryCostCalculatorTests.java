package CostCalculator;

import Errors.InvalidDistance;

import CostCalculator.DeliveryCostCalculator.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static CostCalculator.DeliveryCostCalculator.estimatePrice;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DeliveryCostCalculatorTests {

    @Nested
    class CalculatorTests
    {
        @ParameterizedTest(name="TestCase_{index} [Distance: {0}, Size: {1}, Fragility: {2}, Status: {3}]")
        @ArgumentsSource(DataProvider.class)
        void positiveTests(double dist,
                           SizeType sizeType,
                           CargoFragility fragility,
                           ServiceWorkload workload,
                           int expected) {
            final int price = estimatePrice(dist, sizeType, fragility, workload);
            Assertions.assertEquals(expected, price);
        }

        @Test
        @DisplayName("Exception shall be thrown in case if distance is zero")
        public void zeroDistance()  {
            final int dist = 0;

            final Exception exception = Assertions.assertThrows(InvalidDistance.class, () -> {
                estimatePrice(dist, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.Normal);
            }, "InvalidDistance exception shall be thrown");
            Assertions.assertEquals("Distance is negative", exception.getMessage());
        }

        @Test
        @DisplayName("Exception shall be thrown in case of negative distance")
        public void negativeDistance()  {
            final int dist = -15;

            final Exception exception = Assertions.assertThrows(InvalidDistance.class, () -> {
                estimatePrice(dist, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.Normal);
            }, "InvalidDistance exception shall be thrown");
            Assertions.assertEquals("Distance is negative", exception.getMessage());
        }

        @Test
        @DisplayName("Fragile goods cannot be transported over a distance of more than 30 km")
        public void tooFar4fragileCargo()  {
            final int dist = 33;

            final Exception exception = Assertions.assertThrows(InvalidDistance.class, () -> {
                estimatePrice(dist, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.Normal);
            }, "Too far for fragile cargo");
            Assertions.assertEquals("Too far for fragile cargo", exception.getMessage());
        }
    }

    @Nested
    class ModulesTests
    {
        @Test
        public void CargoFragilityTest()  {
            Assertions.assertEquals(CargoFragility.Durable.getPrice(), 0);
            Assertions.assertEquals(CargoFragility.Fragile.getPrice(), 300);
        }

        @Test
        public void SizeTypeTest()  {
            Assertions.assertEquals(SizeType.Small.getPrice(), 100);
            Assertions.assertEquals(SizeType.Large.getPrice(), 200);
        }

        @Test
        public void ServiceWorkloadTest()  {
            Assertions.assertEquals(ServiceWorkload.Normal.getCoefficient(), 1.0);
            Assertions.assertEquals(ServiceWorkload.AboveNormal.getCoefficient(), 1.2);
            Assertions.assertEquals(ServiceWorkload.High.getCoefficient(), 1.4);
            Assertions.assertEquals(ServiceWorkload.Extreme.getCoefficient(), 1.6);
        }
    }

    /**
     * The set of test data is based on the pairwise testing methodology
     * && Equivalence class partitioning && Boundary Value Analysis
     * **/
    private final static class DataProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    arguments(1, SizeType.Small, CargoFragility.Durable, ServiceWorkload.Normal, 400),
                    arguments(1, SizeType.Large, CargoFragility.Fragile, ServiceWorkload.AboveNormal, 660),
                    arguments(1.99, SizeType.Small, CargoFragility.Durable, ServiceWorkload.High, 400),
                    arguments(1.99, SizeType.Large, CargoFragility.Fragile, ServiceWorkload.Extreme, 880),

                    arguments(2, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.Normal, 500),
                    arguments(2, SizeType.Large, CargoFragility.Durable, ServiceWorkload.AboveNormal, 400),
                    arguments(2, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.High, 700),
                    arguments(9.99, SizeType.Large, CargoFragility.Durable, ServiceWorkload.High, 420),
                    arguments(9.99, SizeType.Large, CargoFragility.Fragile, ServiceWorkload.Extreme, 960),

                    arguments(10.0, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.Normal, 600),
                    arguments(10.0, SizeType.Large, CargoFragility.Durable, ServiceWorkload.AboveNormal, 480),
                    arguments(29.99, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.High, 840),
                    arguments(29.99, SizeType.Large, CargoFragility.Durable, ServiceWorkload.Extreme, 640),

                    arguments(30.00, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.Normal, 700),
                    arguments(30.00, SizeType.Large, CargoFragility.Durable, ServiceWorkload.AboveNormal, 600),
                    arguments(30.00, SizeType.Small, CargoFragility.Fragile, ServiceWorkload.High, 980),
                    arguments(30.00, SizeType.Large, CargoFragility.Durable, ServiceWorkload.Extreme, 800),

                    arguments(50.00, SizeType.Small, CargoFragility.Durable, ServiceWorkload.Normal, 400),
                    arguments(50.00, SizeType.Small, CargoFragility.Durable, ServiceWorkload.High, 560)
            );
        }
    }
}
