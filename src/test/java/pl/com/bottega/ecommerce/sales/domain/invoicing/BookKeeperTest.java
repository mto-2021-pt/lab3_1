package pl.com.bottega.ecommerce.sales.domain.invoicing;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    }

    @Test
    void state_testsuccessful_oneiteminvoicerequest(){
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        when(tp.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"default"));
        ir.add(ri);
        assertEquals(1,bk.issuance(ir,tp).getItems().size());
    }

    @Test
    void behaviour_testsuccessful_twoitemsinvoicerequest(){
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        when(tp.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"default"));
        ir.add(ri);
        ir.add(ri);
        bk.issuance(ir,tp);
        verify(tp,times(2)).calculateTax(ProductType.STANDARD,Money.ZERO);
    }

    @Test
    void state_testsuccessful_noitemsinvoicerequest(){
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        assertEquals(0, bk.issuance(ir, tp).getItems().size());
    }

    @Test
    void behaviour_testsuccessful_threeitemsinvoicerequest(){
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        when(tp.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"default"));
        ir.add(ri);
        ir.add(ri);
        ir.add(ri);
        bk.issuance(ir,tp);
        verify(tp,times(3)).calculateTax(ProductType.STANDARD,Money.ZERO);
    }

    @Test
    void behaviour_testunsuccessful_invoicerequestisnull(){
        ir=null;
        assertThrows(NullPointerException.class,()->bk.issuance(ir,tp));
    }

    @Test
    void state_testsuccessful_oneiteminvoicerequest_requestitemquantityisnegative(){
        when(factory.create(cd)).thenReturn(new Invoice(Id.generate(),cd));
        when(tp.calculateTax(ProductType.STANDARD, Money.ZERO)).thenReturn(new Tax(Money.ZERO,"default"));
        ri=new RequestItemBuilder().withQuantity(-1).withProductData(p.generateSnapshot()).build();
        ir.add(ri);
        assertEquals(1, bk.issuance(ir, tp).getItems().size());
    }


}
