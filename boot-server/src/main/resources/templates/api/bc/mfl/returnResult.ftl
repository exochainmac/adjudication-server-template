<#-- @ftlvariable name="bcLoginContext" type="com.exochain.api.bc.fsm.BcLoginContext" -->
<#import "/spring.ftl" as spring/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Exochain Auth Result</title>
</head>
<body>
<h1>Exochain Auth Result</h1>
<br/><br/>
Debug Info<br/>
<form method="post" action="${bcLoginContext.apiAccountSettings.clientPostbackUrl}" target="_blank">
    <textarea name="initToken" style="width: 400px;">${bcLoginContext.outputToken}</textarea>
    <input type="submit" name="sendtoken" value="Send Response To Blue Cloud"/>
</form>
</body>
</html>