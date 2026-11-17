package delivery;

import java.math.BigDecimal;

public final class StandardDelivery implements DeliveryMethod {
    @Override
    public String getName() { return "Стандартная"; }
    @Override
    public BigDecimal calculateCost() { return new BigDecimal("300.00"); }
    @Override
    public String getEstimatedTime() { return "1–2 дня"; }
    @Override
    public boolean requiresCourier() { return true; }
}
