#!/usr/bin/env bash

case "`uname`" in
  Darwin* )
    uname="osx"
    ;;
  * )
    uname="linux"
    ;;
esac

export ROOT="$( pwd )"
export TEST_ROOT="${ROOT}/src/test"
export SWAGYMNIA_BIN="${TEST_ROOT}/bash/${uname}/swaggymnia"

PATH="${PATH}:${ROOT}/node/"
export PATH

echo "Generate postman from restdocs"
node_modules/restdocs-to-postman/bin/cli -i target/generated-snippets -e insomnia -f secondLastFolder -r "${TEST_ROOT}"/swagger/replacements.json -o target/insomnia-collection.json

echo "Generate swagger from postman"
pushd target
"${SWAGYMNIA_BIN}" generate -insomnia insomnia-collection.json -config "${TEST_ROOT}"/swagger/config.json -output json
popd

echo "Swagger spec is available at $( pwd )/target/swagger.json"
