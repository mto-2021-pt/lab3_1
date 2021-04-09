package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {
    private Money amount = new MoneyBuilder().build();
    private String description = "";

    public TaxBuilder() { }
    public TaxBuilder withAmount(Money amount) {
        this.amount = amount;
        return this;
    }
    public TaxBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    public Tax build() {
        return new Tax(amount, description);
    }
}
