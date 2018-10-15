package com.exochain.api.bc;

import com.exochain.api.bc.domain.Adjudication;
import com.exochain.api.bc.domain.AdjudicationRequest;
import com.exochain.api.bc.domain.Attestation;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class BCControllerTest {

    private MockMvc mvc;

    //@Test
    public void shouldSupportMultiFactorAuthentication() throws Exception {
/*
        Adjudicator dummyAdjudicator = request -> {
            Adjudication adjudication = new Adjudication();
            Attestation attestation = new Attestation();
            attestation.setResult(Attestation.Result.CONFIRMED);
            adjudication.setAttestation(attestation);
            return adjudication;
        };

        mvc = MockMvcBuilders.standaloneSetup(new BCController(dummyAdjudicator, null, null, null, null)).build();

        MvcResult result = mvc.perform(post("/api/bc/mfl")
                .param("initToken", healthyLongLivedToken())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertThat(result).isNotNull();
*/
    }

    //@Test
    public void shouldAwaitOTPEntryWhenSubjectIsUnconfirmed() throws Exception {
/*
        Adjudication otpRequiredAdjudication = new Adjudication();

        Attestation otpRequired = new Attestation();
        otpRequired.setResult(Attestation.Result.OTP_REQUIRED);
        otpRequiredAdjudication.setAttestation(otpRequired);

        Adjudicator otpSignalingAdjudicator = mock(Adjudicator.class);
        when(otpSignalingAdjudicator.adjudicate(any(AdjudicationRequest.class))).thenReturn(otpRequiredAdjudication);

        mvc = MockMvcBuilders.standaloneSetup(new BCController(otpSignalingAdjudicator, null, null, null, null)).build();

        final String ipAddress = "75.58.7.149";

        String token = healthyLongLivedToken();

        MvcResult result = mvc.perform(post("/api/bc/mfl")
                .param("initToken", token)
                .header("X-Forwarded-For", ipAddress)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/api/bc/mfl/otpVerification"))
                .andReturn();

        ArgumentCaptor<AdjudicationRequest> captor = ArgumentCaptor.forClass(AdjudicationRequest.class);

        verify(otpSignalingAdjudicator).adjudicate(captor.capture());

        assertThat(result).isNotNull();

        AdjudicationRequest request = captor.getValue();

        assertThat(request).isNotNull();
        assertThat(request.getToken()).isEqualTo(token);
        assertThat(request.getCallerIPAddress()).isEqualTo(ipAddress);
*/
    }

    //@Test
    public void shouldSupportOneTimePasscodeVerification() throws Exception {
/*
        Adjudication idConfirmedAdjudication = new Adjudication();

        Attestation confirmed = new Attestation();
        confirmed.setResult(Attestation.Result.CONFIRMED);
        idConfirmedAdjudication.setAttestation(confirmed);

        final String ipAddress = "75.58.7.149";

        Adjudicator idConfirmedAdjudicator = mock(Adjudicator.class);
        when(idConfirmedAdjudicator.adjudicate(any(AdjudicationRequest.class))).thenReturn(idConfirmedAdjudication);

        // TODO: fix nulls I added as a quick compile fix
        mvc = MockMvcBuilders.standaloneSetup(new BCController(idConfirmedAdjudicator, null, null, null, null)).build();

        String token = healthyLongLivedToken();
        String otp = "dummyOTP";

        MvcResult result = mvc.perform(post("/api/bc/mfl/verifyOtp")
                .param("initToken", token)
                .param("otp", otp)
                .header("X-Forwarded-For", ipAddress)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/api/bc/mfl/authFinalize"))
                .andReturn();

        assertThat(result).isNotNull();

        ArgumentCaptor<AdjudicationRequest> captor = ArgumentCaptor.forClass(AdjudicationRequest.class);

        verify(idConfirmedAdjudicator).adjudicate(captor.capture());

        assertThat(result).isNotNull();

        AdjudicationRequest request = captor.getValue();

        assertThat(request).isNotNull();
        assertThat(request.getToken()).isEqualTo(token);
        assertThat(request.getCallerIPAddress()).isEqualTo(ipAddress);
        assertThat(request.getOtp()).isEqualTo(otp);
*/
    }

    private String healthyLongLivedToken() {
        return "eyJraWQiOiI0ZWQxNWQ4MS00ZmVlLTRjZmEtODlkMC1hZTU4ZmRjMjY2OTgiLCJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.cuxqJJRgvLOE6tsMNnS2eRVdOj_gqkPgD49m9bSqW7xRUWJB0jn6Mj3FK2L7cgghSYjdt5ILkZMMOLke-A43cbqEEpnf4-lBQ6vCdcs8l79WWxHBT-1iIruefyMNsYU6jBGXVpPfRLf_HId5H-QQs5IAtNFQMsp8wWFEggL8HUU75bXQIZzngtU0iQpqMIbfjWnx2GKLUZHJMWwe7fzPJi4p8wNfp-hDjGiuomx-85SUKqo6lfjd3ga3YZx4aQ1yHXmhyvLAHwpqX74ebKiEzV40jTnY-DsfCiDETs4Ddh3_Tw-rqTu_I_6v2FLkERUrxiLT-cqsOw2ebpMdSjoWgA.nIqzzYk9kqRG8kbr.ckJ1fV_C0CRSZJFAXjrjQ5BVbJ0dpg5ZUY7PKezRYcYZVpLG9Ah1HIdNemp6qQgr3DtUyvNrwb6F4PXH-TlVoggHVdiu89wrEIppp_TaJxQCUALEgrxiY0WN8d9NNu-H1RtwOc8-4vvaTLI9D6Vj8LPcK651F-uQ4PtmMHb9Yc9h04XCjhflPWn1VVNgkNEoqrMCqaANCsXTk0YRuK-1NZ7Ms_L0h0LWX7ifMCVZBtoiW4jTBHd_nrYXkTelH22E44NR2WXEMReSr8NZ8XdtZ5Bn7DCLz3yIRKWiwcKaVAKzchLzBujmR3alE_oK4iS4gRgaY-NkS1lMqW9mCcK0BkDLOFtR-iHunlE7kL88fpo3Fi1rGJDOwNq04RHxt58DkGerF6Iqb9UV3DuL77IlcJdHTMjzANiD8ay7I3TXQxUsTerlcfuhNcvxrr0Kc1BjSW4CFhOSVgMafzrJKvxemY-mFf_NblOBTT0cet_-IlLB2VbGrSC31ojB8YKJASDHHNkMyVHEAP6CHDX3fbVtG84bvLWFi5KKrkp390343goH0uYUdUadXj-C4-BCmuUVqXGTico9OWRY0opSbUkHq5uOJJZ1PkyOgZJw-9AbYLfE6saBMME7iynG7pJYtvKz18GvEDPGlA1FOCTg7gmjWqjHF25NPOY0CBjTLwB_cwNxTsgdXI57TPEY4fUHqsAhsyjrpsSAWnnbdo2EPIVoWxrMhASR1TgKAB8uOMASn9toi66RaDaN2VfdLSzZeVDjJcm0JWXz96TJ2WMmLQudJyEfd1hjQIzAKq0w166L65adBQscG4LIPICqQvynFBxO0aDAT0KQDm7_0yaLaxRbNd-tLKZ4xBYg2-sCa4f0dkwZaMXWd8uXtEmRhYVkj1DKosG2rhPz6iGG_dGm21FV9G0yb6nbWlfCHlyWf-FQrb4cd-16TzA0dMxC0p7WqE7N_UgqD2QfsFJb7LaYeRwuk4vtICQwvKSR-sN0XoqVkwQcwtMCbGxzpsRrr2oOxBvCElntd1GsRuI_CkzULsncj5ulOPVFe5Ft6a0hv00Ap-Qi-hWUVwAeG9Uqy_N26KLaSggP03xUGfG0CC4ZHlAx3SfluNdpOzK1dHTXOgEc-J4SB0NAuIfqBD4YkHDRyZ7UEFDBYB7_7n9zMphhUcGWg_XO5_yU3XljRjL17Mz_Z8c.PEa1c_tctgjuodFxqhd-_w";
    }
}