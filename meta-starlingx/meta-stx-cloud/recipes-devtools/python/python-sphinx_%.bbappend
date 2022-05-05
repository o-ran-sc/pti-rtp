
inherit python-dir
RDEPENDS_${PN}_append = " \
	${PYTHON_PN}-pygments \
	${PYTHON_PN}-typing \
	${PYTHON_PN}-sphinxcontrib-websupport \
	${PYTHON_PN}-alabaster \
	${PYTHON_PN}-imagesize \
	${PYTHON_PN}-snowballstemmer \
	${PYTHON_PN}-packaging \
	"

