package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {


    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private ClientData clientData;
    private TaxPolicy taxPolicy;
    private Money money;
    private  RequestItem requestItem;
    ProductData productData;
    private Tax tax;
    private BookKeeper bookKeeper;


    @BeforeEach
    void setUp() throws Exception {
        clientData = new ClientData(Id.generate(),"name");
        invoiceRequest = new InvoiceRequest(clientData);
        money = Money.ZERO;
        ProductData productData = new ProductData(Id.generate(),money,"name", ProductType.STANDARD,new Date());
        requestItem = new RequestItem(productData,0,money);
        
        invoiceFactory = Mockito.mock(InvoiceFactory.class);
        tax = new Tax(money, "text");
        taxPolicy = Mockito.mock(TaxPolicy.class);





    }

    @Test
    void oneItemInvoiceTest() {
        Mockito.when(taxPolicy.calculateTax(any(),any())).thenReturn(tax);
        Mockito.when(invoiceFactory.create(any())).thenReturn( new Invoice(Id.generate(),null) );
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        invoiceRequest.add(requestItem);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assert.assertTrue(invoice.getItems().size() == 1);
    }

}
