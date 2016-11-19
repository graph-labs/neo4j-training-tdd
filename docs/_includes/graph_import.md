Notez que, dans certains exercices, le graphe complet des données Doctor Who est importé avant l'exécution de chaque test puis supprimé ensuite. Le temps d'exécution est donc significativement impacté et peut vous ralentir dans la progression des exercices.

Afin de pouvoir explorer le graphe facilement et tester vos requêtes 
sans attendre, vous allez importer les données dans votre base locale. Pour se faire, il est nécessaire de :

1. importer le graphe avec `cypher-shell` (cf. répertoire `bin` de la base de données créée avec Neo4j Desktop ou de l'installation standalone):<br/>
```sh
$> cd /chemin/du/repository/neo4j-training-tdd
$> cat src/test/resources/dr-who.cypher | cypher-shell -u neo4j -pMOT_DE_PASSE
```
1. exécuter `MATCH (n:DoctorWho) RETURN COUNT(n)` dans la console de requêtage afin de vous assurer que l'import s'est effectué correctement (vous devriez voir une centaine de noeuds)

Notez que vous pouvez exécuter cette commande autant de fois que vous 
voulez. Elle détruit le graphe Doctor Who et le recrée à chaque 
exécution (vos autres données restent intactes).

