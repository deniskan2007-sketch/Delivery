package delivery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Заказ защищает свои инварианты независимо от интерфейса пользователя. */
public final class Order {
    private final int id;
    private final Client client;
    private final String address;
    private final List<OrderItem> items = new ArrayList<>();
    private DeliveryMethod deliveryMethod;
    private Courier courier;
    private OrderStatus status = OrderStatus.CREATED;

    public Order(int id, Client client, String address) {
        if (id <= 0) { throw new IllegalArgumentException("Номер заказа должен быть положительным."); }
        this.id = id;
        this.client = Objects.requireNonNull(client, "Клиент обязателен");
        this.address = Validation.text(address, "Адрес доставки или пункта выдачи");
    }

    private void requireCreated() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Изменение состава заказа разрешено только в статусе CREATED.");
        }
    }

    public void addItem(OrderItem item) {
        requireCreated();
        items.add(Objects.requireNonNull(item, "Позиция обязательна"));
    }

    public void setDeliveryMethod(DeliveryMethod method) {
        requireCreated();
        Objects.requireNonNull(method, "Способ доставки обязателен");
        // При переходе к самовывозу прежний курьер больше не нужен.
        if (!method.requiresCourier() && courier != null) {
            courier.release(id);
            courier = null;
        }
        deliveryMethod = method;
    }

    public void assignCourier(Courier candidate) {
        requireCreated();
        Objects.requireNonNull(candidate, "Курьер обязателен");
        if (deliveryMethod == null) { throw new IllegalStateException("Сначала выберите способ доставки."); }
        if (!deliveryMethod.requiresCourier()) { throw new IllegalStateException("Самовывоз не требует курьера."); }
        if (candidate == courier) { throw new IllegalStateException("Этот курьер уже назначен."); }
        // Сначала резервируем нового: при ошибке прежнее назначение сохраняется.
        candidate.reserve(id);
        if (courier != null) { courier.release(id); }
        courier = candidate;
    }

    public void changeStatus(OrderStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException("Недопустимый переход " + status + " → " + next);
        }
        if (next == OrderStatus.DELIVERING) {
            if (items.isEmpty()) { throw new IllegalStateException("Добавьте хотя бы одну позицию."); }
            if (deliveryMethod == null) { throw new IllegalStateException("Выберите способ доставки."); }
            if (deliveryMethod.requiresCourier() && courier == null) {
                throw new IllegalStateException("Назначьте курьера.");
            }
        }
        if (next == OrderStatus.COMPLETED && courier != null) { courier.release(id); }
        status = next;
    }

    public BigDecimal getItemsCost() {
        return items.stream().map(OrderItem::getSubtotal).reduce(new BigDecimal("0.00"), BigDecimal::add);
    }
    public BigDecimal getDeliveryCost() {
        return deliveryMethod == null ? new BigDecimal("0.00") : deliveryMethod.calculateCost();
    }
    public BigDecimal getTotalCost() { return getItemsCost().add(getDeliveryCost()); }
    public int getId() { return id; }
    public Client getClient() { return client; }
    public String getAddress() { return address; }
    public List<OrderItem> getItems() { return List.copyOf(items); }
    public DeliveryMethod getDeliveryMethod() { return deliveryMethod; }
    public Courier getCourier() { return courier; }
    public OrderStatus getStatus() { return status; }
}
