#!/usr/bin/env bash

keytool -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown" -genkeypair -alias mytest -keyalg RSA -keysize 2048 -validity 10000 -keypass mypass -keystore mytest.jks -storepass mypass || echo "Exception occurred"
keytool -list -rfc --keystore mytest.jks -storepass mypass | openssl x509 -inform pem -pubkey
