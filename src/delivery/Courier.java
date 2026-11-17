package delivery;

/** Курьер хранит номер активного заказа; произвольного сеттера занятости нет. */
public final class Courier {
    private final int id;
    private final String name;
    private Integer activeOrderId;

    public Courier(int id, String name) {
        if (id <= 0) { throw new IllegalArgumentException("Номер курьера должен быть положительным."); }
        this.id = id;
        this.name = Validation.text(name, "Имя курьера");
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isAvailable() { return activeOrderId == null; }

    void reserve(int orderId) {
        if (!isAvailable()) { throw new IllegalStateException("Курьер занят заказом №" + activeOrderId); }
        activeOrderId = orderId;
    }

    void release(int orderId) {
        if (activeOrderId == null || activeOrderId != orderId) {
            throw new IllegalStateException("Курьер не назначен на этот заказ.");
        }
        activeOrderId = null;
    }
}
