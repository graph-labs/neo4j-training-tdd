Notez que, dans certains exercices, le graphe complet des données Doctor Who est importé avant l'exécution de chaque test puis supprimé ensuite. Le temps d'exécution est donc significativement impacté et peut vous ralentir dans la progression des exercices.

Afin de pouvoir explorer le graphe facilement et tester vos requêtes 
sans attendre, vous allez importer les données dans votre base locale. Pour se faire, il est nécessaire de :

1. **stopper** votre base de données Neo4j locale
2. importer le graphe avec `neo4j-shell` (voir <a href="/neo4j-training/setup/#je-nai-pas-de-script-neo4j-shell-ou-neo4j-shellbat">instructions</a> si vous n'avez pas l'outil): <br/><br/>
```shell
$> neo4j-shell -file src/test/resources/dr-who.cypher -path /chemin/vers/graph.db
```
3. **redémarrer** votre base
4. exécuter `MATCH (n:DoctorWho) RETURN COUNT(n)` dans la console de requêtage afin de vous assurer que l'import s'est effectué correctement (vous devriez voir une centaine de noeuds)

Notez que vous pouvez exécuter cette commande autant de fois que vous 
voulez. Elle détruit le graphe Doctor Who et le recrée à chaque 
exécution (vos autres données restent intactes).

