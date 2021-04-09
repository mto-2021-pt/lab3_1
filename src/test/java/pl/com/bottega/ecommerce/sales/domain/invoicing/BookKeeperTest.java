package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
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
        product = new ProductBuilder().build();
        requestItem = new RequestItemBuilder().build();
    }

    @Test
    void OneItemInvoiceRequest_expectedOneItemInvoice() {
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO, "irrelevant tax"));
        invoiceRequest.add(requestItem);
        assertEquals(1, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    @Test
    public void TwoItemInvoiceRequest_expectedTwoCallsOfTaxMethod(){
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO, "irrelevant tax"));
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(ProductType.STANDARD, Money.ZERO);
    }

    @Test
    public void NoItemInvoiceRequest_expectedNoItemInvoice(){
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        assertEquals(0, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    @Test
    public void ThreeItemsInvoiceRequest_expectedThreeItemsInvoice(){
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO, "irrelevant tax"));
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        assertEquals(3, bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size());
    }

    @Test
    public void NoItemInvoiceRequest_expectedZeroCallsOfTaxMethod(){
        verify(taxPolicy, times(0)).calculateTax(ProductType.STANDARD, Money.ZERO);
    }

    @Test
    public void NullInvoiceRequest_expectedNullPointerException(){
        assertThrows(NullPointerException.class, () -> bookKeeper.issuance(null, taxPolicy));
    }
}
