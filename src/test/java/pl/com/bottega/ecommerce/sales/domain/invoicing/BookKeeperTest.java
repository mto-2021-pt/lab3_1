package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    private final String NAME = "test_name";
    private final String DESCRIPTION = "description";

    @Mock
    private InvoiceFactory factory;

    @Mock
    private TaxPolicy taxPolicy;

    private InvoiceRequest invoiceRequest;
    private ClientData clientData;
    private Money money;
    private RequestItem requestItem;
    private ProductData productData;
    private Tax tax;
    private BookKeeper bookKeeper;

    @BeforeEach
    void setUp() throws Exception {
        money = Money.ZERO;
        tax = new Tax(money, DESCRIPTION);
        clientData = new ClientData(Id.generate(), NAME);
        productData = new ProductDataBuilder().build();
        requestItem = new RequestItem(productData, 0, money);
    }

    @Test
    void shouldReturnInvoiceWithOneItem() {
        //Given
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);
        when(factory.create(any())).thenReturn(new Invoice(Id.generate(), null));
        bookKeeper = new BookKeeper(factory);
        invoiceRequest = new InvoiceRequestBuilder().addClient(clientData).addItem(requestItem).build();

        //When
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        //Then
        assertEquals(1, invoice.getItems().size());
        assertEquals(Money.ZERO, invoice.getNet());
        assertEquals(Money.ZERO, invoice.getGros());
    }

    @Test
    void shouldReturnInvoiceWithoutItems() {
        //Given
        when(factory.create(any())).thenReturn(new Invoice(Id.generate(), null));
        bookKeeper = new BookKeeper(factory);
        invoiceRequest = new InvoiceRequestBuilder().addClient(clientData).build();

        //When
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        //Then
        assertEquals(0, invoice.getItems().size());
        assertEquals(Money.ZERO, invoice.getNet());
        assertEquals(Money.ZERO, invoice.getGros());
    }

    @Test
    void shouldReturnInvoiceWithNineItems() {
        //Given
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);
        when(factory.create(any())).thenReturn(new Invoice(Id.generate(), null));
        bookKeeper = new BookKeeper(factory);
        invoiceRequest = new InvoiceRequestBuilder().addClient(clientData).addItem(requestItem)
                .addItem(requestItem).addItem(requestItem).addItem(requestItem).addItem(requestItem)
                .addItem(requestItem).addItem(requestItem).addItem(requestItem).addItem(requestItem)
                .build();

        //When
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        //Then
        assertEquals(9, invoice.getItems().size());
        assertEquals(Money.ZERO, invoice.getNet());
        assertEquals(Money.ZERO, invoice.getGros());
    }

    @Test
    void shouldCallCalculateMethodTwice() {
        //Given
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);
        when(factory.create(any())).thenReturn(new Invoice(Id.generate(), null));
        bookKeeper = new BookKeeper(factory);
        invoiceRequest = new InvoiceRequestBuilder().addClient(clientData).addItem(requestItem).addItem(requestItem).build();

        //When
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        //Then
        verify(taxPolicy,times(2)).calculateTax(any(), any());
    }

    @Test
    void shouldNotCallCalculateMethod() {
        //Given
        when(factory.create(any())).thenReturn(new Invoice(Id.generate(), null));
        bookKeeper = new BookKeeper(factory);
        invoiceRequest = new InvoiceRequestBuilder().addClient(clientData).build();

        //When
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        //Then
        verify(taxPolicy,times(0)).calculateTax(any(), any());
    }

    @Test
    void shouldCallCalculateMethodNineTimes() {
        //Given
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);
        when(factory.create(any())).thenReturn(new Invoice(Id.generate(), null));
        bookKeeper = new BookKeeper(factory);
        invoiceRequest = new InvoiceRequestBuilder().addClient(clientData).addItem(requestItem)
                .addItem(requestItem).addItem(requestItem).addItem(requestItem).addItem(requestItem)
                .addItem(requestItem).addItem(requestItem).addItem(requestItem).addItem(requestItem)
                .build();

        //When
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        //Then
        verify(taxPolicy,times(9)).calculateTax(any(), any());
    }

}
