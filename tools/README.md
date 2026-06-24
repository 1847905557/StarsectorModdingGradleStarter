# Tools

本目录保存项目开发工具的实现文件。正式入口由 Gradle task 提供，本目录不提供独立执行入口。

## 目录

- `checks/`：项目 Java 静态规则检查器、规则实现和规则 fixture。
- `formatter/`：文本、换行、缩进等 Python 格式化脚本。

## checks 实现

### 目录职责

- `project/`：项目源码规则运行域，包含 Gradle 调用入口、规则 runner、规则输入和规则结果。
- `project/rules/`：项目源码规则实现；规则实现 `ProjectRule`，由 `ProjectRules` 注册并由项目源码规则入口执行。
- `project/fixture/`：项目源码规则 fixture 运行域，包含 fixture 用例模型、运行结果和 fixture runner。
- `project/model/`：项目源码规则共享的数据模型。
- `project/analysis/`：项目源码规则共享的源码语义分析和索引。
- `project/support/`：项目源码规则共享的无状态读取、解析、输出和 AST 辅助函数。
- `integrity/`：检查器自身完整性运行域，包含 Gradle 调用入口、规则 runner 和规则输入模型。
- `integrity/rules/`：检查器自身完整性规则实现和注册表；规则实现 `IntegrityRule`，由 `IntegrityRules` 注册并由检查器自检入口执行。
- `integrity/model/`：检查器自身完整性规则共享的数据模型。
- `integrity/support/`：检查器自身完整性规则共享的无状态读取和输出辅助函数。
- `fixtures/`：项目静态规则的正反例；每个 fixture 通过 `expected-rules.txt` 声明预期命中的规则，ok fixture 必须用 `# covers:` 声明覆盖的规则。

### 项目源码规则

此处根据规则文件逐行列举项目规则和功能。

需要接入项目规则时，在 `project/rules/` 中新增实现 `ProjectRule` 的规则类，并在 `ProjectRules.all()` 中返回该规则实例。规则运行入口、源码解析、类型求解、违规输出和 fixture 执行链路保持可用。

### 自检规则

此处根据规则文件逐行列举自检规则和功能。

需要接入检查器自检时，在 `integrity/rules/` 中新增实现 `IntegrityRule` 的规则类，并在 `IntegrityRules.all()` 中返回该规则实例。自检入口、输入模型和失败输出链路保持可用。

## formatter 实现

- `utf8_no_bom.py` 将带 UTF-8 BOM 的文本文件转换为无 BOM UTF-8。
- `to_crlf.py` 将文本文件换行统一为 CRLF。
- `java_tabs.py` 将 Java 行首四空格缩进转换为 tab 缩进。
- `java_single_final_blank_line.py` 将 Java 文件末尾统一为一个 CRLF，不保留额外空行。
- `remove_pycache.py` 删除项目中的 `__pycache__` 目录。

## Gradle 任务

- `formatProject`：执行 formatter 并写入修复。
- `verifyFormat`：执行 formatter 检查模式，不写入文件。
- `projectRulesCheck`：执行项目 Java 静态规则。
- `projectRulesFixtureCheck`：执行项目 Java 静态规则 fixture。
- `projectRulesIntegrityCheck`：聚合执行检查器自身完整性检查。
- `fixProject`：格式化、编译并执行项目静态规则。
- `verifyProject`：只检查格式、编译并执行项目静态规则。
