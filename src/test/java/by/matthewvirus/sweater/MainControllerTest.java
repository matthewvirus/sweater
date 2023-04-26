package by.matthewvirus.sweater;

import by.matthewvirus.sweater.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WithUserDetails("Mot")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller;

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div/div/form/a").string("Mot"));
    }

    @Test
    public void messageListTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='masonry']/div").nodeCount(5));
    }

    @Test
    public void filterMessageTest() throws Exception {
        this.mockMvc.perform(get("/messages").param("filter", "tag1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='masonry']/div").nodeCount(2))
                .andExpect(xpath("//div[@id='masonry']//div[@data-id='2']").exists())
                .andExpect(xpath("//div[@id='masonry']//div[@data-id='5']").exists());
    }

    @Test
    public void addMessageToList() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/messages")
                .file("file", "123".getBytes())
                .param("text", "six")
                .param("tag", "tag2")
                .with(csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='masonry']/div").nodeCount(6))
                .andExpect(xpath("//div[@id='masonry']//div[@data-id='10']").exists())
                .andExpect(xpath("//div[@id='masonry']//div[@data-id='10']/div/div/span").string("six"))
                .andExpect(xpath("//div[@id='masonry']//div[@data-id='10']/div/div/i").string("tag2"));
    }
}
