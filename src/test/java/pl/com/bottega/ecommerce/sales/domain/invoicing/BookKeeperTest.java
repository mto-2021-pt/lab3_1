package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    @Mock
    private InvoiceFactory factory;
    @Mock
    private TaxPolicy taxPolicy;

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private ClientData clientData;

    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(factory);
        clientData = new ClientData(Id.generate(),"client");
        invoiceRequest = new InvoiceRequest(clientData);
        taxPolicy = mock(TaxPolicy.class);

    }


    @Test
    void InvoiceWithOneItemSizeTest() {
        when(taxPolicy.calculateTax(any(), any())).thenReturn(new Tax(Money.ZERO,"desc"));
        
        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);
        RequestItem requestItem = new RequestItem(productData,1,Money.ZERO);
        invoiceRequest.add(requestItem);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        assertEquals(invoice.getItems().size(),1);
    }

    @Test
    void InvoiceWithTwoSameItemsExecutionTimesTest() {
        when(taxPolicy.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"desc"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);
        RequestItem requestItem = new RequestItem(productData,1,Money.ZERO);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        Mockito.verify(taxPolicy,times(2)).calculateTax(ProductType.STANDARD, Money.ZERO);
    }

    @Test
    void InvoiceWithTwoItemsTest() {
        when(taxPolicy.calculateTax(any(), any())).thenReturn(new Tax(Money.ZERO,"desc"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);
        RequestItem first = new RequestItem(productData,1,Money.ZERO);
        invoiceRequest.add(first);
        RequestItem sec = new RequestItem(productData,1, new Money(1));
        invoiceRequest.add(sec);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        Mockito.verify(taxPolicy,times(1)).calculateTax(ProductType.STANDARD, new Money(0));
        Mockito.verify(taxPolicy,times(1)).calculateTax(ProductType.STANDARD, new Money(1));
    }
    @Test
    void InvoiceWithNoItems() {
        Mockito.lenient().when(taxPolicy.calculateTax(any(), any())).thenReturn(new Tax(Money.ZERO,"desc"));

        ProductData productData=mock(ProductData.class);
        Mockito.lenient().when(productData.getType()).thenReturn(ProductType.STANDARD);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        Mockito.lenient().when(factory.create(clientData)).thenReturn(invoice);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        Assertions.assertTrue(invoice.getItems().size() == 0);
    }

    @Test
    void InvoiceWithNoItemsValueTest() {
        Mockito.lenient().when(taxPolicy.calculateTax(any(), any())).thenReturn(new Tax(Money.ZERO,"desc"));

        ProductData productData=mock(ProductData.class);
        Mockito.lenient().when(productData.getType()).thenReturn(ProductType.STANDARD);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        Mockito.lenient().when(factory.create(clientData)).thenReturn(invoice);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        Assertions.assertTrue(invoice.getNet().equals(Money.ZERO));
    }
}
