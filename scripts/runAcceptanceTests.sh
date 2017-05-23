#!/bin/bash

set -o errexit

function build_maven() {
    echo -e "\n\nInstalling common\n\n"
    cd ${ROOT}/common
    ./mvnw clean install -U
    cd ${ROOT}

    echo -e "\n\nBuilding everything\n\n"
    ./mvnw clean install -Ptest -U
}

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function build() {
    local folder="${1}"
    echo -e "\n\nBuilding ${folder}\n\n"
    cd "${ROOT}/${folder}"
    ./gradlew clean build publishToMavenLocal
    cd "${ROOT}"
}

function build_gradle() {
    clean

    echo -e "\n\nBuilding the external contracts jar\n\n"
    cd "${ROOT}/beer_contracts"
    ./mvnw clean install -U

    build common
    build producer
    build producer_advanced
    build producer_with_stubs_per_consumer
    build producer_with_external_contracts
    build producer_with_restdocs
    build consumer
    build consumer_with_stubs_per_consumer
    build consumer_with_restdocs
    build consumer_with_discovery
    return 0
}


clean
ROOT=`pwd`
RETRIES=3

cat <<'EOF'
 .----------------.  .----------------.  .----------------.  .----------------.  .-----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| | ____    ____ | || |      __      | || | ____   ____  | || |  _________   | || | ____  _____  | |
| ||_   \  /   _|| || |     /  \     | || ||_  _| |_  _| | || | |_   ___  |  | || ||_   \|_   _| | |
| |  |   \/   |  | || |    / /\ \    | || |  \ \   / /   | || |   | |_  \_|  | || |  |   \ | |   | |
| |  | |\  /| |  | || |   / ____ \   | || |   \ \ / /    | || |   |  _|  _   | || |  | |\ \| |   | |
| | _| |_\/_| |_ | || | _/ /    \ \_ | || |    \ ' /     | || |  _| |___/ |  | || | _| |_\   |_  | |
| ||_____||_____|| || ||____|  |____|| || |     \_/      | || | |_________|  | || ||_____|\____| | |
| |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
EOF

SUCCESS=false

for i in $( seq 1 "${RETRIES}" ); do
    echo "Attempt #$i/${RETRIES}..."
    if build_maven; then
    echo "Tests succeeded!"
        SUCCESS="true"
        break;
    else
        echo "Fail #$i/${RETRIES}... will try again"
    fi
done

if [ ${SUCCESS} == "false" ]; then
    echo "Failed to make the build work"
    exit 1
fi

cat <<'EOF'
 .----------------.  .----------------.  .-----------------. .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| | ____    ____ | || |      __      | || | ____  _____  | || | _____  _____ | || |      __      | || |   _____      | |
| ||_   \  /   _|| || |     /  \     | || ||_   \|_   _| | || ||_   _||_   _|| || |     /  \     | || |  |_   _|     | |
| |  |   \/   |  | || |    / /\ \    | || |  |   \ | |   | || |  | |    | |  | || |    / /\ \    | || |    | |       | |
| |  | |\  /| |  | || |   / ____ \   | || |  | |\ \| |   | || |  | '    ' |  | || |   / ____ \   | || |    | |   _   | |
| | _| |_\/_| |_ | || | _/ /    \ \_ | || | _| |_\   |_  | || |   \ `--' /   | || | _/ /    \ \_ | || |   _| |__/ |  | |
| ||_____||_____|| || ||____|  |____|| || ||_____|\____| | || |    `.__.'    | || ||____|  |____|| || |  |________|  | |
| |              | || |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
EOF

echo -e "\n\nBuilding beer_contracts\n\n"
cd "${ROOT}/beer_contracts"

echo -e "\n\nBuilding only the subset of contracts\n\n"
cd "${ROOT}/beer_contracts/src/main/resources/contracts/com/example/beer-api-producer-external/1.0.0"
cp "${ROOT}/mvnw" .
cp -r "${ROOT}/.mvn" .
./mvnw clean install -DskipTests -U

clean

cat <<'EOF'
 .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| |    ______    | || |  _______     | || |      __      | || |  ________    | || |   _____      | || |  _________   | |
| |  .' ___  |   | || | |_   __ \    | || |     /  \     | || | |_   ___ `.  | || |  |_   _|     | || | |_   ___  |  | |
| | / .'   \_|   | || |   | |__) |   | || |    / /\ \    | || |   | |   `. \ | || |    | |       | || |   | |_  \_|  | |
| | | |    ____  | || |   |  __ /    | || |   / ____ \   | || |   | |    | | | || |    | |   _   | || |   |  _|  _   | |
| | \ `.___]  _| | || |  _| |  \ \_  | || | _/ /    \ \_ | || |  _| |___.' / | || |   _| |__/ |  | || |  _| |___/ |  | |
| |  `._____.'   | || | |____| |___| | || ||____|  |____|| || | |________.'  | || |  |________|  | || | |_________|  | |
| |              | || |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
EOF

SUCCESS=false

for i in $( seq 1 "${RETRIES}" ); do
    echo "Attempt #$i/${RETRIES}..."
    if build_gradle; then
    echo "Tests succeeded!"
        SUCCESS=true
        break
    else
        echo "Fail #$i/${RETRIES}... will try again"
    fi
done

if [ ${SUCCESS} == "false" ]; then
    echo "Failed to make the build work"
    exit 1
fi

cat <<'EOF'
 .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. |
| |  ________    | || |     ____     | || |     ______   | || |    _______   | |
| | |_   ___ `.  | || |   .'    `.   | || |   .' ___  |  | || |   /  ___  |  | |
| |   | |   `. \ | || |  /  .--.  \  | || |  / .'   \_|  | || |  |  (__ \_|  | |
| |   | |    | | | || |  | |    | |  | || |  | |         | || |   '.___`-.   | |
| |  _| |___.' / | || |  \  `--'  /  | || |  \ `.___.'\  | || |  |`\____) |  | |
| | |________.'  | || |   `.____.'   | || |   `._____.'  | || |  |_______.'  | |
| |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'
EOF

echo "Generating docs"
./gradlew generateDocumentation

echo "Running Stub Runner Boot test"
./scripts/stub_runner_boot.sh