FROM neo4j:3.4.9

COPY src/test/resources/dr-who.cypher /var/lib/neo4j/import

#CMD ["cat", "/var/lib/neo4j/import/dr-who.cypher", "|", "/var/lib/neo4j/bin/cypher-shell", "-u", "neo4j", "-p", "admin"]