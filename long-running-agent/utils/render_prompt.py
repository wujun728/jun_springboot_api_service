#!/usr/bin/env python3
"""
提示词模板渲染工具
将 prompts/*.md 中的 {{变量}} 占位符替换为 config.json 中的实际值
"""
import json
import sys
import os

def flatten_dict(d, parent_key='', sep='.'):
    """将嵌套字典展平为 key.subkey 形式"""
    items = []
    for k, v in d.items():
        new_key = f"{parent_key}{sep}{k}" if parent_key else k
        if isinstance(v, dict):
            items.extend(flatten_dict(v, new_key, sep=sep).items())
        else:
            items.append((new_key, str(v)))
    return dict(items)

def render(template_path, config_path, extra_vars=None):
    """渲染模板"""
    with open(template_path, 'r', encoding='utf-8') as f:
        template = f.read()

    with open(config_path, 'r', encoding='utf-8') as f:
        config = json.load(f)

    # 展平配置
    flat = flatten_dict(config)

    # 基础变量映射
    var_map = {
        'PROJECT_NAME': config.get('project_name', ''),
        'PROJECT_TYPE': config.get('project_type', ''),
        'PROJECT_DESCRIPTION': config.get('project_description', ''),
        'WORK_DIRECTORY': os.path.abspath(config.get('work_directory', '.')),
        'USER_REQUIREMENTS': config.get('user_requirements', ''),
        'FEATURE_LIST': config.get('paths', {}).get('feature_list', 'feature_list.json'),
        'PROGRESS_FILE': config.get('paths', {}).get('progress_file', 'claude-progress.txt'),
        'INIT_SCRIPT': config.get('paths', {}).get('init_script', 'init.sh'),
        'BUILD_COMMAND': config.get('java', {}).get('build_command', 'mvn clean package -DskipTests'),
        'TEST_COMMAND': config.get('java', {}).get('test_command', 'mvn test'),
        'RUN_COMMAND': config.get('java', {}).get('run_command', 'mvn spring-boot:run'),
    }

    # 追加额外变量
    if extra_vars:
        var_map.update(extra_vars)

    # 替换模板中的 {{VAR}}
    for key, value in var_map.items():
        template = template.replace('{{' + key + '}}', value)

    return template

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("用法: render_prompt.py <template.md> <config.json> [--session N]", file=sys.stderr)
        sys.exit(1)

    template_path = sys.argv[1]
    config_path = sys.argv[2]

    extra = {}
    i = 3
    while i < len(sys.argv):
        if sys.argv[i] == '--session' and i + 1 < len(sys.argv):
            extra['SESSION_NUMBER'] = sys.argv[i + 1]
            i += 2
        else:
            i += 1

    print(render(template_path, config_path, extra))
