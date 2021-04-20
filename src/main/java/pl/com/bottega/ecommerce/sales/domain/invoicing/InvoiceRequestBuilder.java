package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRequestBuilder {
    private ClientData client = new ClientData(Id.generate(), "DEFAULT");
    private List<RequestItem> items = new ArrayList<>();

    public InvoiceRequestBuilder() {
    }

    public InvoiceRequestBuilder withClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder withItems(List<RequestItem> items) {
        this.items = items;
        return this;
    }

    public InvoiceRequest build() {
        return new InvoiceRequest(client, items);
    }
}
