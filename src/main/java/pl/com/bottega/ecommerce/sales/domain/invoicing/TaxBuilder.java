package pl.com.bottega.ecommerce.sales.domain.invoicing;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {

    private Money money= Money.ZERO;
    private String description="";

    public TaxBuilder(){}

    public TaxBuilder withMoney(Money money) {
        this.money = money;
        return this;

    }

    public TaxBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Tax build()
    {
        return new Tax(money,description);
    }
}
