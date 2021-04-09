package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Currency;

public class MoneyBuilder {
    private double denomination = 1.0f;
    private String currencyCode = Money.DEFAULT_CURRENCY.getCurrencyCode();

    public MoneyBuilder() { }
    public MoneyBuilder withCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
    public MoneyBuilder withCurrencyCode(Currency currency) {
        this.currencyCode = currency.getCurrencyCode();
        return this;
    }
    public MoneyBuilder withDenomination(double denomination) {
        this.denomination = denomination;
        return this;
    }
    public Money build() {
        return new Money(denomination, currencyCode);
    }
}
