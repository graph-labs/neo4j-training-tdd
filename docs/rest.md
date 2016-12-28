---
layout: page
title: REST
permalink: /rest/
order: 60
---

## Git

```shell
➜ ~ cd neo4j-training
➜ ~ git checkout -f rest
```

## Structure

Cette série d'exercices va dépendre de l'instance Neo4j que vous avez installée
sur votre machine. Par conséquent, il est nécessaire d'importer les données
Doctor Who afin que les tests puissent s'exécuter et de laisser votre instance 
Neo4j active.

### Import

{% include graph_import.md %}


### Authentification

Il est nécessaire de spécifier votre mot de passe dans `src/test/resources/credentials.properties`.