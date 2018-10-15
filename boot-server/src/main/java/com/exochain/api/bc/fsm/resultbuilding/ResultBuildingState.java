package com.exochain.api.bc.fsm.resultbuilding;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.config.ApiAccountSettings;
import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ClaimExtractor;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.catastrophicerror.CatastrophicErrorEvent;
import com.exochain.api.bc.fsm.resultready.ResultReadyEvent;
import com.exochain.jwt.claims.ClaimNames;
import com.exochain.jwt.processing.TokenEncrypter;
import com.exochain.jwt.processing.TokenSigner;
import com.exochain.result.exception.ExoStructuredException;
import com.exochain.result.meta.ResultMeta;
import com.exochain.result.meta.WarnLoggedResultMeta;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.StringUtils;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class ResultBuildingState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(ResultBuildingState.class);
    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();
    private static final String[] BLANK_STRING_ARRAY = {""};
    private static final Float DEFAULT_SCORE_UPON_ERROR = 0.0f;
    private static final Float DEFAULT_SCORE_UPON_SUCCESS = 3.0f;

    public static final int CODE_GROUP_SUCCESS = 200;

    private final TokenEncrypter tokenEncrypter;
    private final TokenSigner tokenSigner;

    public ResultBuildingState(TokenSigner tokenSigner, TokenEncrypter tokenEncrypter) {
        super("resultBuildingState");
        this.tokenSigner = tokenSigner;
        this.tokenEncrypter = tokenEncrypter;
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();
        L.debug("Executing resultBuildingState action");
        Event resultEvent;
        /*
        Pull everything together and assemble the final token, whether error or other.
         */

        ResultMeta rm = ctx.getResultMeta();
        if(null == rm) {
            // Shouldn't get here
            rm = new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, "Resultmeta is null in final ResultBuildingState");
            ctx.setResultMeta(rm);
        }

        try {
            String serializedToken;
            if (rm.getCodeGroup() == CODE_GROUP_SUCCESS) {
                serializedToken = buildSuccessToken(ctx);
            } else {
                serializedToken = buildFailureToken(ctx);
            }
            resultEvent = new ResultReadyEvent(ctx, serializedToken);
        } catch (ExoStructuredException e) {
            // Error where we can't construct the final result
            resultEvent = new CatastrophicErrorEvent(ctx, e);
        }

        return resultEvent;
    }

    private String buildSuccessToken(BcLoginContext ctx) throws ExoStructuredException {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        populateCommonClaims(ctx, builder);
        populateSuccessClaims(ctx, builder);

        return createSerializedResponseToken(ctx, builder.build());
    }

    private void populateSuccessClaims(BcLoginContext ctx, JWTClaimsSet.Builder builder) {
        AuthenticationFactor emailAuth = ctx.getAccount().getEmailFactor();
        AuthenticationFactor cellAuth = ctx.getAccount().getCellFactor();

        builder.claim(ClaimNames.EX_SUBJECT_EMAILS.getClaimName(), new String[]{emailAuth.getOtpAddress()})
                .claim(ClaimNames.EX_SUBJECT_CELLS.getClaimName(), new String[]{cellAuth.getOtpAddress()})
                .claim(ClaimNames.EX_SUBJECT_FNAME.getClaimName(), StringUtils.EMPTY)
                .claim(ClaimNames.EX_SUBJECT_LNAME.getClaimName(), StringUtils.EMPTY)
                .claim(ClaimNames.EX_ODENTITY_SCORE.getClaimName(), DEFAULT_SCORE_UPON_SUCCESS);
    }

    private String buildFailureToken(BcLoginContext ctx) throws ExoStructuredException {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        populateCommonClaims(ctx, builder);
        populateFailureClaims(ctx, builder);

        return createSerializedResponseToken(ctx, builder.build());
    }

    private String createSerializedResponseToken(BcLoginContext ctx, JWTClaimsSet claimsSet) throws ExoStructuredException {

        String responseToken = null;
        try {
            SignedJWT embeddedJWT = tokenSigner.createSignedJWT(claimsSet);
            JWEObject jweObject = tokenEncrypter.createEncryptedJWE(embeddedJWT);
            responseToken = jweObject.serialize();
        } catch (JOSEException e) {
            throw new ExoStructuredException(new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, "Error creating final serialized token: [{}]", e.getMessage()), e);
        }

        return responseToken;
    }

    private void populateFailureClaims(BcLoginContext ctx, JWTClaimsSet.Builder builder) {
        builder.claim(ClaimNames.EX_SUBJECT_EMAILS.getClaimName(), BLANK_STRING_ARRAY)
                .claim(ClaimNames.EX_SUBJECT_CELLS.getClaimName(), BLANK_STRING_ARRAY)
                .claim(ClaimNames.EX_SUBJECT_FNAME.getClaimName(), StringUtils.EMPTY)
                .claim(ClaimNames.EX_SUBJECT_LNAME.getClaimName(), StringUtils.EMPTY)
                .claim(ClaimNames.EX_ODENTITY_SCORE.getClaimName(), DEFAULT_SCORE_UPON_ERROR);

    }

    private void populateReturnCodeClaims(JWTClaimsSet.Builder builder, ResultMeta rMeta) {
        builder.claim(ClaimNames.EX_RC_CODE_GROUP.getClaimName(), new Integer(rMeta.getCodeGroup()))
                .claim(ClaimNames.EX_RC_CODE.getClaimName(), new Integer(rMeta.getCode()))
                .claim(ClaimNames.EX_RC_MESSAGE.getClaimName(), rMeta.getDescription())
                .claim(ClaimNames.EX_RC_LOG_ID.getClaimName(), rMeta.getLogId());
    }

    private void populateCommonClaims(BcLoginContext ctx, JWTClaimsSet.Builder builder) {
        JWTClaimsSet partnerClaims = ctx.getJwtClaimsSet();
        OffsetDateTime issuedAt = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ApiAccountSettings apiAccountSettings = ctx.getApiAccountSettings();
        OffsetDateTime expirationTime = issuedAt.plusSeconds(apiAccountSettings.getExoJwtExpirationSecs());

        // Build standard claims common to every response
        builder.jwtID(UUID_GENERATOR.generate().toString())
                .subject(partnerClaims.getSubject())
                .audience(apiAccountSettings.getClientAudience())
                .issuer(apiAccountSettings.getExoApiUrl())
                .issueTime(Date.from(issuedAt.toInstant()))
                .notBeforeTime(Date.from(issuedAt.toInstant()))
                .expirationTime(Date.from(expirationTime.toInstant()));

        String opaquePartnerInput = ClaimExtractor.extractIssuerState(partnerClaims);
        if(StringUtils.isNotEmpty(opaquePartnerInput)) {
            // Partner provided some opaque state information so pass it back unchanged
            builder.claim(ClaimNames.EX_ISSUER_STATE.getClaimName(), opaquePartnerInput);
        }

        populateReturnCodeClaims(builder, ctx.getResultMeta());
    }
}
