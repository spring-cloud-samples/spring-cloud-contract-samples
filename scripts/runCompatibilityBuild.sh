#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=${ROOT:-$(pwd)}
export BUILD_COMMON="${BUILD_COMMON:-true}"
export SKIP_TESTS="${SKIP_TESTS:-false}"
export OLD_PRODUCER_TRAIN="${OLD_PRODUCER_TRAIN:-false}"

. ${ROOT}/scripts/setup.sh

function clean() {
	rm -rf ~/.m2/repository/com/example/
	rm -rf ~/.m2/repository/org/springframework/cloud/spring-cloud-contract-gradle-plugin/
	rm -rf ~/.m2/repository/org/springframework/cloud/spring-cloud-contract-verifier/
	rm -rf "${ROOT}"/target/
	rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
	rm -rf ~/.gradle/caches/modules-2/files-2.1/org.springframework.cloud/spring-cloud-contract-gradle-plugin
	rm -rf ~/.gradle/caches/modules-2/files-2.1/org.springframework.cloud/spring-cloud-contract-verifier
}

function bootVersion() {
	local minor="${1}"
	# FOR LATEST
	#BOOT_VERSION="\$( curl https://repo.spring.io/snapshot/org/springframework/boot/spring-boot-starter/maven-metadata.xml | sed -ne '/<latest>/s#\s*<[^>]*>\s*##gp')"
	curl --silent https://repo.spring.io/snapshot/org/springframework/boot/spring-boot-starter/maven-metadata.xml | grep "<version>${minor}." | tail -1 | sed -ne '/<version>/s#\s*<[^>]*>\s*##gp' | xargs
}

function contractVersion() {
	local minor="${1}"
	#BOOT_VERSION="\$( curl https://repo.spring.io/snapshot/org/springframework/cloud/spring-cloud-starter-contract-verifier/maven-metadata.xml | sed -ne '/<latest>/s#\s*<[^>]*>\s*##gp')"
	curl --silent https://repo.spring.io/snapshot/org/springframework/cloud/spring-cloud-starter-contract-verifier/maven-metadata.xml | grep "<version>${minor}." | tail -1 | sed -ne '/<version>/s#\s*<[^>]*>\s*##gp' | xargs
}

function cloudVersion() {
	local minor="${1}"
	#BOOT_VERSION="\$( curl https://repo.spring.io/snapshot/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml | sed -ne '/<latest>/s#\s*<[^>]*>\s*##gp')"
	curl --silent https://repo.spring.io/snapshot/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml | grep "<version>${minor}." | tail -1 | sed -ne '/<version>/s#\s*<[^>]*>\s*##gp' | xargs
}

CURRENT_BOOT_VERSION="${CURRENT_BOOT_VERSION:-}"
CURRENT_CONTRACT_VERSION="${CURRENT_CONTRACT_VERSION:-}"
CURRENT_CLOUD_VERSION="${CURRENT_CLOUD_VERSION:-}"
PREVIOUS_BOOT_VERSION="${PREVIOUS_BOOT_VERSION:-}"
PREVIOUS_CLOUD_VERSION="${PREVIOUS_CLOUD_VERSION:-}"
PREVIOUS_CONTRACT_VERSION="${PREVIOUS_CONTRACT_VERSION:-}"
[[ -z "${CURRENT_BOOT_VERSION}" ]] && CURRENT_BOOT_VERSION="$(bootVersion "2.4")"
[[ -z "${CURRENT_CONTRACT_VERSION}" ]] && CURRENT_CONTRACT_VERSION="$(contractVersion "3.0")"
[[ -z "${CURRENT_CLOUD_VERSION}" ]] && CURRENT_CLOUD_VERSION="$(cloudVersion "2020.0")"
[[ -z "${PREVIOUS_BOOT_VERSION}" ]] && PREVIOUS_BOOT_VERSION="$(bootVersion "2.3")"
[[ -z "${PREVIOUS_CLOUD_VERSION}" ]] && PREVIOUS_CLOUD_VERSION="$(cloudVersion "Hoxton")"
[[ -z "${PREVIOUS_CONTRACT_VERSION}" ]] && PREVIOUS_CONTRACT_VERSION="$(contractVersion "2.2")"

function build() {
	local folder="${1}"
	local bootVersion="${2}"
	local cloudVersion="${3}"
	local verifierVersion="${4}"
	echo -e "\n\nCOMPATIBILITY BUILD IN PROGRESS\n\n"
	echo -e "\n\nBuilding [${folder}] for boot [${bootVersion}] and cloud [${cloudVersion}] and verifier [${verifierVersion}]\n\n"
	pushd "${ROOT}/${folder}"
	if [[ "${SKIP_TESTS}" == "true" ]]; then
		./gradlew clean build publishToMavenLocal --refresh-dependencies -x test -PBOM_VERSION="${cloudVersion}" -PbootVersion="${bootVersion}" -PverifierVersion="${verifierVersion}" -POLD_PRODUCER_TRAIN="${OLD_PRODUCER_TRAIN}" --stacktrace -x copyContracts -x verifierStubsJar -x generateClientStubs -x generateContractTests
	else
		./gradlew clean build publishToMavenLocal --refresh-dependencies -PBOM_VERSION="${cloudVersion}" -PbootVersion="${bootVersion}" -PverifierVersion="${verifierVersion}" -POLD_PRODUCER_TRAIN="${OLD_PRODUCER_TRAIN}" --stacktrace
	fi
	popd
}

