package pl.com.bottega.ecommerce.sales.domain.invoicing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
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
        bk=new BookKeeper(factory);
        cd=new ClientData(Id.generate(),"default");
        ir=new InvoiceRequestBuilder().withClient(cd).build();
        p=new ProductBuilder().build();
        ri=new RequestItemBuilder().withProductData(p.generateSnapshot()).withQuantity(1).build();
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        when(tp.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"default"));
    }

    @Test
    void testsuccessful_oneiteminvoicerequest(){
        ir.add(ri);
        assertEquals(1,bk.issuance(ir,tp).getItems().size());
    }

    @Test
    void testsuccessful_twoitemsinvoicerequest(){
        ir.add(ri);
        ir.add(ri);
        bk.issuance(ir,tp);
        verify(tp,times(2)).calculateTax(ProductType.STANDARD,Money.ZERO);
    }



}
