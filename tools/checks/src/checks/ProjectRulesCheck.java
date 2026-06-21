package checks;

import checks.rules.ProjectRules;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProjectRulesCheck {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Usage: ProjectRulesCheck <source-root>");
		}

		List<Violation> violations = ProjectRuleRunner.run(Path.of(args[0]), ProjectRules.all());
		ProjectRuleRunner.printViolations(violations);
		if (!violations.isEmpty()) {
			throw new IllegalStateException(violations.size() + " project rule violation(s) found.");
		}
	}
}
