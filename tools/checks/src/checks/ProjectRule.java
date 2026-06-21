package checks;

import java.util.List;

public interface ProjectRule {
	List<Violation> check(SourceFile sourceFile);
}