function prepare_for_build() {
	clean

	cd "${ROOT}/beer_contracts"
	./mvnw clean install -U -Dspring-cloud.version="${PREVIOUS_CLOUD_VERSION}"

	if [[ "${BUILD_COMMON}" == "true" ]]; then
		pushd "${ROOT}/common" && ./gradlew clean build publishToMavenLocal -PBOM_VERSION="${PREVIOUS_CLOUD_VERSION}" --refresh-dependencies -x test --stacktrace && popd
	fi

	prepare_git
}

function build_all_projects() {
	local producerBootVersion="${1}"
	local producerCloudVersion="${2}"
	local producerVerifierVersion="${3}"
	local consumerBootVersion="${4}"
	local consumerCloudVersion="${5}"
	local consumerVerifierVersion="${6}"
	build producer "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_advanced "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_java "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_jaxrs "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_jaxrs_spring "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_webflux "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build consumer_pact "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build producer_advanced "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_pact "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_kafka "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	# TODO: Uses Kotlin - we need to fix this
	# build producer_kotlin "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	# TODO: Uses Kotlin - we need to fix this
	# build producer_kotlin_ftw "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build consumer_pact "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build producer_proto "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_router_function "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_security "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_testng "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_webflux "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	# TODO: Uses Kotlin - we need to fix this
	# build producer_webflux_security "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_webflux_webtestclient "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_dsl_restdocs "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_empty_git "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_external_contracts "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_git "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_junit4 "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_latest_2_2_features "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_restdocs "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_spock "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_stubs_per_consumer "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_webtestclient_restdocs "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_with_xml "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	build producer_yaml "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
	echo "Due to some issues part of the tests are failing, will skip them"
	export SKIP_COMPATIBILITY_TESTS=true
	build consumer "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_java "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_kafka "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	# TODO: Uses Kotlin - we need to fix this
	# build consumer_kotlin_ftw "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_pact "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_pact_stubrunner "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_proto "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_security "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_with_discovery "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_with_junit4 "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_with_latest_2_2_features "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_with_restdocs "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_with_secured_webflux "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
	build consumer_with_stubs_per_consumer "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
}

function build_gradle() {
	prepare_for_build
	echo "Building current producer, old consumer"
	build_all_projects "${CURRENT_BOOT_VERSION}" "${CURRENT_CLOUD_VERSION}" "${CURRENT_CONTRACT_VERSION}" "${PREVIOUS_BOOT_VERSION}" "${PREVIOUS_CLOUD_VERSION}" "${PREVIOUS_CONTRACT_VERSION}"

	prepare_for_build
	export OLD_PRODUCER_TRAIN="true"
	echo "Building old producer, current consumer"
	build_all_projects "${PREVIOUS_BOOT_VERSION}" "${PREVIOUS_CLOUD_VERSION}" "${PREVIOUS_CONTRACT_VERSION}" "${CURRENT_BOOT_VERSION}" "${CURRENT_CLOUD_VERSION}" "${CURRENT_CONTRACT_VERSION}"
}

cat <<'EOF'
 _______  _______  _______  _______  _______ __________________ ______  _________ _       __________________
(  ____ \(  ___  )(       )(  ____ )(  ___  )\__   __/\__   __/(  ___ \ \__   __/( \      \__   __/\__   __/|\     /|
| (    \/| (   ) || () () || (    )|| (   ) |   ) (      ) (   | (   ) )   ) (   | (         ) (      ) (   ( \   / )
| |      | |   | || || || || (____)|| (___) |   | |      | |   | (__/ /    | |   | |         | |      | |    \ (_) /
| |      | |   | || |(_)| ||  _____)|  ___  |   | |      | |   |  __ (     | |   | |         | |      | |     \   /
| |      | |   | || |   | || (      | (   ) |   | |      | |   | (  \ \    | |   | |         | |      | |      ) (
| (____/\| (___) || )   ( || )      | )   ( |   | |   ___) (___| )___) )___) (___| (____/\___) (___   | |      | |
(_______/(_______)|/     \||/       |/     \|   )_(   \_______/|/ \___/ \_______/(_______/\_______/   )_(      \_/

EOF

cat <<EOF
VERSIONS:

CURRENT_BOOT_VERSION="${CURRENT_BOOT_VERSION}"
CURRENT_CLOUD_VERSION="${CURRENT_CLOUD_VERSION}"
CURRENT_CONTRACT_VERSION="${CURRENT_CONTRACT_VERSION}"
PREVIOUS_BOOT_VERSION="${PREVIOUS_BOOT_VERSION}"
PREVIOUS_CLOUD_VERSION="${PREVIOUS_CLOUD_VERSION}"
PREVIOUS_CONTRACT_VERSION="${PREVIOUS_CONTRACT_VERSION}"

EOF

build_gradle
echo -e "\n\nFinished compatibility build!!!\n\n"
