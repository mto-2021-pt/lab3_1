
package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    private InvoiceRequest invoiceRequest;
    private ClientData clientData;

    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(factory);
        clientData = new ClientData(Id.generate(), "name");
        invoiceRequest = new InvoiceRequest(clientData);
    }

    //Testy stanu

    @Test
    public void checkIfSinglePositionRequestReturnSinglePositionInvoice() {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        ProductBuilder productBuilder = new ProductBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        Money money = new Money(10);
        Product product = productBuilder.withPrice(money).withName("produkt").withProductType(ProductType.STANDARD).build();
        RequestItem requestItem = requestItemBuilder.withProductData(product.generateSnapshot()).withTotalCost(money).build();
        invoiceRequest.add(requestItem);

        int result = bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size();
        assertEquals(1,result);
    }

    @Test
    public void checkIfEmptyRequestRetuensEmptyInovice(){
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        assertEquals(0, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    @Test
    public void checkIfProductsOnInvoiceAreValid() {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        ProductBuilder productBuilder = new ProductBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        Money money = new Money(10);
        Product product = productBuilder.withPrice(money).withName("Fajny produkt").withProductType(ProductType.STANDARD).build();
        RequestItem requestItem = requestItemBuilder.withProductData(product.generateSnapshot()).withTotalCost(money).build();
        invoiceRequest.add(requestItem);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(product.generateSnapshot(), invoice.getItems().get(0).getProduct());
    }

    // Testy zachowania

    @Test
    public void checkIfDoublePositionRequestInvokeCalculateTaxTwice(){
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(factory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        ProductBuilder productBuilder = new ProductBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        Money money = new Money(10);
        Product product = productBuilder.withPrice(money).withName("produkt").withProductType(ProductType.STANDARD).build();
        RequestItem requestItem = requestItemBuilder.withProductData(product.generateSnapshot()).withTotalCost(money).build();

        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);


        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(ProductType.STANDARD, money);
    }

    @Test
    public void checkIfEmptyRequestDoesntInvokeCalculateTaxMethod(){
        verify(taxPolicy, times(0)).calculateTax(ProductType.STANDARD, Money.ZERO);
    }

    @Test()
    public void checkIfMethodThrowsExceptionWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> bookKeeper.issuance(null, taxPolicy));
    }

}
