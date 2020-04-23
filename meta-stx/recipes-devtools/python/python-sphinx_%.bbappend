#
## Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

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

