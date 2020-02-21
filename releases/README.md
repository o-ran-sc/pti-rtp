### How to tigger release job


1, Submit a patch with file pathname with pattern: releases/oran-shell-release*.yaml
while * can be any string.


2, Merge this patch.


3, the oran jenkins job 'pti-rpt-shell-release-master' will be triggered to build and publish
images

