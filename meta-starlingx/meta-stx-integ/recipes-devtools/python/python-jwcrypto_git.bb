
DESCRIPTION = "An implementation of the JOSE Working Group documents: RFC 7515 - JSON Web Signature (JWS) RFC 7516 - JSON Web Encryption (JWE) RFC 7517 - JSON Web Key (JWK) RFC 7518 - JSON Web Algorithms (JWA) RFC 7519 - JSON Web Token (JWT) RFC 7520 - Examples of Protecting Content Using JSON Object Signing and Encryption (JOSE)"

LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRCREV = "437ea86caef224cf769e30cafe30f1c0b4e0f3e2"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.6.0+git${SRCPV}"

SRC_URI = "git://github.com/latchset/jwcrypto.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools distutils
