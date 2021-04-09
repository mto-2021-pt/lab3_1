package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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


}
