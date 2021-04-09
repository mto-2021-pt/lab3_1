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
        ClientData client = new ClientData(Id.generate(),"name");
        InvoiceRequest request=new InvoiceRequest(client);
        Invoice invoice= new Invoice(Id.generate(),client);
        when(factory.create(client)).thenReturn(invoice);

        bookKeeper.issuance(request,taxPolicy);
        verifyNoInteractions(taxPolicy);
    }

    @Test
    void invoiceShouldReturnOnePosition()
    {
        ClientData client = new ClientData(Id.generate(),"dominik");
        InvoiceRequest request=new InvoiceRequest(client);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(5))).thenReturn(new Tax(new Money(0.05),"5%"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);

        RequestItem item=new RequestItem(productData,1,new Money(5));

        request.add(item);


        Invoice invoice = new Invoice(Id.generate(),client);
        when(factory.create(client)).thenReturn(invoice);

        bookKeeper.issuance(request,taxPolicy);


        assertEquals(invoice.getItems().size(),1);
    }


    @Test
    void invoiceWithTwoPositionTwoTimesCalculateTax()
    {
        ClientData client = new ClientData(Id.generate(),"dominik");
        InvoiceRequest request=new InvoiceRequest(client);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(5))).thenReturn(new Tax(new Money(0.05),"5%"));


        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);

        ProductData productData2=mock(ProductData.class);
        when(productData2.getType()).thenReturn(ProductType.STANDARD);

        RequestItem item=new RequestItem(productData,1,new Money(5));

        request.add(item);

        RequestItem item2=new RequestItem(productData2,6,new Money(5));

        request.add(item2);

        Invoice invoice = new Invoice(Id.generate(),client);
        when(factory.create(client)).thenReturn(invoice);

        bookKeeper.issuance(request,taxPolicy);

        Mockito.verify(taxPolicy,times(2)).calculateTax(ProductType.STANDARD,new Money(5));


    }

    @Test
    void invoiceShouldReturnZeroPosition()
    {
        ClientData client = new ClientData(Id.generate(),"dominik");
        InvoiceRequest request=new InvoiceRequest(client);

        Invoice invoice = new Invoice(Id.generate(),client);
        when(factory.create(client)).thenReturn(invoice);

        bookKeeper.issuance(request,taxPolicy);


        assertEquals(invoice.getItems().size(),0);
    }

    @Test
    void invoiceShouldReturnTwoPosition()
    {
        ClientData client = new ClientData(Id.generate(),"dominik");
        InvoiceRequest request=new InvoiceRequest(client);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.STANDARD,new Money(5))).thenReturn(new Tax(new Money(0.05),"5%"));
        when(taxPolicy.calculateTax(ProductType.FOOD,new Money(21))).thenReturn(new Tax(new Money(0.03),"3%"));

        ProductData productData=mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.STANDARD);

        ProductData productData2=mock(ProductData.class);
        when(productData2.getType()).thenReturn(ProductType.FOOD);

        RequestItem item=new RequestItem(productData,1,new Money(5));

        request.add(item);

        RequestItem item2=new RequestItem(productData2,1,new Money(21));

        request.add(item2);


        Invoice invoice = new Invoice(Id.generate(),client);
        when(factory.create(client)).thenReturn(invoice);

        bookKeeper.issuance(request,taxPolicy);

        assertEquals(invoice.getItems().size(),2);
    }




}
