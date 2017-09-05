package com.sug.core.rest.client;

import org.junit.Test;

/**
 * Created by Administrator on 2014/10/21.
 */
public class RESTServiceClientTest {

    @Test
    public void getTest() throws Exception {
        HttpBuilder httpBuilder = new HttpBuilder("http://localhost:9090/", "960e284150d5a4611df0439c4e96ef08","eFR6PiAY4JOcC03+EYVGMePcPTaAsGSIznTOj34x6SO/bCXzkowdNFN75K1SHfMtiMxvqOJnqK7LxugMecy+Ww==", null, null);

        RESTServiceClient client = new RESTServiceClient(httpBuilder);

        //VisitorTest v = client.get("/visitor/create", VisitorTest.class);

       // System.out.println(v.toString());

    }

    @Test
    public void getTest2() throws Exception {
        HttpBuilder httpBuilder = new HttpBuilder("http://localhost:8080/",
                "960e284150d5a4611df0439c4e96ef08",
                "eFR6PiAY4JOcC03+EYVGMePcPTaAsGSIznTOj34x6SO/bCXzkowdNFN75K1SHfMtiMxvqOJnqK7LxugMecy+Ww==", null, null);


        //httpBuilder.setToken("MzFiNWFmZTdmNGIzNDFiYmE4ZDE1NjQxOGViZTBlNDU=");

        RESTServiceClient client = new RESTServiceClient(httpBuilder);

        //VisitorTest v = client.get("/customer", VisitorTest.class);

        //System.out.println(v.toString());

    }

}
