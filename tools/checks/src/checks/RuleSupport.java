package checks;

import com.github.javaparser.ast.Node;

public final class RuleSupport {
	private RuleSupport() {
	}

	public static int lineOf(Node node) {
		return node.getBegin()
				.map(position -> position.line)
				.orElse(1);
	}
}
