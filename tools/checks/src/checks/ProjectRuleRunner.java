package checks;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class ProjectRuleRunner {
	private ProjectRuleRunner() {
	}

	public static List<Violation> run(Path sourceRoot, List<ProjectRule> rules) throws IOException {
		StaticJavaParser.setConfiguration(new ParserConfiguration()
				.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
				.setCharacterEncoding(StandardCharsets.UTF_8));

		List<Violation> violations = new ArrayList<>();
		for (Path file : listJavaFiles(sourceRoot)) {
			checkFile(file, rules, violations);
		}
		violations.sort(Comparator
				.comparing((Violation violation) -> violation.file().toString())
				.thenComparingInt(Violation::line)
				.thenComparing(Violation::rule));
		return violations;
	}

	public static void printViolations(List<Violation> violations) {
		for (Violation violation : violations) {
			System.err.println(violation);
		}
	}

	private static List<Path> listJavaFiles(Path sourceRoot) throws IOException {
		try (Stream<Path> paths = Files.walk(sourceRoot)) {
			return paths
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".java"))
					.sorted()
					.toList();
		}
	}

	private static void checkFile(Path file, List<ProjectRule> rules, List<Violation> violations) {
		CompilationUnit unit;
		try {
			unit = StaticJavaParser.parse(file);
		} catch (RuntimeException | IOException ex) {
			violations.add(new Violation(file, 1, "ParseError", ex.getMessage()));
			return;
		}

		SourceFile sourceFile = new SourceFile(file, unit);
		for (ProjectRule rule : rules) {
			violations.addAll(rule.check(sourceFile));
		}
	}
}
