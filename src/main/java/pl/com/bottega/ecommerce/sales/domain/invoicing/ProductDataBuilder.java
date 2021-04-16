package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {

    private Id productId = Id.generate();
    private Money price = new Money(0.0);

    private String name = "Product";

    private Date snapshotDate = new Date();

    private ProductType type = ProductType.STANDARD;

    public ProductDataBuilder() {}

    public ProductDataBuilder addId(Id id) {
        this.productId = id;
        return this;
    }

    public ProductDataBuilder addName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder addType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductDataBuilder addPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder addDate(Date date) {
        this.snapshotDate = date;
        return this;
    }

    public ProductData build() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }

}
