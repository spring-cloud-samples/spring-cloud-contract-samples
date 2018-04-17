package docs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Generates a JSON file that will be used to sketch
 * the dependencies between projects
 *
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
public class GenerateGraphFromContractsTests {
	private final ObjectMapper mapper = new ObjectMapper();

	@Value("classpath:contracts") Resource contracts;

	@Test
	public void should_build_a_list_of_relationships() throws IOException {
		Path root = Paths.get(contracts.getURI());
		DependencyWalker dependencyWalker = new DependencyWalker(root);

		Files.walkFileTree(root, dependencyWalker);

		File outputFile = new File("relationships.js");
		String relationships = mapper.writeValueAsString(dependencyWalker.relationships());
		outputFile.createNewFile();
		Files.write(outputFile.toPath(), ("relationships = '" + relationships + "'").getBytes());
	}
}

class Relationship {
	public String source;
	public String target;

	public Relationship(String source, String target) {
		this.source = source;
		this.target = target;
	}
}

class DependencyWalker extends SimpleFileVisitor<Path> {

	private static final Logger log = LoggerFactory.getLogger(DependencyWalker.class);
	private static final Pattern SEMVER = Pattern.compile("^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$");
	private static final List<String> SKIPPED_FOLDERS = Arrays.asList("mvn", "target", "build", ".mvn", ".gradle", ".idea", "eclipse");

	private final List<String> foundProducers = new ArrayList<>();
	private final List<String> foundConsumers = new ArrayList<>();
	private final List<Relationship> relationships = new ArrayList<>();
	private final Path root;

	DependencyWalker(Path root) {
		this.root = root;
	}

	List<String> foundProducers() {
		return this.foundProducers;
	}

	List<String> foundConsumers() {
		// TODO: print which consumers are not matched
		return this.foundConsumers;
	}

	List<Relationship> relationships() {
		// parent has always group and artifact but a child not necessarily
		// we need to assume that if a child has just artifact id then we'll match it against
		// the first parent
		// TODO: Add an option to explicitly match it (or just document to use groupid.artifactid.version_with_underscores)
		this.relationships.forEach(relationship -> {
			this.foundProducers.stream()
					.filter(s -> relationship.target.equals(artifactId(s)))
					.findFirst()
					.ifPresent(s -> relationship.target = s);
		});
		return this.relationships;
	}

	private String artifactId(String name) {
		if (name.contains(".")) {
			String[] split = name.split(":");
			// [foo],[bar],[bazService],[1.2.3]
			String potentialVersion = split[split.length - 1];
			if (isSemver(potentialVersion)) {
				// bazService
				return split[split.length - 2];
			}
			// [foo],[bar],[bazService]
			return potentialVersion;
		}
		// bazService
		return name;
	}

	@Override public FileVisitResult preVisitDirectory(Path dir,
			BasicFileAttributes attrs) throws IOException {
		File producer = dir.toFile();
		boolean pomExists = new File(producer, "pom.xml").exists();
		boolean gradleExists = new File(producer, "build.gradle").exists();
		// in this case we don't know explicitly who the consumers are
		boolean producerContracts = new File(producer, ".producer_contracts").exists();
		if (pomExists || gradleExists) {
			// com/example/producer/1.0.0 -> com/example
			// com/example/producer -> com/example
			boolean isSemVerDir = isSemver(dir.getFileName().toString());
			Path artifactId = isSemVerDir ? dir.getParent() : dir;
			Path artifactIdParent = artifactId.getParent();
			String version = isSemVerDir ? dir.getFileName().toString() : "";
			Path relativePath = this.root.relativize(artifactIdParent);
			// com/example -> com.example
			String relativeGroupId = relativePath.toString().replace(File.separator, ".");
			String producerName = artifactId.getFileName().toString();
			// com.example:producer
			// com.example:producer:1.0.0
			String gav = relativeGroupId + ":" + producerName + (isSemVerDir ? ":" + version : "");
			File[] consumers = new File[0];
			List<String> consumerForProducer = new ArrayList<>();
			if (!producerContracts) {
				consumers = producer.listFiles(File::isDirectory);
				if (consumers != null && consumers.length > 0) {
					Arrays.stream(consumers)
							.filter(file -> !SKIPPED_FOLDERS.contains(file.getName()))
							.forEach(file -> {
								String consumerName = folderToGav(file);
								consumerForProducer.add(consumerName);
								this.foundConsumers.add(consumerName);
								this.relationships.add(new Relationship(gav, consumerName));
							});
				}
			}
			log.info("Found [" + gav + "] producer with consumers " + consumerForProducer);
			this.foundProducers.add(gav);
			return FileVisitResult.SKIP_SUBTREE;
		}
		return super.preVisitDirectory(dir, attrs);
	}

	private boolean isSemver(String name) {
		//TODO: Consider adding some file like .version for non semver
		return SEMVER.matcher(name).matches();
	}

	private String folderToGav(File file) {
		String fileName = file.getName();
		// (1) bazService
		// (2) foo.bar.bazService
		// (3) foo.bar.bazService.1_2_3
		if (fileName.contains(".")) {
			String[] split = fileName.split("\\.");
			// (2) [foo],[bar],[bazService]
			// (3) [foo],[bar],[bazService],[1.2.3]
			String potentialVersion = split[split.length - 1].replace("_", ".");
			if (isSemver(potentialVersion)) {
				// 1.2.3
				String version = potentialVersion;
				// bazService
				String artifactId = split[split.length - 2];
				StringBuilder group = new StringBuilder();
				for (int i = 0; i < split.length - 2; i++) {
					group.append(split[i]);
					if (i + 1 < split.length - 2) {
						group.append(".");
					}
				}
				// foo.bar
				String groupId = group.toString();
				// (3) foo.bar:bazService:1.2.3
				return groupId + ":" + artifactId + ":" + version;
			}
			// (2) foo.bar.bazService -> foo.bar:bazService
			return replaceLast(fileName, ".", ":");
		}
		// (3) bazService
		return fileName;
	}

	private String replaceLast(String string, String substring, String replacement) {
		int index = string.lastIndexOf(substring);
		if (index == -1) {
			return string;
		}
		return string.substring(0, index) + replacement + string
				.substring(index + substring.length());
	}

}