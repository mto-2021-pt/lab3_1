package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    @Mock
    private InvoiceFactory invoiceFactory;
    @Mock
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;
    private ClientData clientData;
    private InvoiceRequest invoiceRequest;

    Invoice expectedInvoice;
    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(), "name");
        invoiceRequest = new InvoiceRequest(clientData);
        expectedInvoice = new Invoice(Id.generate(), clientData);
    }

    @Test
    void issueAnInvoiceWithOneItem() {
        expectedInvoice.addItem(new InvoiceLineBuilder().build());

        Mockito.when(invoiceFactory.create(notNull())).thenReturn(expectedInvoice);

        Invoice returnedInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(1, returnedInvoice.getItems().size());
    }
    @Test
    void calculateTaxInvokeCountVerification() {
        expectedInvoice.addItem(new InvoiceLineBuilder().build());
        expectedInvoice.addItem(new InvoiceLineBuilder().build());

        Mockito.when(invoiceFactory.create(notNull())).thenReturn(expectedInvoice);
        Mockito.when(taxPolicy.calculateTax(notNull(), notNull())).thenReturn(new TaxBuilder().build());

        bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, times(2)).calculateTax(notNull(), notNull());
    }
    @Test
    void issueAnInvoiceWithNoItems() {
        Mockito.when(invoiceFactory.create(notNull())).thenReturn(expectedInvoice);

        Invoice returnedInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(0, returnedInvoice.getItems().size());
    }
    @Test
    void calculateNumberOfInvoices() {
        Mockito.when(invoiceFactory.create(notNull())).thenReturn(expectedInvoice);

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(invoiceFactory, times(2)).create(notNull());
    }
    @Test
    void verifyInvoiceData() {
        Tax expectedTax = new TaxBuilder().withAmount(new MoneyBuilder().withDenomination(2).build()).build();
        expectedInvoice.addItem(new InvoiceLineBuilder().withTax(expectedTax).build());

        Mockito.when(invoiceFactory.create(notNull())).thenReturn(expectedInvoice);

        Invoice returnedInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertTrue(returnedInvoice.getItems().size() > 0);
        assertEquals(expectedTax, returnedInvoice.getItems().get(0).getTax());
    }
}
