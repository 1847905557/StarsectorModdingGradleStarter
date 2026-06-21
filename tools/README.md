# Tools

本目录保存项目开发工具的实现文件。正式入口由 Gradle task 提供，本目录不提供独立执行入口。

## 目录

- `pmd/`：PMD Java AST 规则。
- `formatter/`：文本、换行、缩进等 Python 格式化脚本。

## 当前实现

- `formatter/utf8_no_bom.py` 将带 UTF-8 BOM 的文本文件转换为无 BOM UTF-8。
- `formatter/to_crlf.py` 将文本文件换行统一为 CRLF。
- `formatter/java_tabs.py` 将 Java 行首四空格缩进转换为 tab 缩进。
- `formatter/java_single_final_blank_line.py` 将 Java 文件末尾统一为一个 CRLF，不保留额外空行。
- `formatter/remove_pycache.py` 删除项目中的 `__pycache__` 目录。
- `pmd/rules/` 保存 Java 实现警告规则，Gradle PMD plugin 直接加载规则 XML。

## Gradle 任务

- `formatProject`：执行 formatter 并写入修复。
- `verifyFormat`：执行 formatter 检查模式，不写入文件。
- `pmdMain`：使用 Gradle PMD plugin 执行 PMD 规则。
- `projectCheck`：格式化、编译并执行 PMD。
- `verifyProject`：只检查格式、编译并执行 PMD。
