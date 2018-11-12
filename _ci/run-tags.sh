#!/usr/bin/env bash
set -euo pipefail

tags="basics core_api traversal cypher_read cypher_write rest bolt extension csv"
for tag in ${tags}; do
    echo "Tag: ${tag}"
    git checkout -f "${tag}"
    mvn -q -B clean package -DskipTests
done

git checkout -f master
