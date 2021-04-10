package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;


@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    InvoiceFactory invoiceFactory;
    @Mock
    TaxPolicy taxPolicy;
    @Mock
    ProductData productData;

    private BookKeeper bookKeeper;
    private ClientData clientData;
    private InvoiceRequest invoiceRequest;
    private Tax tax;
    private RequestItem requestItem;

    @BeforeEach
    void setUp() {
        invoiceFactory = mock(InvoiceFactory.class);
        taxPolicy = mock(TaxPolicy.class);
        productData = mock(ProductData.class);
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(), "client");
        invoiceRequest = new InvoiceRequest(clientData);
        Money money = new Money(0.25);
        tax=new Tax(money,"25%");
        requestItem=new RequestItem(productData,4,money);

    }


    @Test
    void invoiceRequestShouldReturnOnePosition() {
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);
        invoiceRequest.add(requestItem);
        Invoice invoice = new Invoice(Id.generate(), clientData);
        when(invoiceFactory.create(any())).thenReturn(invoice);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), Is.is((1)));

    }

}
