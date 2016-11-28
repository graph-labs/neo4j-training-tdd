---
---
# Formation Neo4j

Retour à l'[accueil](..).

 1. ["Échauffement"](../basics/)
 1. ["API unitaires"](../core_api/)
 1. ["Traversées"](../traversal/)
 1. ["Cypher en lecture"](../cypher_reading/)
 1. ["Cypher en écriture"](../cypher_writing/)
 1. ["API REST"](../rest/)
 1. ["Extensions Neo4j"](../extensions/)
 
## Bolt

À l'instar de la série d'exercice sur les API REST, cette série d'exercices sur Bolt 
va dépendre de l'instance Neo4j que vous avez installée sur votre machine. 
Par conséquent, il est nécessaire d'importer les données Doctor Who afin que les tests 
puissent s'exécuter et de laisser votre instance Neo4j active.

### Import

Pour importer le graphe, veuillez vous référer à la section "Import" de la série
["Cypher en lecture"](../cypher_reading/).

### Authentification

Il est nécessaire de spécifier votre mot de passe dans `src/test/resources/credentials.properties`.