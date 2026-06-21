package checks;

import checks.rules.ProjectRules;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectRulesFixtureCheck {
	private static final String EXPECTED_RULES_FILE = "expected-rules.txt";

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Usage: ProjectRulesFixtureCheck <fixtures-root>");
		}

		Path fixturesRoot = Path.of(args[0]);
		List<String> failures = runFixtures(fixturesRoot);
		for (String failure : failures) {
			System.err.println(failure);
		}
		if (!failures.isEmpty()) {
			throw new IllegalStateException(failures.size() + " project rule fixture failure(s) found.");
		}
	}

	private static List<String> runFixtures(Path fixturesRoot) throws IOException {
		if (!Files.exists(fixturesRoot)) {
			return List.of();
		}

		try (Stream<Path> paths = Files.walk(fixturesRoot, 2)) {
			return paths
					.filter(Files::isDirectory)
					.filter(path -> Files.exists(path.resolve(EXPECTED_RULES_FILE)))
					.sorted()
					.flatMap(path -> runFixture(path).stream())
					.toList();
		}
	}

	private static List<String> runFixture(Path fixture) {
		try {
			Set<String> expectedRules = readExpectedRules(fixture.resolve(EXPECTED_RULES_FILE));
			List<Violation> violations = ProjectRuleRunner.run(fixture, ProjectRules.all());
			Set<String> actualRules = violations.stream()
					.map(Violation::rule)
					.collect(Collectors.toCollection(HashSet::new));

			if (actualRules.equals(expectedRules)) {
				return List.of();
			}

			String message = fixture + ": expected rules " + expectedRules + " but got " + actualRules;
			return Stream.concat(Stream.of(message), violations.stream().map(Violation::toString)).toList();
		} catch (IOException ex) {
			return List.of(fixture + ": " + ex.getMessage());
		}
	}

	private static Set<String> readExpectedRules(Path file) throws IOException {
		return Files.readAllLines(file, StandardCharsets.UTF_8).stream()
				.map(String::trim)
				.filter(line -> !line.isEmpty())
				.filter(line -> !line.startsWith("#"))
				.collect(Collectors.toCollection(HashSet::new));
	}
}
