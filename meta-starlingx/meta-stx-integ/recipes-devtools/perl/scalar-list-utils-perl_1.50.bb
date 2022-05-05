
SUMMARY = "A selection of general-utility list subroutines"
DESCRIPTION = "\
  List::Util contains a selection of subroutines that people have expressed \
  would be nice to have in the perl core, but the usage would not really be \
  high enough to warrant the use of a keyword, and the size so small such \
  that being individual extensions would be wasteful.\
"
HOMEPAGE= "https://metacpan.org/release/Scalar-List-Utils"

SECTION = "libs"

LICENSE = "Artistic-1.0 | GPL-1.0+"

LIC_FILES_CHKSUM = "file://META.yml;beginline=11;endline=11;md5=963ce28228347875ace682de56eef8e8"

CPAN_PACKAGE = "Scalar-List-Utils"

SRC_URI = "${CPAN_MIRROR}/authors/id/P/PE/PEVANS/${CPAN_PACKAGE}-${PV}.tar.gz"

SRC_URI[md5sum] = "136313884d1afa2fb6840695a1034b2c"
SRC_URI[sha256sum] = "06aab9c693380190e53be09be7daed20c5d6278f71956989c24cca7782013675"

S = "${WORKDIR}/${CPAN_PACKAGE}-${PV}"

inherit cpan

BBCLASSEXTEND = "native nativesdk"
