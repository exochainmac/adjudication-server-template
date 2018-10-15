<#import "/spring.ftl" as spring/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Exochain One-Time Passcode Verification</title>
</head>
<body>
<h1>Please enter your One-Time Passcode:</h1>
<br/><br/>

<form action="/api/bc/mfl/verifyOtp" method="POST">

    <input type="text" name="otp">
    <input type="hidden" name="initToken" value="${initToken}">

    <input type="submit" value="Verify">
</form>

</body>
</html>