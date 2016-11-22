---
layout: page
title: Cypher en lecture
permalink: /cypher-reads/
order: 40
is_exercise: true
---

## Git

```shell
➜ ~ git checkout -f cypher_read
```

## Structure

Cette série d'exercices se divise de la sorte :

1. requêtes `MATCH` simples
1. requêtes avec `OPTIONAL MATCH`
1. requêtes d'agrégation
1. manipulation de *path*
1. utilisation des fonctions de collection
1. utilisation de `WITH`

## Doctor Who ? Doctor Who !

Cette série d'exercices porte sur la série légendaire
[Doctor Who](https://fr.wikipedia.org/wiki/Doctor_Who).

Pour comprendre un peu mieux les données du graphe, voici un rapide
résumé pour les profanes, extrait de Wikipedia:

> **Doctor Who** (trad. litt. : « Docteur Qui » est une série télévisée 
> britannique de science-fiction créée par Sydney Newman et Donald Wilson
> et **diffusée depuis le 23 novembre 1963** sur BBC One. 
> 
> Elle raconte les aventures du Docteur, qui voyage à travers l'espace 
> et le temps à bord d'un vaisseau spatial, le TARDIS (Time And Relative 
> Dimension In Space, qu'on trouve aussi traduit en version française par 
> « Temps à relativité dimensionnelle inter-spatiale »).
> 
> Lorsque le Docteur est mortellement blessé, il peut survivre en se régénérant ; 
> il change alors d'apparence et, dans une certaine mesure, de personnalité, mais 
> tout en conservant le souvenir de ses vies antérieures.
> 
> [...]
>
> Les principaux adversaires du Docteur, dans l'ancienne série, sont les
> Autons, les Cybermen, les Sontariens, les Zygons, les Silurians, le Wirrn,
> le Yéti, les guerriers de glace (Ice Warriors), le Maître (un Seigneur du 
> Temps ayant une soif de conquête universelle), et, les plus notables, 
> les Daleks, avec ou sans leur créateur Davros.
>
> Cette tradition des monstres a continué avec la nouvelle série, 
> dans laquelle sont réapparus, entre autres, les Daleks, les Cybermen, 
> le Maître, les Autons, les Sontariens, les Macras et les Silurians 
> ainsi que de nouveaux ennemis tel que les Slitheen, les Judoon, 
> les Anges Pleureurs et le Silence.
>
> [...]
> 
> Le Docteur partage presque toutes ses aventures avec au plus trois compagnons,
> et depuis 1963, plus de 35 acteurs et actrices ont joué dans ces rôles. 
> À l’origine, les compagnons du premier Docteur étaient sa petite-fille 
> Susan Foreman (Carole Ann Ford) et les professeurs Barbara Wright (Jacqueline Hill) et 
> Ian Chesterton (William Russell). La seule histoire, dans l’ancienne série, où le 
> Docteur voyage seul est The Deadly Assassin.

## Explorer le graph local

### Import

{% include graph_import.md %}

### Méta-graphe

Afin de visualiser le "méta-graphe", vous pouvez exécuter cette requête :
```
MATCH (a)-[r]->(b)  
WITH [a_label IN labels(a) WHERE a_label <> 'DoctorWho'] AS a_labels,
     type(r) AS rel_type,
     [b_label IN labels(b) WHERE b_label <> 'DoctorWho'] AS b_labels  
UNWIND a_labels as label1  
UNWIND b_labels as label2
RETURN DISTINCT label1 AS first_node, rel_type AS connected_by, label2 AS second_node
```

Cela vous permet de lister toutes les relations qui existent entre les
différents labels du graphe.

### Zoom sur le méta-graphe

Si vous êtes intéressé-e par un label en particulier, vous pouvez filter de la sorte (exemple
avec le label **`Character`**) :

```
MATCH (a)-[r]->(b)  
WITH [a_label IN labels(a) WHERE a_label <> 'DoctorWho'] AS a_labels,
     type(r) AS rel_type,
     [b_label IN labels(b) WHERE b_label <> 'DoctorWho'] AS b_labels  
UNWIND a_labels as label1  
UNWIND b_labels as label2
WITH label1, label2, rel_type
WHERE label1 = 'Character' OR label2 = 'Character'
RETURN DISTINCT label1 AS first_node, rel_type AS connected_by, label2 AS second_node
```

Pour un type de relation en particulier, la solution est très similaire :

```
MATCH (a)-[r]->(b)  
WITH [a_label IN labels(a) WHERE a_label <> 'DoctorWho'] AS a_labels,
     type(r) AS rel_type,
     [b_label IN labels(b) WHERE b_label <> 'DoctorWho'] AS b_labels  
UNWIND a_labels as label1  
UNWIND b_labels as label2
WITH label1, label2, rel_type
WHERE rel_type = 'ALLY_OF'
RETURN DISTINCT label1 AS first_node, rel_type AS connected_by, label2 AS second_node
```

La combinaison des deux est un exercise laissé au lecteur ;-)

Notez que cette requête est une adaptation de l'excellent [article](http://blog.bruggen.com/2015/03/hidden-graphgems-meta-graph.html) 
de [Rik Van Bruggen](https://twitter.com/rvanbruggen), 
antérieur à Neo4j 3.x. 