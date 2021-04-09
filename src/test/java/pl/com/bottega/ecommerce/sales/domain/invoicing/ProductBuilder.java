package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
    private Money price = new MoneyBuilder().build();
    private String name = "sample_product";
    private ProductType productType = ProductType.STANDARD;
    private Id id = Id.generate();

    public ProductBuilder() { }
    public ProductBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }
    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public ProductBuilder withProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }
    public ProductBuilder withID(Id id) {
        this.id = id;
        return this;
    }
    public Product build() {
        return new Product(id, price, name, productType);
    }
}
