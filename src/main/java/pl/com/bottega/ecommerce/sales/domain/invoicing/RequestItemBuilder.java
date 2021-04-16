package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
    private ProductData productData;
    private int quantity=1;
    private Money money;

    public RequestItemBuilder withProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    public RequestItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public RequestItemBuilder withMoney(Money money) {
        this.money = money;
        return this;
    }
    public RequestItem build()
    {
        return new RequestItem(productData,quantity,money);
    }
}
