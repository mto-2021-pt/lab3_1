package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRequestBuilder {

    private ClientData client;
    private List<RequestItem> items = new ArrayList<>();

    public InvoiceRequestBuilder() {}

    public InvoiceRequestBuilder addClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder addItems(List<RequestItem> items) {
        this.items = items;
        return this;
    }

    public InvoiceRequestBuilder addItem(RequestItem item) {
        this.items.add(item);
        return this;
    }

    public InvoiceRequestBuilder addItem(ProductData productData, Money money, int quantity) {
        this.items.add(new RequestItem(productData, quantity, money));
        return this;
    }

    public InvoiceRequest build() {
        InvoiceRequest request = new InvoiceRequest(this.client);
        for(RequestItem item : this.items) {
            request.add(item);
        }
        return request;
    }

}
