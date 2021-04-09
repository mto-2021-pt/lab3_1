package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    private InvoiceFactory factory;
    @Mock
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;
    private ClientData clientData;
    private InvoiceRequest invoiceRequest;
    private Product product;
    private RequestItem requestItem;

    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(factory);
        clientData = new ClientData(Id.generate(), "irrelevant name");
        invoiceRequest = new InvoiceRequest(clientData);
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO, "irrelevant tax"));
        product = new Product(Id.generate(), Money.ZERO, "irrelevant name", ProductType.STANDARD);
        requestItem = new RequestItem(product.generateSnapshot(), 1, Money.ZERO);
    }

    @Test
    void OneItemInvoiceRequest_expectedOneItemInvoice() {
        invoiceRequest.add(requestItem);
        assertEquals(1, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    @Test
    public void TwoItemInvoiceRequest_expectedTwoCallsOfTaxMethod(){
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(ProductType.STANDARD, Money.ZERO);
    }

}
