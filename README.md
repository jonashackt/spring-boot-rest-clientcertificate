REST Client uses clientcertificate to authenticate to Spring Boot Server
=============================

If you only frequently use some sec-technologies like me, then you maybe need a refresher to what was what in this world :) For all the file formats like .crt, .cert, .pem, .key, .pkcs12/.pfx/.p12 read this post: https://serverfault.com/a/9717

And here´s an explanation of the difference between the 2 Java Keystore-Options (Keystore.jks and Truststore.jks): https://stackoverflow.com/a/6341566/4964553


# Generate the usual .key and .crt - and import them into needed Keystore .jks files

For the app here, you need the following files, if you want to fully want to go through all the steps (you need `openssl`and a `jdk` installed):

> Please make sure to alwasy use the same password for all artifacts! This is needed later, because Tomcat need the same password for the key and the keystores (see https://stackoverflow.com/a/23979014/4964553).


#### 1. generate Private Key: exampleprivate.key

```
openssl genrsa -des3 -out exampleprivate.key 1024
```

- enter a passphrase for the key, in this example I used `allpassword`


#### 2. generate Certificate Signing Request (CSR): example.csr

```
openssl req -new -key exampleprivate.key -out example.csr
```

This will bring up some questions you should answer according to the X.509 standard. You can nearly answer anything as you want to, but be sure to mind the `Common Name`. Because a certificate is always issued for a certain domain and in this example our Spring Boot server uses `localhost` here, we have to issue this accordingly. Otherwise you´ll get the following exception (see https://stackoverflow.com/questions/8839541/hostname-in-certificate-didnt-match also):

```
Caused by: javax.net.ssl.SSLPeerUnverifiedException: Certificate for <localhost> doesn't match any of the subject alternative names: []
	at org.apache.http.conn.ssl.SSLConnectionSocketFactory.verifyHostname(SSLConnectionSocketFactory.java:467)
	at org.apache.http.conn.ssl.SSLConnectionSocketFactory.createLayeredSocket(SSLConnectionSocketFactory.java:397)
	at org.apache.http.conn.ssl.SSLConnectionSocketFactory.connectSocket(SSLConnectionSocketFactory.java:355)
	at org.apache.http.impl.conn.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:142)
	at org.apache.http.impl.conn.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:359)
	at org.apache.http.impl.execchain.MainClientExec.establishRoute(MainClientExec.java:381)
	at org.apache.http.impl.execchain.MainClientExec.execute(MainClientExec.java:237)
	at org.apache.http.impl.execchain.ProtocolExec.execute(ProtocolExec.java:185)
	at org.apache.http.impl.execchain.RetryExec.execute(RetryExec.java:89)
	at org.apache.http.impl.execchain.RedirectExec.execute(RedirectExec.java:111)
	at org.apache.http.impl.client.InternalHttpClient.doExecute(InternalHttpClient.java:185)
	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:83)
	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:56)
	at org.springframework.http.client.HttpComponentsClientHttpRequest.executeInternal(HttpComponentsClientHttpRequest.java:89)
	at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:48)
	at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:53)
	at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:652)
```

There´s also a X.509 extension that allows to configure a certificate to support multiple domains (with the Subject Alternative Names (SAN) parameter) - but we won´t use this here. See https://www.digicert.com/subject-alternative-name.htm for more information.

```
Enter pass phrase for exampleprivate.key:
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:Bayern
Locality Name (eg, city) []:Munich
Organization Name (eg, company) [Internet Widgits Pty Ltd]:TheExampleInc
Organizational Unit Name (eg, section) []:SectionX
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:max.muller@example.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:
```


#### 3. generate self-signed Certificate: example.crt

```
openssl x509 -req -days 3650 -in example.csr -signkey exampleprivate.key -out example.crt
```


#### 4. create a Java Truststore Keystore, that inherits the generated self-signed Certificate: truststore.jks

```
keytool -import -file example.crt -alias exampleCA -keystore truststore.jks
```

You´re promted for a password again - be sure to use __the same password__ like the key´s one (I used `allpassword` here).


#### 5. create a Java Keystore, that inherits Public and Private Keys (keypair): keystore.jks

Since the JDK´s keytool can´t import a Private Key directly, we need to create a importable container format first - the `keystore.p12`: 

```
openssl pkcs12 -export -in example.crt -inkey exampleprivate.key -certfile example.crt -name "examplecert" -out keystore.p12
```

You´re promted for a password again - be sure to use __the same password__ like the key´s one (I used `allpassword` here).


```
keytool -importkeystore -srckeystore keystore.p12 -srcstoretype pkcs12 -destkeystore keystore.jks -deststoretype JKS
```

You´re promted for a password again - be sure to use __the same password__ like the key´s one (I used `allpassword` here). You´re also prompted for the exportpassword, which is `allpassword` again. Then finally, we have all files ready to implement our server.


# next steps

Copy the generated `keystore.jks` and `truststore.jks` into `src/main/resources` and - for showing a complete Testexample - also into `src/test/resources`


## Links

Create .key, .csr & .crt with openssl: https://www.akadia.com/services/ssh_test_certificate.html