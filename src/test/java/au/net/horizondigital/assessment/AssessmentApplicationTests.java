package au.net.horizondigital.assessment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AssessmentApplication.class)
@AutoConfigureMockMvc
class AssessmentApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void configureShop() throws Exception {
        // To avoid time, simply using json string for test request
        String request = "{\"contactNumber\":\"8989898\",\"location\":\"Lahore\",\"numberOfQueues\":1,\"queueMaxSize\":3,\"openingTime\":\"10:00:00\",\"closingTime\":\"22:00:00\",\"coffeeMenu\":{\"menuItems\":[{\"name\":\"latte\",\"price\":250}]}}\n";

        MvcResult mvcResult = mockMvc.perform(post("/configureShop").contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().isOk()).andReturn();

        assertEquals(mvcResult.getResponse().getContentAsString(), "Shop Configured Successfully");
    }



}
