---
layout: page
title: Protocole Bolt
permalink: /bolt/
order: 70
is_exercise: true
---

## Git

```shell
➜ ~ git checkout -f bolt
```

## Structure

À l'instar de la série d'exercice sur les API REST, cette série d'exercices sur Bolt 
va dépendre de l'instance Neo4j que vous avez installée sur votre machine. 
Par conséquent, il est nécessaire d'importer les données Doctor Who afin que les tests 
puissent s'exécuter et de laisser votre instance Neo4j active.

### Import

Pour importer le graphe, veuillez vous référer à la section "Import" de la série
["Cypher en lecture"](../cypher_reading/).

### Authentification

Il est nécessaire de spécifier votre mot de passe dans `src/test/resources/credentials.properties`.