#!/usr/bin/env python3
"""
进度检查工具
读取 feature_list.json 并输出进度报告
"""
import json
import sys
import os

def check_progress(feature_list_path):
    """检查并输出功能完成进度"""
    if not os.path.exists(feature_list_path):
        print(f"ERROR: {feature_list_path} 不存在")
        return 1

    with open(feature_list_path, 'r', encoding='utf-8') as f:
        features = json.load(f)

    total = len(features)
    passed = [f for f in features if f.get('passes', False)]
    failed = [f for f in features if not f.get('passes', False)]

    # 按类别统计
    categories = {}
    for f in features:
        cat = f.get('category', 'unknown')
        if cat not in categories:
            categories[cat] = {'total': 0, 'passed': 0}
        categories[cat]['total'] += 1
        if f.get('passes', False):
            categories[cat]['passed'] += 1

    pct = (len(passed) / total * 100) if total > 0 else 0

    print("=" * 60)
    print(f"  项目进度报告")
    print("=" * 60)
    print(f"  总功能数:   {total}")
    print(f"  已完成:     {len(passed)}")
    print(f"  未完成:     {len(failed)}")
    print(f"  完成率:     {pct:.1f}%")
    print()
    print(f"  {'类别':<20} {'完成/总数':<15} {'进度'}")
    print(f"  {'-'*20} {'-'*15} {'-'*20}")
    for cat, stats in sorted(categories.items()):
        cat_pct = (stats['passed'] / stats['total'] * 100) if stats['total'] > 0 else 0
        bar = '█' * int(cat_pct / 5) + '░' * (20 - int(cat_pct / 5))
        print(f"  {cat:<20} {stats['passed']}/{stats['total']:<13} {bar} {cat_pct:.0f}%")

    if failed:
        print()
        print("  下一个待实现功能:")
        # 按优先级排序
        failed.sort(key=lambda x: x.get('priority', 99))
        nxt = failed[0]
        print(f"    ID:       {nxt.get('id', 'N/A')}")
        print(f"    类别:     {nxt.get('category', 'N/A')}")
        print(f"    描述:     {nxt.get('description', 'N/A')}")
        print(f"    优先级:   {nxt.get('priority', 'N/A')}")

    print("=" * 60)
    return 0

if __name__ == '__main__':
    path = sys.argv[1] if len(sys.argv) > 1 else 'feature_list.json'
    sys.exit(check_progress(path))
