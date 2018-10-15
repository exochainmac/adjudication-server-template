<#import "/spring.ftl" as spring/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OperationError</title>
</head>
<body>
<h1>Error Information</h1>
<br/><br/>
FIX ME: This page is an information page for invalid requests.  These requests
never pass back to Blue Cloud because we have no way to know whether or not
they are legit so we just need some generic message that we respond with here.
<br/>
We could give a link or something to contact Blue Cloud.  We just need to work
out messaging because the user can't continue.
<br/>
Some of the reasons this page is triggered:
<ol>
    <li>Bot hit the page posting a value for the token that couldn't be parsed</li>
    <li>The input token could be parsed but was expired</li>
    <li>Input token failed cryptographic validation</li>

</ol>
</body>
</html>