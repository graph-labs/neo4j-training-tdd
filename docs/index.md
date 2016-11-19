---
layout: page
title: Introduction à Neo4j
---

## Pré-requis

Merci de vous référer à la [section dédiée](./setup/). 

## Structure des exercices

Les exercices sont regroupés par thème. Ils suivent l'ordre des slides de la formation.
Prenez bien le temps de lire l'énoncé à chaque fois. 

Chaque exercice est composé d'une série de tests.

Les tests sont des méthodes Java annotées avec `@Test` (1 test = 1 méthode).

Chaque test est composé de différentes assertions, généralement
commençant par `assertThat(...)`.

Il est important de ne pas modifier les assertions.
En effet, celles-ci décrivent le résultat attendu. Il est donc primordial
de les lire attentivement et de les comprendre.

Il y aura toujours du code à compléter (voir les sections
commentées `TODO`).

Tous les tests se trouvent dans `src/test/java/io/github/fbiville/trainings/neo4j` ([conventions Maven](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html))
et sont à résoudre dans l'ordre dans lequel **ils sont déclarés**
(la librairie de test utilisée, JUnit, ne garantit pas l'ordre d'exécution).

## Exécution des exercices

Chaque test est indépendant. Si, par exemple, vous avez créé
un noeud dans un test précédent, celui-ci ne sera pas présent 
lors de l'exécution du test suivant.

Notez néanmoins que chaque classe doit compiler correctement.

Si vous bloquez sur un exercice et souhaitez passer au suivant, 
il faudra a minima que la classe Java de l'exercice compile toujours.

Pour exécuter les tests, vous avez le choix:

 - **utilisateurs d'Intellij** : cliquez droit sur le test ou le fichier complet
 et sélectionnez `Run 'NOM_DE_METHODE|NOM_DE_CLASSE'`
 - **utilisateurs de la ligne de commande** : 

```shell
  $> cd /chemin/vers/neo4j-training
  # pour un test particulier
  $> mvn clean test -Dtest=NOM_DE_CLASSE#NOM_DE_METHODE
  # pour une classe complète
  $> mvn clean test -Dtest=NOM_DE_CLASSE
  # pour tous les tests
  $> mvn clean test
```

## Conseils

N'hésitez pas à travailler en binôme si vous le pouvez et à vous entraider !

Si vous êtes bloqué sur un exercice :

1. vous êtes chanceux et participez à la formation, le formateur est là pour vous aider
1. vous n'êtes pas en formation, la meilleure solution est de rejoindre le [Gitter dédié](gitter.im/neo4j-developer-training) et d'y poser vos questions !

Soyez curieux ! Explorez les API de Neo4j, elles vous donneront
la réponse que vous cherchez. (L'autocomplétion de l'IDE aide beaucoup.)

Notez enfin que la solution la plus simple est souvent la meilleure ;-)
