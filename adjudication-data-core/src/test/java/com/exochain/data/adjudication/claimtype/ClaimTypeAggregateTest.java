package com.exochain.data.adjudication.claimtype;

import com.exochain.data.adjudication.claimtype.api.ClaimTypeCreatedEvt;
import com.exochain.data.adjudication.claimtype.api.CreateClaimTypeCmd;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ClaimTypeAggregateTest {
    private AggregateTestFixture<ClaimTypeAggregate> testFixture;
    private static final String EMAIL_CLAIM_UUID = "4021a193-2963-4223-b2b8-ca42c655bce0";
    private static final String EMAIL_CLAIM_TITLE = "Control Of Email Account";
    private static final String EMAIL_CLAIM_DESCRIPTION = "The user has demonstrated they control the given email account";

    @BeforeMethod
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(ClaimTypeAggregate.class);
    }

    @Test
    public void testCreateClaimDefinition () {
        CreateClaimTypeCmd cmd = buildEmailClaimTypeCmd();
        ClaimTypeCreatedEvt expectedEvent = new ClaimTypeCreatedEvt(cmd);

        testFixture.givenNoPriorActivity()
                .when(cmd)
                .expectEvents(expectedEvent);
    }

    private CreateClaimTypeCmd buildEmailClaimTypeCmd() {
        return new CreateClaimTypeCmd(new ClaimTypeId(EMAIL_CLAIM_UUID), EMAIL_CLAIM_TITLE, EMAIL_CLAIM_DESCRIPTION);
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

}