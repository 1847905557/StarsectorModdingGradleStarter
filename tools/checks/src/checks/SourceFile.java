package checks;

import com.github.javaparser.ast.CompilationUnit;

import java.nio.file.Path;

public record SourceFile(Path path, CompilationUnit unit) {
}
