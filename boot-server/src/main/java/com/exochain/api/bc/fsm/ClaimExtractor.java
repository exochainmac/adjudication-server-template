package com.exochain.api.bc.fsm;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.fsm.exception.PassthroughException;
import com.exochain.api.bc.fsm.exception.PhoneNumberFormatException;
import com.exochain.jwt.claims.ClaimNames;
import com.exochain.result.meta.DebugLoggedResultMeta;
import com.exochain.result.meta.InfoLoggedResultMeta;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public class ClaimExtractor {
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance(false, false);
    public static final String CLAIM_ERROR_FORMAT = "Claim: [{}] Error: [{}]";
    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();
    public static final String DEFAULT_PHONE_COUNTRY = "US";


    public static String extractSingleSubjectEmail(JWTClaimsSet claimsSet) throws PassthroughException {
        String claimName = ClaimNames.EX_SUBJECT_EMAILS.getClaimName();
        String email = null;
        try {
            List<String> candidateEmails = claimsSet.getStringListClaim(claimName);

            requireOneInCollection(candidateEmails, claimName, CLAIM_ERROR_FORMAT);
            email = candidateEmails.get(0);
            if(!EMAIL_VALIDATOR.isValid(email)) {
                throw new PassthroughException(new InfoLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, CLAIM_ERROR_FORMAT, claimName, "Invalid email address format"));
            }
        } catch (ParseException e) {
            throw new PassthroughException(new InfoLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, CLAIM_ERROR_FORMAT, claimName, e.getMessage()));
        }
        return email;
    }

    private static void requireOneInCollection(Collection collection, String claimName, String errorFormat) throws PassthroughException {
        if(CollectionUtils.isEmpty(collection)) {
            throw new PassthroughException(new InfoLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, errorFormat, claimName, "Collection is empty"));
        }
        if (collection.size() != 1) {
            throw new PassthroughException(new InfoLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, errorFormat, claimName, "Only one collection item allowed"));
        }
    }

    public static String extractSingleSubjectCell(JWTClaimsSet claimsSet) throws PassthroughException, PhoneNumberFormatException {
        String claimName = ClaimNames.EX_SUBJECT_CELLS.getClaimName();
        String cell = null;

        try {
            List<String> candidateCells = claimsSet.getStringListClaim(claimName);
            requireOneInCollection(candidateCells, claimName, CLAIM_ERROR_FORMAT);
            Phonenumber.PhoneNumber phoneNumber = PHONE_UTIL.parseAndKeepRawInput(candidateCells.get(0), DEFAULT_PHONE_COUNTRY);
            if(!PHONE_UTIL.isPossibleNumber(phoneNumber)) {
                throw new PhoneNumberFormatException(new DebugLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, CLAIM_ERROR_FORMAT, claimName, PHONE_UTIL.isPossibleNumberWithReason(phoneNumber).toString()));
            }
            if(!PHONE_UTIL.isValidNumber(phoneNumber)) {
                throw new PhoneNumberFormatException(new DebugLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, CLAIM_ERROR_FORMAT, claimName, "Invalid phone number format"));
            }
            cell = PHONE_UTIL.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (ParseException e) {
            throw new PassthroughException(new InfoLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, CLAIM_ERROR_FORMAT, claimName, e.getMessage()));
        } catch (NumberParseException e) {
            throw new PhoneNumberFormatException(new DebugLoggedResultMeta(RMReg.CLAIM_CONTENT_ERROR, CLAIM_ERROR_FORMAT, claimName, e.getMessage()));
        }

        return cell;
    }

    public static String extractIssuerState(JWTClaimsSet claimsSet) {
        String issuerState = null;

        try {
            issuerState = claimsSet.getStringClaim(ClaimNames.EX_ISSUER_STATE.getClaimName());
        } catch (ParseException e) {
            // Intentional NOOP here to swallow instruction because this claim is optional
        }
        return issuerState;
    }

}
