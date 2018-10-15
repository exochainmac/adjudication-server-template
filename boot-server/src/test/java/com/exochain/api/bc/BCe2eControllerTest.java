package com.exochain.api.bc;

import com.exochain.api.bc.fsm.BcLoginContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"h2db", "dev"})
public class BCe2eControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void shouldNotErrorOnEmailValidation() throws Exception {
        MvcResult result = mvc.perform(post("/api/bc/mfltest")
                .param("initToken", healthyLongLivedToken())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn();

        Map<String, Object> models = result.getModelAndView().getModel();

        final String loginContextAttributeKey = "bcLoginContext";
        assertThat(models.containsKey(loginContextAttributeKey)).isTrue();

        Object loginContext = models.get(loginContextAttributeKey);

        BcLoginContext ctx = (BcLoginContext) loginContext;

        mvc.perform(get("/api/bc/mfltest/forceemailauth")
                .flashAttr(loginContextAttributeKey, ctx))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/api/bc/mfl/validateCell"));
    }

    private String healthyLongLivedToken() {
        return "eyJraWQiOiI0ZWQxNWQ4MS00ZmVlLTRjZmEtODlkMC1hZTU4ZmRjMjY2OTgiLCJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.Z1DPO9B-8ASy-zlVVCVumXWsEGtlDV1HbEPZ6753Ii064WxN-7pJgBzaDurX8enuFP-NY9T28X949kxzsLcygbz4XS7z_gClgJVQOhM79-rqHrwWOzYnRk1rD5Z4KMFRHes_LDKJCy-96DkpUFiGZ9EqyjkJ82kUpgm2WGFdt7UsMOQ7kHOwaSNhwraoQNI0fbe8YJk_2IuFRkMYQiDIaJDnJa8uA_3Ij6q9esl7wybQmSDv5SHNSGORPtA_enEgjT-1yOMd9p6yK-TlBjaYK-G5LvMDFHTqt0VGE9xEsO7UHetA281kzURmGTXZULfrrgm1yQ-gYRx-vfOwZhDtMg.W2lQWQUdWJKz5EuN.gvoVGS5lD_znOP0bErU-9heCPlnJ0h7YYlor-97_WS59rVW6lp4XGLqzp4X_k3Zd_-Xtxww3pH4BI7zh_mwvbP0Q4DWrQvmY3ATVuZYUPbxrqe2ylOeli6b_klWpWNrMv83kIB82dB4RgkBNPiQcLNJhH3hfZ4vJzgGEezZfPBvzOQqN68cXDgnImB4CNIi4D5WLK9suaVKropOYNhV00c5z24QeQd3t0Mc523HSYeygcWqit-gxnElB5kKikERY3O1Kl03LdUbNgnNh_kwtlEOcE8Rv55nvq2NcTMAGN9HWoxjSh5HOzHyrWW-hchAIP8LV4EiWN8APk7wW5K26nX68bW96TVKdW0M9qyE_uhWC8VUmo87xc2rVROGdqrv-8yXuUYwRUr9IlKC89Z4mE8-Zn7HLBR2bNs6RZEQ4pBclGGeujlb-anmi2GaWzmYedn7iE-vWQ_y9eUdlUp7j3KAhi3-UQIb9h5b4sXKjs48Hc99cWAALMnwMwFJnQDLSWmeDgZtHUtZUt9TNUerE2-XOnprHtnX2O2p2fxLH2kfVMgFR41DkJp_0yY5mDRkOPCXtT4yWVCjAa1VzDj2mSjbzkysbZzHVYODhLCfyn0qAS-VswdJ9ZpLzNjFfRLjEQ1OgW7JN28KSMssuolzDPWupdaU5TWuhX-ZxhjCDp6uJ057Xccxqx0ymJhnjqm_Vdm5WFWJLIUBNHndAfa2T9vyTGy0u2PWOiAMRCzzLZch2TR__QJ6io4eJepWPYpMdAxbIEwfw0DmKc74rxpkSUPcwWQYd6ZkE9cDKl0T6axX1pKEgAoYiUTjC3PF4WEOpilVHLqM5EVmk7OSSyvROwDXNimMOuXXwvRh95WKntU_EUBULTL8EeWNyFgApvQpGBqwDDoiW679U0qLA7_Vxexj3_HsQSnOoMlbZhkl7Q-Fv3lkLoPJh6lfFfj5x3GiTkCE14P5lpS5TcQNtQkcl_FRF5PGUrRdCYw8rvxQPjf_MtoIwxGiDjupUc-9wVjJ-YfPGsgZI_39m6xULawHNLZuOIypg8HB537yhbbrm8KRd6K1-Gfw27MXRd2HWt2FQWBisIoHO3d20QNSCCme2j-Yy6EehAny-psEX-8clIeIRfJLZjVjH1_5X5eI83y1rr-xv.5sT82TvcGjj6ibUHZCncKQ";
    }
}
