package Model;

public class PaymentMethod {
    private final String name;

    public PaymentMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

//DANKORT,KONTANT,KLIPPEKORT,MOBILEPAY,REGNING