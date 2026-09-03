#!/bin/bash

echo "下载 PDF.js Worker 文件..."

# 下载 pdfjs-dist 2.16.105 的 worker 文件
curl -o public/pdf.worker.min.js https://cdn.jsdelivr.net/npm/pdfjs-dist@2.16.105/build/pdf.worker.min.js

if [ $? -eq 0 ]; then
    echo "✓ Worker 文件下载成功: public/pdf.worker.min.js"
    echo "✓ 版本: 2.16.105"
else
    echo "✗ Worker 文件下载失败"
    exit 1
fi
