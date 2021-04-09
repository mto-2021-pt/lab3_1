package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.Date;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private ClientData clientData;
    private TaxPolicy taxPolicy;
    private Money money;
    private  RequestItem requestItem;
    private Invoice invoice;
    private ProductData productData;
    private Tax tax;
    private BookKeeper bookKeeper;


    @BeforeEach
    void setUp() throws Exception {
        money = Money.ZERO;
        clientData = new ClientData(Id.generate(),"name");
        invoiceRequest = new InvoiceRequest(clientData);
        invoiceFactory = Mockito.mock(InvoiceFactory.class);
        tax = new Tax(money, "money");
        taxPolicy = Mockito.mock(TaxPolicy.class);
        productData = new ProductData(Id.generate(),money,"name", ProductType.STANDARD,new Date());
        requestItem = new RequestItem(productData,0,money);
    }
    
    @Test
    void test_case_1() {
        when(taxPolicy.calculateTax(any(),any())).thenReturn(tax);
        when(invoiceFactory.create(any())).thenReturn( new Invoice(Id.generate(),null) );
        
        bookKeeper = new BookKeeper(invoiceFactory);
        invoiceRequest.add(requestItem);
        invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assertions.assertTrue(invoice.getItems().size() == 1);
    }
    
    @Test
    void test_case_2(){
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);
        when(invoiceFactory.create(any())).thenReturn( new Invoice(Id.generate(),null) );
        
        bookKeeper = new BookKeeper(invoiceFactory);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);
        
        Mockito.verify(taxPolicy,times(2)).calculateTax(any(),any());
    }

}
