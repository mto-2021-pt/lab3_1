package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceLineBuilder {
    private ProductData product = null;
    private int quantity = 0;
    private Money net = new MoneyBuilder().build();
    private Tax tax = new TaxBuilder().build();

    public InvoiceLineBuilder() { }
    public InvoiceLineBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    public InvoiceLineBuilder withNet(Money net) {
        this.net = net;
        return this;
    }
    public InvoiceLineBuilder withTax(Tax tax) {
        this.tax = tax;
        return this;
    }
    public InvoiceLine build() {
        return new InvoiceLine(product, quantity, net, tax);
    }
}
