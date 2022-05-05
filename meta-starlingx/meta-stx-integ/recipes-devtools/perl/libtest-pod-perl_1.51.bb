
DESCRIPTION = "Test::Pod - check for POD errors in files"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://README;beginline=26;md5=ea81140c3f3b2937c6490fc732471cd0"
HOMEPAGE = "https://github.com/perl-pod/test-pod/"
BBCLASSEXTEND = "native"
RDEPENDS_${PN} += " perl-module-test-more \
                    perl-module-file-spec \
                    perl-module-pod-simple \
                    perl-module-test-builder-tester \
                    "

SRC_URI ="http://search.cpan.org/CPAN/authors/id/E/ET/ETHER/Test-Pod-${PV}.tar.gz;name=test-pod-perl-${PV}"
SRC_URI[test-pod-perl-1.51.md5sum] = "f806aa84de2f0c0fba48b3a5a8a4aecf"
SRC_URI[test-pod-perl-1.51.sha256sum] = "c1a1d3cedf4a579e3aad89c36f9878a8542b6656dbe71f1581420f49582d7efb"
S = "${WORKDIR}/Test-Pod-${PV}"

inherit cpan

