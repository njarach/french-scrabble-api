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
| `POST` | `/api/auth/register` | Crée un compte et retourne un jeton JWT |
| `POST` | `/api/auth/login` | Authentifie un compte et retourne un jeton JWT |

Les routes `/api/words/**` nécessitent un jeton transmis dans l'en-tête `Authorization: Bearer <token>`. Les mots de passe sont stockés sous forme hachée avec BCrypt. Le nom d'utilisateur doit faire 3 à 30 caractères ; le mot de passe doit faire 8 à 72 caractères.

Exemple d'inscription ou de connexion :

```json
{"username":"joueur1","password":"motdepasse123"}
```

La réponse contient `accessToken`, `tokenType`, `expiresIn` et `username`. En production, définissez `SCRABBLE_JWT_SECRET` avec une clé secrète Base64 d'au moins 32 octets. La clé définie par défaut est réservée au développement local.

## Données

Le dictionnaire ODS9 est chargé au démarrage depuis un fichier texte (`ods9.txt`) embarqué dans les ressources. Le chargement est ignoré si la base est déjà peuplée.

Console H2 disponible sur `http://localhost:8080/h2-console`.

## Lancer le projet

```bash
./mvnw spring-boot:run
```

## Roadmap

- [x] **Authentification** — Spring Security + JWT
- [ ] **Définitions** — exposer la définition des mots (à étudier : existence d'une API publique ODS/Wiktionnaire ?)
- [ ] **Client consommateur** — projet séparé (React / autre) qui consomme cette API
