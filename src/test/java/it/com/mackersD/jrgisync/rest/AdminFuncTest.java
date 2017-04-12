package it.com.mackersD.jrgisync.rest;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.mackersD.jrgisync.rest.Admin;
import com.mackersD.jrgisync.rest.AdminModel;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

public class AdminFuncTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {

        String baseUrl = System.getProperty("baseurl");
        String resourceUrl = baseUrl + "/rest/jrgisync/admin/1.0";

        RestClient client = new RestClient();
        Resource resource = client.resource(resourceUrl);

        AdminModel message = resource.get(AdminModel.class);

        //assertEquals("wrong message","Hello World",message.getMessage());
    }
}
