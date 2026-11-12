# Consignes de revue

Ce dépôt contient une API REST éducative en Java 21 et Spring Boot, construite avec Maven.

## Lors d'une revue

- Chercher les erreurs fonctionnelles, régressions et risques de sécurité concrets. Signaler les problèmes avec leur fichier et leur ligne.
- Vérifier que les changements liés au comportement sont couverts par des tests adaptés, en particulier pour l'authentification, les jetons JWT et les endpoints de mots.
- Pour la sécurité, porter une attention particulière aux secrets, mots de passe, contrôles d'accès, entrées HTTP et configuration H2/JWT.
- Ne pas signaler comme problème une préférence de style déjà vérifiée par Spotless.
- Ne pas proposer d'outils ou de changements d'architecture sans besoin concret.
- Si aucun problème important n'est trouvé, le dire clairement. Distinguer les constats certains des points à vérifier.

## Vérifications

La CI exécute `mvn --batch-mode verify`. Pour reproduire ses contrôles localement, utiliser `./mvnw verify`.
