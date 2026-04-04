#!/usr/bin/env bash
# build.sh — Compile and run the Merchandise Catalog app
# Requires: Java 17+

set -e

SRC_DIR="src/main/java"
OUT_DIR="out"
MAIN_CLASS="catalog.Main"

echo "▸ Compiling..."
mkdir -p "$OUT_DIR"
find "$SRC_DIR" -name "*.java" | xargs javac -d "$OUT_DIR"

echo "▸ Done. Starting application..."
echo ""
java -cp "$OUT_DIR" "$MAIN_CLASS"
