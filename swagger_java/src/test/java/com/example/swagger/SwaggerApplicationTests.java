package com.example.swagger;

import com.example.swagger.common.R;
import com.example.swagger.controller.ApiController;
import com.example.swagger.entity.SwaggerData;
import com.example.swagger.service.ApiControllerService;
import com.example.swagger.service.SwaggerDataService;
import com.example.swagger.service.impl.ApiControllerServiceImpl;
import com.example.swagger.service.impl.SwaggerDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.apache.coyote.http11.Constants.a;

@Slf4j
@SpringBootTest
class SwaggerApplicationTests {
    @Autowired
    private ApiControllerService apiControllerService;
    @Autowired
    private SwaggerDataService swaggerDataService;
    @Test
    public void Test() throws IOException {
        HashMap<String, String> params = new HashMap<>();
        params.put("a", "1111");
        params.put("b", "2222");
        params.put("c", "3333");
        ApiController.Response stringBuffer = apiControllerService.GetRequestSender("http://localhost:8080/test", null, params);
        log.info(String.valueOf(stringBuffer));
//        return null;
    }
    @Test
    public void PostRequestSender() {
        HashMap<String, String> params = new HashMap<>();
        params.put("a", "1111");
        params.put("b", "2222");
        params.put("c", "3333");

        String stringBuffer;
        ApiController.Response response;
        try {
            response = apiControllerService.PostRequestSender("http://localhost:8080/test", null, params);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(String.valueOf(response));
    }

@Test
    public void SwaggerDataServiceTest(){
    List<SwaggerData> analyze = swaggerDataService.analyze();
    System.out.println(analyze);

}
}
