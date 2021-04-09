package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InvoiceRequestBuilder {
    private ClientData client = new ClientData(new Id("0"), "Client");
    private List<RequestItem> items = new ArrayList<>();

    public InvoiceRequestBuilder() {}

    public InvoiceRequestBuilder withClient(ClientData client){
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder withItems(List<RequestItem> items){
        this.items = items;
        return this;
    }

    public InvoiceRequestBuilder addItem(RequestItem item){
        this.items.add(item);
        return this;
    }

    public InvoiceRequestBuilder addItem(ProductData productData, int quantity, Money totalCost) {
        this.items.add(new RequestItem(productData, quantity, totalCost));
        return this;
    }

    public InvoiceRequest build(){
        InvoiceRequest request = new InvoiceRequest(client);

        for (RequestItem item : items) {
            request.add(item);
        }

        return request;
    }
}
