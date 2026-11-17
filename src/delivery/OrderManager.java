package delivery;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Хранилище в памяти. Создание публикует только полностью заполненный заказ. */
public final class OrderManager {
    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private final List<Courier> couriers = List.of(new Courier(1, "Иван"), new Courier(2, "Анна"));
    private int nextId = 1;

    public Order createOrder(Client client, String address, List<OrderItem> items) {
        Objects.requireNonNull(items, "Список позиций обязателен");
        if (items.isEmpty()) { throw new IllegalArgumentException("Заказ должен содержать хотя бы одну позицию."); }
        Order order = new Order(nextId, client, address);
        items.forEach(order::addItem);
        orders.put(nextId++, order);
        return order;
    }

    public Order getOrder(int id) {
        Order order = orders.get(id);
        if (order == null) { throw new IllegalArgumentException("Заказ №" + id + " не найден."); }
        return order;
    }
    public Courier getCourier(int id) {
        return couriers.stream().filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Курьер не найден."));
    }
    public List<Order> getOrders() { return List.copyOf(orders.values()); }
    public List<Courier> getCouriers() { return couriers; }
    public void chooseDelivery(int id, DeliveryMethod method) { getOrder(id).setDeliveryMethod(method); }
    public void assignCourier(int id, int courierId) { getOrder(id).assignCourier(getCourier(courierId)); }
    public void changeStatus(int id, OrderStatus status) { getOrder(id).changeStatus(status); }
}
