DESCRIPTION = "The source recipe for StarlingX Config-Files repo"

inherit stx-source

STX_REPO = "config-files"

BRANCH = "r/stx.5.0"
SRCREV = "237737bbd2488bcae6822dfadc4977d86ea642d7"

SRC_URI += "\
	file://nfs-utils-config-remove-the-f-option-for-rpc.mountd.patch \
	"
