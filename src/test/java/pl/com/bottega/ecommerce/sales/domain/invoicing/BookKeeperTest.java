package pl.com.bottega.ecommerce.sales.domain.invoicing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    private InvoiceFactory factory;
    @Mock
    private TaxPolicy tp;

    private BookKeeper bk;
    private ClientData cd;
    private InvoiceRequest ir;
    private Product p;
    private RequestItem ri;


    @BeforeEach
    void setUp() throws Exception {
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        when(tp.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"default"));
        bk=new BookKeeper(factory);
        cd=new ClientData(Id.generate(),"default");
        ir=new InvoiceRequestBuilder().build();
        p=new ProductBuilder().build();
        ri=new RequestItemBuilder().build();
    }

    @Test
    void test() {
        fail("not implemented");
    }

}
