# scrabble-ods-api

API REST Spring Boot à vocation éducative, permettant de vérifier et trouver des mots valides au Scrabble (dictionnaire ODS9).

## Stack

- Java / Spring Boot
- H2 (base de données embarquée, fichier local)
- JPA / Hibernate
- Lombok

## Endpoints

| Méthode | Route | Description |
|--------|-------|-------------|
| `GET` | `/api/words/check?letters=MOT` | Vérifie si un mot existe dans l'ODS9 |
| `GET` | `/api/words/cheat?letters=LETTRES` | Retourne tous les mots formables depuis ces lettres |

## Données

Le dictionnaire ODS9 est chargé au démarrage depuis un fichier texte (`ods9.txt`) embarqué dans les ressources. Le chargement est ignoré si la base est déjà peuplée.

Console H2 disponible sur `http://localhost:8080/h2-console`.

## Lancer le projet

```bash
./mvnw spring-boot:run
```

## Roadmap

- [ ] **Authentification** — sécuriser les endpoints (Spring Security + JWT ou session)
- [ ] **Définitions** — exposer la définition des mots (à étudier : existence d'une API publique ODS/Wiktionnaire ?)
- [ ] **Client consommateur** — projet séparé (React / autre) qui consomme cette API
