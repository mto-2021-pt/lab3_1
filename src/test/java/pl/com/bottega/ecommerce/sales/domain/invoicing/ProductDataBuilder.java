package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {
    private Id productId = Id.generate();
    private Money price = new Money(0.0);

    private String name = "produkt";

    private Date snapshotDate = new Date(2021, 04, 1);

    private ProductType type = ProductType.FOOD;

    public ProductDataBuilder() {}

    public ProductDataBuilder withId(Id id){
        this.productId = id;
        return this;
    }

    public ProductDataBuilder withName(String name){
        this.name = name;
        return this;
    }


    public ProductDataBuilder withType(ProductType type){
        this.type = type;
        return this;
    }

    public ProductDataBuilder withPrice(Money price){
        this.price = price;
        return this;
    }

    public ProductDataBuilder withDate(Date date){
        this.snapshotDate = date;
        return this;
    }

    public ProductData build(){
        return ProductData.create(productId, price, name, type, snapshotDate);
    }
}
