package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

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
    @BeforeEach
    void setUp() throws Exception {
            bookKeeper=new BookKeeper(factory);
    }

    @Test
    void test() {
        ClientData clientData= new ClientData(Id.generate(),"klient");
        InvoiceRequest request = new InvoiceRequest(clientData);
        Invoice invoice = new Invoice(Id.generate(), clientData);
        when(factory.create(clientData)).thenReturn(invoice);

        bookKeeper.issuance(request,taxPolicy);
        verifyNoInteractions(taxPolicy);

    }
    @Test
    void invoiceRequestReturnOneItem() {
        ClientData clientData= new ClientData(Id.generate(),"oneClient");
        InvoiceRequest request = new InvoiceRequest(clientData);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(5))).thenReturn(new Tax(new Money(0.05),"5%"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);

        RequestItem requestItem = new RequestItem(productData,1,new Money(5));
        request.add(requestItem);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);
        bookKeeper.issuance(request,taxPolicy);

        assertEquals(invoice.getItems().size(),1);
    }

    @Test
    void invoiceRequestReturnTwoItems() {
        ClientData clientData= new ClientData(Id.generate(),"oneClientTwoItems");
        InvoiceRequest request = new InvoiceRequest(clientData);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(5))).thenReturn(new Tax(new Money(0.05),"5%"));
        when(taxPolicy.calculateTax(ProductType.FOOD,new Money(16))).thenReturn(new Tax(new Money(0.025),"2.5%"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);
        ProductData productDataFood=mock(ProductData.class);
        when(productDataFood.getType()).thenReturn(ProductType.FOOD);

        RequestItem requestItem = new RequestItem(productData,3,new Money(5));
        request.add(requestItem);
        RequestItem requestItem2 = new RequestItem(productDataFood,7,new Money(16));
        request.add(requestItem2);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);
        bookKeeper.issuance(request,taxPolicy);

        Mockito.verify(taxPolicy,times(1)).calculateTax(ProductType.STANDARD, new Money(5));
        Mockito.verify(taxPolicy,times(1)).calculateTax(ProductType.FOOD,new Money(16));
    }
    @Test
    void invoiceRequestReturnTwoSameItems() {
        ClientData clientData= new ClientData(Id.generate(),"oneClientTwoSameItems");
        InvoiceRequest request = new InvoiceRequest(clientData);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(100))).thenReturn(new Tax(new Money(0.23),"23%"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);


        RequestItem requestItem = new RequestItem(productData,2,new Money(100));
        request.add(requestItem);
        request.add(requestItem);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);
        bookKeeper.issuance(request,taxPolicy);

        Mockito.verify(taxPolicy,times(2)).calculateTax(ProductType.STANDARD, new Money(100));
    }
    @Test
    void invoiceRequestReturnItems() {
        ClientData clientData= new ClientData(Id.generate(),"oneClientTwoItems");
        InvoiceRequest request = new InvoiceRequest(clientData);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(100))).thenReturn(new Tax(new Money(0.23),"23%"));
        when(taxPolicy.calculateTax(ProductType.FOOD,new Money(16))).thenReturn(new Tax(new Money(0.025),"2.5%"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);
        ProductData productDataFood=mock(ProductData.class);
        when(productDataFood.getType()).thenReturn(ProductType.FOOD);

        RequestItem requestItem = new RequestItem(productData,2,new Money(100));
        request.add(requestItem);
        request.add(requestItem);
        RequestItem requestItem2 = new RequestItem(productDataFood,7,new Money(16));
        request.add(requestItem2);
        request.add(requestItem2);

        Invoice invoice = new Invoice(Id.generate(),clientData);
        when(factory.create(clientData)).thenReturn(invoice);
        bookKeeper.issuance(request,taxPolicy);

        Mockito.verify(taxPolicy,times(2)).calculateTax(ProductType.STANDARD, new Money(100));
        Mockito.verify(taxPolicy,times(2)).calculateTax(ProductType.FOOD,new Money(16));
    }

}