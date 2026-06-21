# Tools

本目录保存项目开发工具的实现文件。正式入口由 Gradle task 提供，本目录不提供独立执行入口。

## 目录

- `checks/`：JavaParser 项目静态检查器。
- `formatter/`：文本、换行、缩进等 Python 格式化脚本。

## 当前实现

- `formatter/utf8_no_bom.py` 将带 UTF-8 BOM 的文本文件转换为无 BOM UTF-8。
- `formatter/to_crlf.py` 将文本文件换行统一为 CRLF。
- `formatter/java_tabs.py` 将 Java 行首四空格缩进转换为 tab 缩进。
- `formatter/java_single_final_blank_line.py` 将 Java 文件末尾统一为一个 CRLF，不保留额外空行。
- `formatter/remove_pycache.py` 删除项目中的 `__pycache__` 目录。
- `checks/src/` 保存 JavaParser 静态检查框架，具体规则通过规则注册入口扩展。
- `checks/fixtures/` 预留项目静态规则 fixture 目录；后续规则可通过 `expected-rules.txt` 声明预期命中的规则。

## Gradle 任务

- `formatProject`：执行 formatter 并写入修复。
- `verifyFormat`：执行 formatter 检查模式，不写入文件。
- `projectRulesCheck`：使用 JavaParser 执行项目 Java 静态规则。
- `projectRulesFixtureCheck`：执行项目静态规则 fixture。
- `projectCheck`：格式化、编译并执行项目静态规则和规则 fixture。
- `verifyProject`：只检查格式、编译并执行项目静态规则和规则 fixture。
