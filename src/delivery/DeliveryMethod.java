package delivery;

import java.math.BigDecimal;

/** Стратегия расчёта доставки; заказ работает только с этим контрактом. */
public interface DeliveryMethod {
    String getName();
    BigDecimal calculateCost();
    String getEstimatedTime();
    boolean requiresCourier();
}
