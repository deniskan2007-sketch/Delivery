package delivery;

import java.math.BigDecimal;

public final class PickupDelivery implements DeliveryMethod {
    @Override
    public String getName() { return "Самовывоз"; }
    @Override
    public BigDecimal calculateCost() { return new BigDecimal("0.00"); }
    @Override
    public String getEstimatedTime() { return "В часы работы пункта выдачи"; }
    @Override
    public boolean requiresCourier() { return false; }
}
