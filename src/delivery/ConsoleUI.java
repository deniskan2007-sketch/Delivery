package delivery;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** Координирует меню, не дублируя правила предметной области. */
public final class ConsoleUI {
    private final OrderManager manager;
    private final ConsoleInput input;
    private final OrderPrinter printer;
    private final PrintStream out;
    private final List<DeliveryMethod> methods = List.of(
            new StandardDelivery(), new ExpressDelivery(), new PickupDelivery());

    public ConsoleUI(OrderManager manager, ConsoleInput input, PrintStream out) {
        this.manager = manager;
        this.input = input;
        this.out = out;
        this.printer = new OrderPrinter(out);
    }

    public void start() {
        boolean running = true;
        while (running) {
            out.println("\n1 — Создать заказ\n2 — Список заказов\n3 — Способ доставки"
                    + "\n4 — Назначить курьера\n5 — Изменить статус\n6 — Выход");
            try {
                switch (input.integer("Выберите действие: ", 1, 6)) {
                    case 1 -> createOrder();
                    case 2 -> showOrders();
                    case 3 -> chooseDelivery();
                    case 4 -> assignCourier();
                    case 5 -> changeStatus();
                    case 6 -> { out.println("Работа завершена."); running = false; }
                    default -> throw new IllegalStateException("Неизвестная команда");
                }
            } catch (NoSuchElementException e) {
                out.println("Ввод завершён. Работа программы остановлена.");
                running = false;
            } catch (IllegalArgumentException | IllegalStateException e) {
                out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void createOrder() {
        Client client = new Client(input.text("Имя клиента: "), input.text("Телефон: "));
        String address = input.text("Адрес доставки или пункта выдачи: ");
        List<OrderItem> items = new ArrayList<>();
        do {
            items.add(new OrderItem(input.text("Товар: "),
                    input.integer("Количество: ", 1, Integer.MAX_VALUE), input.price("Цена за единицу: ")));
        } while (input.integer("Добавить ещё позицию? 1 — да, 0 — нет: ", 0, 1) == 1);
        Order order = manager.createOrder(client, address, items);
        out.println("Заказ создан.");
        printer.print(order);
    }
    private void showOrders() {
        if (manager.getOrders().isEmpty()) { out.println("Заказов пока нет."); }
        manager.getOrders().forEach(printer::print);
    }
    private int orderId() {
        int id = input.integer("Номер заказа: ", 1, Integer.MAX_VALUE);
        manager.getOrder(id);
        return id;
    }
    private void chooseDelivery() {
        int id = orderId();
        for (int i = 0; i < methods.size(); i++) {
            DeliveryMethod method = methods.get(i);
            out.println((i + 1) + " — " + method.getName() + ": " + method.calculateCost()
                    + " руб., " + method.getEstimatedTime());
        }
        int choice = input.integer("Способ доставки: ", 1, methods.size());
        manager.chooseDelivery(id, methods.get(choice - 1));
        printer.print(manager.getOrder(id));
    }
    private void assignCourier() {
        int id = orderId();
        for (Courier courier : manager.getCouriers()) {
            out.println(courier.getId() + " — " + courier.getName()
                    + (courier.isAvailable() ? " (свободен)" : " (занят)"));
        }
        manager.assignCourier(id, input.integer("Номер курьера: ", 1, Integer.MAX_VALUE));
        printer.print(manager.getOrder(id));
    }
    private void changeStatus() {
        int id = orderId();
        out.println("Текущий статус: " + manager.getOrder(id).getStatus());
        out.println("1 — В доставке / готов к выдаче\n2 — Завершён");
        int choice = input.integer("Новый статус: ", 1, 2);
        manager.changeStatus(id, choice == 1 ? OrderStatus.DELIVERING : OrderStatus.COMPLETED);
        printer.print(manager.getOrder(id));
    }
}
