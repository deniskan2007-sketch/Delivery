package delivery;

import java.math.BigDecimal;

public final class ExpressDelivery implements DeliveryMethod {
    @Override
    public String getName() { return "Экспресс"; }
    @Override
    public BigDecimal calculateCost() { return new BigDecimal("800.00"); }
    @Override
    public String getEstimatedTime() { return "2–4 часа"; }
    @Override
    public boolean requiresCourier() { return true; }
}
