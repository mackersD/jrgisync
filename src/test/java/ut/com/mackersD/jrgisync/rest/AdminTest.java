package ut.com.mackersD.jrgisync.rest;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.mackersD.jrgisync.rest.Admin;
import com.mackersD.jrgisync.rest.AdminModel;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericEntity;

public class AdminTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {
        Admin resource = new Admin();

        Response response = resource.getMessage();
        final AdminModel message = (AdminModel) response.getEntity();

        //assertEquals("wrong message","Hello World",message.getMessage());
    }
}
