find . -name "*.java" | xargs sed -i "1s/package/\/\/ SPDX-License-Identifier: MIT\n\npackage/g"
