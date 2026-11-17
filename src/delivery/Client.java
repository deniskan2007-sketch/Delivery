package delivery;

/** Неизменяемые данные клиента. */
public final class Client {
    private final String name;
    private final String phone;

    public Client(String name, String phone) {
        this.name = Validation.text(name, "Имя клиента");
        this.phone = Validation.text(phone, "Телефон");
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
}
