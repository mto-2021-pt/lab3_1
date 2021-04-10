package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookKeeperTest {

    BookKeeper bookKeeper;
    InvoiceRequest invoiceRequest;
    Product product;
    ProductBuilder productBuilder = new ProductBuilder();
    RequestItemBuilder itemBuilder = new RequestItemBuilder();

    @Mock
    TaxPolicy taxMock;

    @BeforeEach
    void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "name"));
        product = productBuilder.setAggregateId(Id.generate()).setProductType(ProductType.FOOD).setName("InvoiceTestingProduct").setPrice(new Money(BigDecimal.ONE)).createProduct();
        taxMock = mock(TaxPolicy.class);
        when(taxMock.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(BigDecimal.ONE), "tax"));
    }

    @Test
    public void issuance_Req0Elements() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxMock);
        assertThat(invoice.getItems(), hasSize(0));
    }

    @Test
    public void issuance_Req1Element() {
        invoiceRequest.add(itemBuilder.setProductData(product.generateSnapshot()).setTotalCost(new Money(BigDecimal.ONE)).setQuantity(15).createRequestItem());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxMock);
        assertThat(invoice.getItems(), hasSize(1));
    }

    @Test
    public void issuance_Req2Elements() {
        RequestItem requestItem1 = itemBuilder.setProductData(product.generateSnapshot()).setQuantity(45).setTotalCost(new Money(BigDecimal.ONE)).createRequestItem();
        RequestItem requestItem2 = itemBuilder.setProductData(product.generateSnapshot()).setQuantity(12).setTotalCost(new Money(BigDecimal.TEN)).createRequestItem();
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        bookKeeper.issuance(invoiceRequest, taxMock);
        verify(taxMock).calculateTax(requestItem1.getProductData().getType(), requestItem1.getProductData().getPrice());
        verify(taxMock).calculateTax(requestItem2.getProductData().getType(), requestItem2.getProductData().getPrice());
        verify(taxMock, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    public void issuance_ReqMoreThan1Element() {
        int howManyElements = 5;
        for (int i = 0; i < howManyElements; i++) {
            invoiceRequest.add(new RequestItem(product.generateSnapshot(), 15, new Money(BigDecimal.ONE)));
        }
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxMock);
        assertThat(invoice.getItems(), hasSize(howManyElements));
    }

    @Test
    public void issuance_noElementsBehaviour() {
        bookKeeper.issuance(invoiceRequest, taxMock);
        verify(taxMock, never()).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    public void issuance_OneElementBehaviour() {
        RequestItem requestItem = itemBuilder.setProductData(product.generateSnapshot()).setTotalCost(new Money(BigDecimal.ONE)).setQuantity(15).createRequestItem();
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxMock);
        verify(taxMock).calculateTax(requestItem.getProductData().getType(), requestItem.getProductData().getPrice());
        verify(taxMock, times(1)).calculateTax(any(ProductType.class), any(Money.class));
    }
}
