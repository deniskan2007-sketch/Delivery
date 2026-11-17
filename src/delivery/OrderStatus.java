package delivery;

public enum OrderStatus {
    CREATED("Создан"), DELIVERING("В доставке / готов к выдаче"), COMPLETED("Завершён");

    private final String label;
    OrderStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
    public boolean canTransitionTo(OrderStatus next) {
        return (this == CREATED && next == DELIVERING)
                || (this == DELIVERING && next == COMPLETED);
    }
}
