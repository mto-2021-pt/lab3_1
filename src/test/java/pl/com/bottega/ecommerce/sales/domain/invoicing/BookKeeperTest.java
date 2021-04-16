package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.time.LocalDate;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    InvoiceFactory factory;
    @Mock
    TaxPolicy tax;

    BookKeeper keeper;

    @BeforeEach
    void setUp() throws Exception {
        keeper = new BookKeeper(factory);
    }

    @Test
    void invoiceRequestWithOneItem_invoiceSizeVerification() {
        ProductData potato = new ProductDataBuilder()
                .withName("ziemniak")
                .withPrice(new Money(2.5))
                .build();

        InvoiceRequest request = new InvoiceRequestBuilder()
                .addItem(potato, 1, new Money(10))
                .build();

        Invoice invoice_to_get = new Invoice(Id.generate(), new ClientData(Id.generate(), "klien"));
        Mockito.when(factory.create(notNull())).thenReturn(invoice_to_get);

        Mockito.when(tax.calculateTax(notNull(), notNull())).thenReturn(new Tax(new Money(0.0), ""));

        Invoice invoice = keeper.issuance(request, tax);
        assertEquals(1, invoice.getItems().size());
    }

    @Test
    void invoiceRequestWithTwoItems_methodCallVerification() {
        ProductData potato = new ProductDataBuilder()
                .withName("ziemniak")
                .withPrice(new Money(2.5))
                .build();

        InvoiceRequest request = new InvoiceRequestBuilder()
                .addItem(potato, 1, new Money(2.5))
                .addItem(potato, 2, new Money(5))
                .build();

        Invoice invoice_to_get = new Invoice(Id.generate(), new ClientData(Id.generate(), "klien"));
        Mockito.when(factory.create(notNull())).thenReturn(invoice_to_get);

        Mockito.when(tax.calculateTax(notNull(), notNull())).thenReturn(new Tax(new Money(0.0), ""));

        Invoice invoice = keeper.issuance(request, tax);
        Mockito.verify(tax, times(2)).calculateTax(notNull(), notNull());
    }

    @Test
    void invoiceRequestWithNoItems_methodCallVerification() {
        InvoiceRequest request = new InvoiceRequestBuilder()
                .build();

        Invoice invoice_to_get = new Invoice(Id.generate(), new ClientData(Id.generate(), "klien"));
        Mockito.when(factory.create(notNull())).thenReturn(invoice_to_get);

        Invoice invoice = keeper.issuance(request, tax);
        Mockito.verifyNoInteractions(tax);
    }


    @Test
    void invoiceRequestWithNoItems_invoiceSizeVerification() {
        InvoiceRequest request = new InvoiceRequestBuilder()
                .build();

        Invoice invoice_to_get = new Invoice(Id.generate(), new ClientData(Id.generate(), "klien"));
        Mockito.when(factory.create(notNull())).thenReturn(invoice_to_get);

        Invoice invoice = keeper.issuance(request, tax);
        assertEquals(0, invoice.getItems().size());
    }

    @Test
    void numberOfInvoicesCreated(){
        InvoiceRequest request = new InvoiceRequestBuilder()
                .build();

        Invoice invoice_to_get = new Invoice(Id.generate(), new ClientData(Id.generate(), "klien"));
        Mockito.when(factory.create(notNull())).thenReturn(invoice_to_get);

        keeper.issuance(request, tax);
        keeper.issuance(request, tax);

        Mockito.verify(factory, times(2)).create(notNull());
    }


    @Test
    void verifyProductDataInInvoice(){
        ProductData potato = new ProductDataBuilder()
                .withName("ziemniak")
                .withPrice(new Money(2.5))
                .build();

        InvoiceRequest request = new InvoiceRequestBuilder()
                .addItem(potato, 1, new Money(2.5))
                .build();

        Invoice invoice_to_get = new Invoice(Id.generate(), new ClientData(Id.generate(), "klien"));
        Mockito.when(factory.create(notNull())).thenReturn(invoice_to_get);
        Mockito.when(tax.calculateTax(notNull(), notNull())).thenReturn(new Tax(new Money(0.0), ""));

        Invoice invoice = keeper.issuance(request, tax);

        assertTrue(invoice.getItems().size() > 0);
        assertEquals(potato, invoice.getItems().get(0).getProduct());
    }
}
